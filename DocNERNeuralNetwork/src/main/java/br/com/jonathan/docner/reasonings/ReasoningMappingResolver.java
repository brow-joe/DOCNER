package br.com.jonathan.docner.reasonings;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.jonathan.docner.reasonings.analytic.MultilayerPerceptron;
import br.com.jonathan.docner.reasonings.analytic.RegressaoLogistica;
import br.com.jonathan.docner.reasonings.logic.NaiveBayes;
import br.com.jonathan.docner.reasonings.logic.Poisson;
import br.com.jonathan.docner.vo.RNDataInVO;

public class ReasoningMappingResolver{
	protected final Logger LOGGER = LogManager.getLogger( ReasoningMappingResolver.class );

	public RNDataInVO resolve( Integer sequence, EReasoningLogic logic, String dataSet, String[ ] sequential ) throws ReasoningException {
		RNDataInVO dataIn;
		switch ( logic ) {
			case POISSON:
				dataIn = new Poisson().trainer( sequence, dataSet, sequential );
				break;
			case NAIVE_BAYES:
				dataIn = new NaiveBayes().trainer( sequence, dataSet, sequential );
				break;
			default:
				dataIn = null;
				break;
		}
		return dataIn;
	}

	public RNDataInVO resolve( Integer sequence, EReasoningAnalytic analytic, String dataSet, String[ ] sequential ) throws ReasoningException {
		RNDataInVO dataIn;
		switch ( analytic ) {
			case REGRESSAO_LOGISTICA_MULTINOMIAL:
				dataIn = new RegressaoLogistica( 20, 0 ).trainer( sequence, dataSet, sequential );
				break;
			case MULTILAYER_PERCEPTRON:
				dataIn = new MultilayerPerceptron( 20, 0 ).trainer( sequence, dataSet, sequential );
				break;
			default:
				dataIn = null;
				break;
		}
		return dataIn;
	}

}