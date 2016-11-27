package br.com.jonathan.docner.reasonings.logic;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;

import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.reasonings.ReasoningException;
import br.com.jonathan.docner.vo.RNDataInVO;

public class Poisson extends ADistribution{

	public Poisson(){
		super( LogManager.getLogger( Poisson.class ) );
	}

	@Override
	public RNDataInVO trainer( Integer sequence, String dataSet, String[ ] sequential ) throws ReasoningException {
		try {
			ByteArrayOutputStream out = trainer( dataSet );
			return new RNDataInVO( 
					sequence,
					EReasoningLogic.POISSON, 
					out, 
					getResultSequential(out, sequential)
			);
		} catch ( Exception e ) {
			LOGGER.error( e );
			throw new ReasoningException( e );
		}
	}

	@Override
	protected Map< String, Map< Integer, Map< String, Double > > > getCoeficients( int size, String[ ][ ] distinct, String[ ] outcomes, Map< String, Map< Integer, Map< String, Double > > > context ) {
		Map< String, Map< Integer, Map< String, Double > > > coeficients = new HashMap< >();

		for ( String outcome : outcomes ) {
			Map< Integer, Map< String, Double > > indexer = new HashMap< >();
			double outComeCoef = context.get( outcome ).get( 0 ).get( outcome ) + ALPHA;

			Map< String, Double > cache = new HashMap< >();
			String label = outcome;
			double fatorial = fatorial( (int) outComeCoef );
			double coef = Math.pow( size + ALPHA, outComeCoef );
			coef = coef * Math.pow( Math.E, ( size + ALPHA ) * -1 );
			coef = coef / (int) fatorial;

			cache.put( label, coef );
			indexer.put( 0, cache );

			for ( int index = 1; index < distinct.length; index++ ) {
				cache = new HashMap< >();
				for ( int j = 0; j < distinct[ index ].length; j++ ) {
					label = distinct[ index ][ j ];
					coef = context.get( outcome ).get( index ).get( label ) + ALPHA;
					fatorial = fatorial( (int) coef );
					coef = Math.pow( outComeCoef, coef );
					coef = coef * Math.pow( Math.E, outComeCoef * -1 );
					coef = coef / (int) fatorial;

					cache.put( label, coef );
				}

				indexer.put( index, cache );
			}

			coeficients.put( outcome, indexer );
		}

		return coeficients;
	}
	
	private static Double fatorial( int n ) {
		return n <= 1 ? 1 : n * fatorial( n - 1 );
	}

}