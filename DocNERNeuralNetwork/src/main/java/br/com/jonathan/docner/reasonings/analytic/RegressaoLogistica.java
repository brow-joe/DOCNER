package br.com.jonathan.docner.reasonings.analytic;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.reasonings.IReasoning;
import br.com.jonathan.docner.reasonings.ReasoningException;
import br.com.jonathan.docner.vo.RNDataInVO;
import opennlp.tools.ml.maxent.GIS;
import opennlp.tools.ml.maxent.io.GISModelReader;
import opennlp.tools.ml.maxent.io.PlainTextGISModelWriter;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.DataReader;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.PlainTextFileDataReader;
import opennlp.tools.ml.model.TwoPassDataIndexer;
import opennlp.tools.util.ObjectStream;

public class RegressaoLogistica implements IReasoning{
	protected final Logger LOGGER = LogManager.getLogger( RegressaoLogistica.class );

	private final Integer ITERATION;
	private final Integer CUTOFF;

	public RegressaoLogistica( int iteration, int cutOff ){
		this.ITERATION = iteration;
		this.CUTOFF = cutOff;
	}

	public RegressaoLogistica(){
		this( 100, 5 );
	}

	@Override
	public RNDataInVO trainer( Integer sequence, String dataSet, String[ ] sequential ) throws ReasoningException {
		try {
			ObjectStream< Event > stream = new ByteArrayEventStream( 
					new ByteArrayInputStream( dataSet.getBytes() ) 
			);
			TwoPassDataIndexer data = new TwoPassDataIndexer( stream, CUTOFF );
			AbstractModel model = GIS.trainModel( ITERATION, data );

			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			BufferedWriter buffer = new BufferedWriter( new OutputStreamWriter( writer ) );
			new PlainTextGISModelWriter( model, buffer ).persist();
			buffer.close();
			stream.close();
			
			return new RNDataInVO( 
					sequence,
					EReasoningAnalytic.REGRESSAO_LOGISTICA_MULTINOMIAL, 
					writer, 
					getResultSequential(writer, sequential) 
			);
			
		} catch ( Exception e ) {
			LOGGER.error( e );
			throw new ReasoningException( e );
		}
	}
	
	@Override
	public void classifier( RNDataInVO in, String[ ] classifier ) throws ReasoningException {
		try {
			in.setResultSequential( getResultSequential( in.getModel(), classifier ) );
		} catch ( IOException e ) {
			LOGGER.error( e );
			throw new ReasoningException( e );
		}
	}

	private Map< Integer, Map< String, Double > > getResultSequential( ByteArrayOutputStream writer, String[ ] sequential ) throws IOException {
		Map< Integer, Map< String, Double > > resultSequential = new HashMap< >();

		InputStream stream = new ByteArrayInputStream( writer.toByteArray() );
		DataReader dataReader = new PlainTextFileDataReader( stream );
		AbstractModel model = new GISModelReader( dataReader ).getModel();

		String[ ] outcomes = (String[ ]) model.getDataStructures()[ 2 ];

		int line = 0;
		for ( String context : sequential ) {
			context = context.replaceAll( "  +", " " ).trim();
			double[ ] coeficients = model.eval( context.split( " " ) );

			Map< String, Double > predictor = new HashMap< >();
			for ( int index = 0; index < coeficients.length; index++ ) {
				predictor.put( outcomes[ index ], coeficients[ index ] );
			}
			resultSequential.put( line++, predictor );
		}
		stream.close();

		return resultSequential;
	}

}