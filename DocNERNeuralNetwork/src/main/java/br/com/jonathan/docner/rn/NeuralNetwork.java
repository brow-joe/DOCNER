package br.com.jonathan.docner.rn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.reasonings.ReasoningException;
import br.com.jonathan.docner.reasonings.ReasoningMappingResolver;
import br.com.jonathan.docner.vo.RNDataInVO;
import br.com.jonathan.docner.vo.RNVO;

public class NeuralNetwork implements INeuralNetwork{
	protected final Logger LOGGER = LogManager.getLogger( NeuralNetwork.class );
	private final String FEATURE_SEPARATOR = "\\s{2,}";
	private final String DOCUMENT_SEPARATOR = System.lineSeparator();

	private final String MSG_DATASET_EMPTY = "DataSet está vazio!";
	private final String MSG_REASONINGS_EMPTY = "Nenhum raciocinio definido!";
	private final String MSG_REASONINGS_COLLECT_EMPTY = "Nenhum dado coletado!";
	
	private ReasoningMappingResolver resolver;
	
	public NeuralNetwork(){
		super();
		this.resolver = new ReasoningMappingResolver();
	}

	@Override
	public RNVO trainer( String dataSet, List< EReasoningLogic > logics, List< EReasoningAnalytic > analytics ) throws NeuralException {
		if ( CollectionUtils.isEmpty( logics ) && CollectionUtils.isEmpty( analytics ) ) {
			throw new NeuralException( MSG_REASONINGS_EMPTY );
		}

		if ( StringUtils.isNotEmpty( dataSet ) ) {
			dataSet = dataSet
					.replaceAll( "(?m)(^ *| +(?= |$))", "" )
					.replaceAll( "(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1" )
					.trim();
			
			if ( StringUtils.isEmpty( dataSet ) ) {
				throw new NeuralException( MSG_DATASET_EMPTY );
			}
			
			dataSet = Arrays.stream( dataSet.split( DOCUMENT_SEPARATOR ) ).map( document -> {
				return document
						.replaceAll( FEATURE_SEPARATOR, " " )
						.trim();
			} ).filter(
					str -> StringUtils.isNotEmpty( str.trim() ) 
			).collect( Collectors.joining( DOCUMENT_SEPARATOR ) )
					.trim();
			
			return collectReasoning( dataSet, logics, analytics );
		} else {
			throw new NeuralException( MSG_DATASET_EMPTY );
		}
	}

	private RNVO collectReasoning( String dataSet, List< EReasoningLogic > logics, List< EReasoningAnalytic > analytics ) throws NeuralException {
		LinkedList< RNDataInVO > dataInListed = new LinkedList< >();
		
		String splited = new String( dataSet );
		List< String > removed = Arrays.stream( 
				splited.split( DOCUMENT_SEPARATOR ) 
		).map( s -> s.trim().split( " " )[ 0 ].trim() )
				.distinct()
				.collect( Collectors.toList() );
		
		for ( String remove : removed ) {
			splited = splited.replaceAll( Pattern.quote( remove ) + " ", "? " ).trim();
		}
		String[ ] sequential = splited.trim().split( DOCUMENT_SEPARATOR );
		
		try {
			int sequence = 0;
			if ( CollectionUtils.isNotEmpty( logics ) ) {
				for ( EReasoningLogic logic : logics ) {
					RNDataInVO dataIn = resolver.resolve( sequence++, logic, dataSet, sequential );
					if ( Objects.nonNull( dataIn ) ) {
						dataInListed.add( dataIn );
					}
				}
			}

			if ( CollectionUtils.isNotEmpty( analytics ) ) {
				for ( EReasoningAnalytic analytic : analytics ) {
					RNDataInVO dataIn = resolver.resolve( sequence++, analytic, dataSet, sequential );
					if ( Objects.nonNull( dataIn ) ) {
						dataInListed.add( dataIn );
					}
				}
			}
		} catch ( ReasoningException e ) {
			throw new NeuralException( e );
		}
		
		if ( CollectionUtils.isEmpty( dataInListed ) ) {
			throw new NeuralException( MSG_REASONINGS_COLLECT_EMPTY );
		} else {
			LOGGER.info( "REASONINGS DETECTED: " + dataInListed.size() );
		}
		
		//TODO Falta implementar a rede neural em si
		LOGGER.info( "-------> Resultado dos raciocinios que serão emitido para a rn <-------" );
		dataInListed.forEach( data->{
			System.out.println( data.getAnalytic()+" " + data.getLogic() );
			for(Entry< Integer, Map< String, Double > > a : data.getResultSequential().entrySet()){
				System.out.print( "\t" );
				for(Entry< String, Double > b : a.getValue().entrySet()){
					System.out.print( b.getValue()+"\t" );
				}
				System.out.println(  );
			}
			System.out.println( "\n" );
		});
		return null;
	}

}