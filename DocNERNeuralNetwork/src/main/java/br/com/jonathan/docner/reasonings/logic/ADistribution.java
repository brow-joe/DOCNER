package br.com.jonathan.docner.reasonings.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import br.com.jonathan.docner.reasonings.IReasoning;

public abstract class ADistribution implements IReasoning{
	protected final Logger LOGGER;
	protected final double ALPHA;

	public ADistribution( Logger logger ){
		this( logger, 0.01 );
	}

	public ADistribution( Logger logger, double alpha ){
		this.LOGGER = logger;
		this.ALPHA = alpha;
	}

	protected ByteArrayOutputStream trainer( String corpus ) throws IOException {
		String[ ] files = corpus.split( System.lineSeparator() );
		String[ ][ ] dataSet = new String[ files.length ][ ];

		int fileId = 0;
		for ( String file : files ) {
			dataSet[ fileId++ ] = file.trim().split( " " );
		}

		String[ ][ ] distinct = getDistinct( dataSet );
		String[ ] outcomes = distinct[ 0 ];

		Map< String, Map< Integer, Map< String, Double > > > context = getContext( dataSet, distinct, outcomes );
		Map< String, Map< Integer, Map< String, Double > > > coeficients = getCoeficients( fileId, distinct, outcomes, context );

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream( byteOut );
		out.writeObject( coeficients );
		return byteOut;
	}

	private String[ ][ ] getDistinct( String[ ][ ] dataSet ) {
		String[ ][ ] distinct = new String[ dataSet[ 0 ].length ][ ];
		for ( int i = 0; i < dataSet[ 0 ].length; i++ ) {
			final int index = i;
			distinct[ i ] = IntStream.range( 0, dataSet.length )
					.mapToObj( j -> dataSet[ j ][ index ] )
					.distinct().toArray( String[ ]::new );
		}
		return distinct;
	}

	private Map< String, Map< Integer, Map< String, Double > > > getContext( String[ ][ ] dataSet, String[ ][ ] distinct, String[ ] outcomes ) {
		Map< String, Map< Integer, Map< String, Double > > > context = Arrays.stream( outcomes )
				.map( outcome -> pairOutcome( outcome, distinct, dataSet ) )
				.collect( Collectors.toMap( Pair::getKey, Pair::getValue ) );
		return context;
	}

	private Pair< String, Map< Integer, Map< String, Double > > > pairOutcome( String outcome, String[ ][ ] distinct, String[ ][ ] dataSet ) {
		Map< Integer, Map< String, Double > > indexer = IntStream.range( 0, distinct.length )
				.mapToObj( index -> pairCounter(index, distinct, outcome, dataSet) )
				.collect( Collectors.toMap( Pair::getKey, Pair::getValue ) );
		return Pair.of( outcome, indexer );
	}

	private Pair<Integer, Map< String, Double >> pairCounter( int index, String[ ][ ] distinct, String outcome, String[ ][ ] dataSet ) {
		Map< String, Double > count = IntStream.range( 0, distinct[ index ].length )
				.mapToObj( labeling -> counter(labeling, index, distinct, outcome, dataSet) )
				.collect( Collectors.toMap( Pair::getKey, Pair::getValue ) );
		return Pair.of( index, count );
	}
	
	private Pair<String, Double> counter(Integer labeling, int index, String[ ][ ] distinct, String outcome, String[ ][ ] dataSet){
		String label = distinct[ index ][ labeling ];
		return Pair.of( 
				label, 
				counter( index, label, outcome, dataSet ) 
		);
	}

	private double counter( int index, String label, String outcome, String[ ][ ] dataSet ) {
		return Arrays.stream( dataSet )
				.filter( 
						data -> 
							data[ 0 ].equals( outcome ) && label.equals( data[ index ] )
				)
		.count();
	}

	protected abstract Map< String, Map< Integer, Map< String, Double > > > getCoeficients( 
			int size, 
			String[ ][ ] distinct, 
			String[ ] outcomes, 
			Map< String, Map< Integer, Map< String, Double > > > context 
	);
	
	protected Map< String, Double > getResult( Map< String, Map< Integer, Map< String, Double > > > coeficients, String[ ] context ) throws ParseException {
		Map< String, Double > result = new HashMap< >();

		for ( Entry< String, Map< Integer, Map< String, Double > > > outcome : coeficients.entrySet() ) {
			String labelIndex = outcome.getKey();
			double coeficient = coeficients.get( labelIndex ).get( 0 ).get( labelIndex );

			for ( int index = 0; index < context.length; index++ ) {
				String label = context[ index ];

				Map< Integer, Map< String, Double > > cache = coeficients.get( labelIndex );
				if ( cache != null ) {
					Map< String, Double > predition = cache.get( index + 1 );
					if ( predition != null ) {
						Double value = predition.get( label );
						value = value == null ? 0 : value;
						coeficient = coeficient * value;
					}
				}
			}
			result.put( labelIndex, coeficient );
		}

		double sum = result.entrySet().stream().mapToDouble( e -> e.getValue() ).sum();
		if ( sum > 0 ) {
			for ( Entry< String, Double > entry : result.entrySet() ) {
				double coef = ( entry.getValue() / sum ) /** 100 */
				;
				result.put( entry.getKey(), coef );
			}
		}

		return result;
	}
	
	protected Map< Integer, Map< String, Double > > getResultSequential( ByteArrayOutputStream out, String[ ] sequential ) throws ParseException, IOException, ClassNotFoundException {
		Map< Integer, Map< String, Double > > result = new HashMap< >();
		ByteArrayInputStream byteIn = new ByteArrayInputStream( out.toByteArray() );
		ObjectInputStream in = new ObjectInputStream( byteIn );
		@SuppressWarnings( "unchecked" )
		Map< String, Map< Integer, Map< String, Double > > > coeficients = (Map< String, Map< Integer, Map< String, Double > > >) in.readObject();

		int line = 0;
		for ( String context : sequential ) {
			context = context.replaceAll( "\\?", "" ).replaceAll( "  +", " " ).trim();
			Map< String, Double > predictor = getResult( coeficients, context.split( " " ) );
			result.put( line++, predictor );
		}

		return result;
	}

}