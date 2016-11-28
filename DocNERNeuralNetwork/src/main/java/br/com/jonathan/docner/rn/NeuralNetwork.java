package br.com.jonathan.docner.rn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.reasonings.ReasoningException;
import br.com.jonathan.docner.reasonings.ReasoningMappingResolver;
import br.com.jonathan.docner.vo.RNDataInVO;
import br.com.jonathan.docner.vo.RNDataOutVO;
import br.com.jonathan.docner.vo.ResultSetVO;

public class NeuralNetwork implements INeuralNetwork{
	protected final Logger LOGGER = LogManager.getLogger( NeuralNetwork.class );
	private final String FEATURE_SEPARATOR = "\\s{2,}";
	private final String DOCUMENT_SEPARATOR = System.lineSeparator();

	private final String MSG_DATASET_EMPTY = "DataSet está vazio!";
	private final String MSG_REASONINGS_EMPTY = "Nenhum raciocinio definido!";
	private final String MSG_REASONINGS_COLLECT_EMPTY = "Nenhum dado coletado!";
	
	private final String MSG_DATACLASSIFIER_MODEL_EMPTY = "ClassifierSet ou modelo vazios!";
	
	private ReasoningMappingResolver resolver;
	
	public NeuralNetwork(){
		super();
		this.resolver = new ReasoningMappingResolver();
	}

	@Override
	public RNDataOutVO trainer( String dataSet, List< EReasoningAnalytic > analytics, List< EReasoningLogic > logics ) throws NeuralException {
		if ( CollectionUtils.isEmpty( analytics ) && CollectionUtils.isEmpty( logics ) ) {
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
			
			return collectReasoning( dataSet, analytics, logics );
		} else {
			throw new NeuralException( MSG_DATASET_EMPTY );
		}
	}
	
	@Override
	public List< ResultSetVO > classifier( String classifierSet, RNDataOutVO model ) throws NeuralException {
		if ( Objects.nonNull( model ) && StringUtils.isNotEmpty( classifierSet ) ) {
			String[ ] classifier = classifierSet.split( DOCUMENT_SEPARATOR );
			List< ResultSetVO > resulter = new ArrayList<>();
			
			List< RNDataInVO > modelIn = new ArrayList< >( model.getModels() );
			
			modelIn.stream().forEach( in -> {
				try {
					resolver.classifierResolver( in, classifier );
				} catch ( Exception e ) {
					LOGGER.error( e );
				}
			} );
			
			if(CollectionUtils.isNotEmpty( modelIn )){
				Collections.sort( 
						modelIn, 
						( l, r ) -> Integer.compare( 
								l.getSequence(), 
								r.getSequence() 
						) 
				);
				
				try{
					resulter.addAll( new RNExecutor().classifier( classifier, model ) );
				}catch ( RNException e ) {
					LOGGER.fatal( e );
					throw new NeuralException( e );
				}
			}
			
			return resulter;
		}
		throw new NeuralException( MSG_DATACLASSIFIER_MODEL_EMPTY );
	}

	private RNDataOutVO collectReasoning( String dataSet, List< EReasoningAnalytic > analytics, List< EReasoningLogic > logics ) throws NeuralException {
		List< RNDataInVO > dataInListed = new ArrayList< >();
		
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
			if ( CollectionUtils.isNotEmpty( analytics ) ) {
				for ( EReasoningAnalytic analytic : analytics ) {
					RNDataInVO dataIn = resolver.trainerResolve( sequence++, analytic, dataSet, sequential );
					if ( Objects.nonNull( dataIn ) ) {
						dataInListed.add( dataIn );
					}
				}
			}

			if ( CollectionUtils.isNotEmpty( logics ) ) {
				for ( EReasoningLogic logic : logics ) {
					RNDataInVO dataIn = resolver.trainerResolve( sequence++, logic, dataSet, sequential );
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
		
		try {
			Collections.sort( 
					dataInListed, 
					( l, r ) -> Integer.compare( 
							l.getSequence(), 
							r.getSequence() 
					) 
			);
			
			return new RNExecutor().trainer( 
					dataSet,
					new RNDataOutVO( dataInListed ) 
			);
		} catch ( RNException e ) {
			LOGGER.fatal( e );
			throw new NeuralException( e );
		}
	}

}