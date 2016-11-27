package br.com.jonathan.docner.reasonings.logic;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;

import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.reasonings.ReasoningException;
import br.com.jonathan.docner.vo.RNDataInVO;

public class NaiveBayes extends ADistribution{

	public NaiveBayes(){
		super( LogManager.getLogger( NaiveBayes.class ) );
	}

	@Override
	public RNDataInVO trainer( Integer sequence, String dataSet, String[ ] sequential ) throws ReasoningException {
		try {
			ByteArrayOutputStream out = trainer( dataSet );
			return new RNDataInVO( 
					sequence,
					EReasoningLogic.NAIVE_BAYES, 
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
			double outComeCoef = Math.log( context.get( outcome ).get( 0 ).get( outcome ) + ALPHA );

			Map< String, Double > cache = new HashMap< >();
			String label = outcome;
			double coeficient = Math.exp( outComeCoef - Math.log( size + ALPHA ) );
			cache.put( label, coeficient );
			indexer.put( 0, cache );

			for ( int i = 1; i < distinct.length; i++ ) {
				cache = new HashMap< >();
				for ( int j = 0; j < distinct[ i ].length; j++ ) {
					label = distinct[ i ][ j ];
					coeficient = Math.log( context.get( outcome ).get( i ).get( label ) + ALPHA );
					cache.put( label, Math.exp( coeficient - outComeCoef ) );
				}

				indexer.put( i, cache );
			}

			coeficients.put( outcome, indexer );
		}

		return coeficients;
	}

}