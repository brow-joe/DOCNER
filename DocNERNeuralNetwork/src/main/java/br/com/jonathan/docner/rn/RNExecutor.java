package br.com.jonathan.docner.rn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.PersistBasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import br.com.jonathan.docner.vo.RNDataInVO;
import br.com.jonathan.docner.vo.RNDataOutVO;
import br.com.jonathan.docner.vo.ResultSetVO;

public class RNExecutor{
	protected final Logger LOGGER = LogManager.getLogger( RNExecutor.class );
	
	private final double ERROR;
	private final int ITERATIONS;
	private final double LIMIAR;

	public RNExecutor(){
		this( 0.01, 100, 0.5 );
	}

	public RNExecutor( double error, int iterations, double limiar ){
		super();
		ERROR = error;
		ITERATIONS = iterations;
		LIMIAR = limiar;
	}

	public RNDataOutVO trainer( String dataSet, RNDataOutVO dataOut ) throws RNException {
		try{
			Map< Integer, String > context = getContext( dataSet );
			
			List< String > outcomes = context.entrySet()
					.stream().map( s -> s.getValue() )
					.distinct().collect( Collectors.toList() );
			
			int documents = dataSet.split( System.lineSeparator() ).length;
			
			ByteArrayOutputStream model = getStreamTrainer( context, outcomes, dataOut, documents );
			dataOut.setOutcomes( outcomes );
			dataOut.setModel( model );
			
			dataOut.getModels().forEach(
					models->models.setResultSequential( null ) 
			);
			
			return dataOut;
		}catch(Exception e){
			LOGGER.error( e );
			throw new RNException( e );
		}
	}
	
	public List< ResultSetVO > classifier( String[ ] classifier, RNDataOutVO model ) throws RNException {
		try {
			PersistBasicNetwork persistence = new PersistBasicNetwork();
			BasicNetwork network = (BasicNetwork) persistence.read( 
					new ByteArrayInputStream( model.getModel().toByteArray() ) 
			);
			int inputNeuron = model.getOutcomes().size() * model.getModels().size();
			
			MLDataSet dataClassifier = createDataClassifier( 
					getInputs( classifier.length, inputNeuron, model, model.getOutcomes() ) 
			);
			
			List< ResultSetVO > result = classifier( dataClassifier, model.getOutcomes(), network, model.getModels() );
			
			model.getModels().forEach(
					models->models.setResultSequential( null ) 
			);
			
			return result;
		} catch ( Exception e ) {
			LOGGER.error( e );
			throw new RNException( e );
		}
	}
	
	private List< ResultSetVO > classifier( MLDataSet dataClassifier, List< String > outcomes, BasicNetwork network, List< RNDataInVO > models ) throws RNException {
		List< ResultSetVO > result = new ArrayList<>();
		int index = 0;

		for ( MLDataPair pair : dataClassifier ) {
			MLData data = configure( network.compute( pair.getInput() ) );
			String outcome = getOutcome( data.getData(), outcomes );
			Map< String, Double > coeficients = new HashMap<>();

			final int idx = index;
			models.stream().map( r -> r.getResultSequential().get( idx ) ).forEach( r -> {
				for ( Entry< String, Double > out : r.entrySet() ) {
					Double coeficient = coeficients.get( out.getKey() );
					coeficient = ( coeficient == null ? 0 : coeficient ) + out.getValue();
					coeficients.put( out.getKey(), coeficient );
				}
			} );

			int size = models.size();
			for ( Entry< String, Double > entry : coeficients.entrySet() ) {
				coeficients.put( entry.getKey(), entry.getValue() / size );
			}

			ResultSetVO vo = new ResultSetVO();
			vo.setIndex( index++ );
			vo.setOutcome( outcome );
			vo.setProbs( coeficients );
			result.add( vo );
		}
		return result;
	}
	
	private MLData configure( MLData out ) {
		double[ ] result = out.getData();
		for ( int index = 0; index < result.length; index++ ) {
			double coeficient = result[ index ];
			if ( coeficient >= LIMIAR ) {
				result[ index ] = 1;
			} else {
				result[ index ] = 0;
			}
		}
		out.setData( result );
		return out;
	}
	
	private String getOutcome( double[ ] data, List< String > outcomes ) {
		String outcome = "?";

		for ( int index = 0; index < outcomes.size(); index++ ) {
			double[ ] neuron = new double[ outcomes.size() ];
			neuron[ index ] = 1;
			if ( Arrays.equals( neuron, data ) ) {
				outcome = outcomes.get( outcomes.size() - 1 - index );
				break;
			}
		}

		return outcome;
	}

	private MLDataSet createDataClassifier( double[ ][ ] input ) throws RNException {
		MLDataSet classifier = new BasicMLDataSet();
		Arrays.stream( input ).forEach( data -> {
			MLData ml = new BasicMLData( data );
			classifier.add( ml );
		} );
		return classifier;
	}

	private ByteArrayOutputStream getStreamTrainer( Map< Integer, String > context, List< String > outcomes, RNDataOutVO dataOut, int documents ) throws Exception {
		int neuronOutput = outcomes.size();
		int inputNeuron = neuronOutput * dataOut.getModels().size();
		
		BasicNetwork network = configureNeuralNetwork( inputNeuron, neuronOutput );
		MLDataSet dataSet = createDataSet( documents, inputNeuron, dataOut, context, outcomes );
		Propagation trainer = createTrainer( network, dataSet );
		
		trainer( trainer );
		LOGGER.info( "Neural netWork Done..." );
		
		ByteArrayOutputStream model = new ByteArrayOutputStream();
		PersistBasicNetwork persistence = new PersistBasicNetwork();
		persistence.save( model, network );
		return model;
	}
	
	private void trainer( Propagation trainer ) throws Exception {
		int iteration = 1;
		do {
			trainer.iteration();
			System.out.println( "Iteracao #" + iteration++ + " Erro:" + trainer.getError() );
		} while ( trainer.getError() > ERROR && iteration < ITERATIONS );

		trainer.finishTraining();
	}
	
	private Propagation createTrainer( BasicNetwork network, MLDataSet dataSet ) throws Exception {
		return new ResilientPropagation( network, dataSet );
	}
	
	private MLDataSet createDataSet( int size, int inputNeuron, RNDataOutVO dataOut, Map< Integer, String > context, List< String > outcomes ) throws Exception {
		double[ ][ ] input = getInputs( size, inputNeuron, dataOut, outcomes );
		double[ ][ ] ideal = getOutputs( size, outcomes, context );
		return new BasicMLDataSet( input, ideal );
	}
	
	private double[ ][ ] getOutputs( int size, List< String > outcomes, Map< Integer, String > context ) throws Exception {
		double[ ][ ] output = new double[ size ][ outcomes.size() ];

		Map< String, double[ ] > neurons = new HashMap<>();
		for ( int o = 0; o < outcomes.size(); o++ ) {
			String outcome = outcomes.get( outcomes.size() - 1 - o );
			double[ ] neuron = new double[ outcomes.size() ];
			neuron[ o ] = 1;
			neurons.put( outcome, neuron );
		}

		for ( Entry< Integer, String > entry : context.entrySet() ) {
			output[ entry.getKey() ] = neurons.get( entry.getValue() );
		}

		return output;
	}
	
	private double[ ][ ] getInputs( int size, int inputNeuron, RNDataOutVO dataOut, List< String > outcomes ) throws Exception {
		double[ ][ ] input = new double[ size ][ inputNeuron ];

		int resultIndex = 0;
		int acumulator = 0;
		for ( RNDataInVO dataIn : dataOut.getModels() ) {
			Map< Integer, Map< String, Double > > result = dataIn.getResultSequential();
			for ( Entry< Integer, Map< String, Double > > entry : result.entrySet() ) {
				for ( int index = 0; index < outcomes.size(); index++ ) {
					double coeficient = entry.getValue().get( outcomes.get( index ) );
					input[ resultIndex ][ acumulator + index ] = coeficient * 100;
				}
				resultIndex++;
			}
			acumulator += 2;
			resultIndex = 0;
		}
		return input;
	}
	
	private BasicNetwork configureNeuralNetwork( int neuronInput, int neuronOutput ) throws Exception {
		BasicNetwork redeNeural = new BasicNetwork();
		redeNeural.addLayer( new BasicLayer( null, true, neuronInput ) );
		redeNeural.addLayer( new BasicLayer( new ActivationSigmoid(), false, neuronOutput ) );
		redeNeural.getStructure().finalizeStructure();
		redeNeural.reset();
		return redeNeural;
	}
	
	private Map< Integer, String > getContext( String trainerSet ) throws Exception {
		Map< Integer, String > context = new HashMap<>();
		trainerSet = trainerSet.replaceAll( "  +", " " ).trim();
		int line = 0;
		for ( String str : trainerSet.split( System.lineSeparator() ) ) {
			String label = str.trim().split( " " )[ 0 ].trim();
			context.put( line++, label );
		}
		return context;
	}

}