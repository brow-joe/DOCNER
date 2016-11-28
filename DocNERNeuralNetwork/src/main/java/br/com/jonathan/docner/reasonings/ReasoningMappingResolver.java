package br.com.jonathan.docner.reasonings;

import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.jonathan.docner.reasonings.analytic.MultilayerPerceptron;
import br.com.jonathan.docner.reasonings.analytic.RegressaoLogistica;
import br.com.jonathan.docner.reasonings.logic.NaiveBayes;
import br.com.jonathan.docner.reasonings.logic.Poisson;
import br.com.jonathan.docner.vo.RNDataInVO;

public class ReasoningMappingResolver{
	protected final Logger LOGGER = LogManager.getLogger( ReasoningMappingResolver.class );

	public RNDataInVO trainerResolve( Integer sequence, EReasoningLogic logic, String dataSet, String[ ] sequential ) throws ReasoningException {
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

	public RNDataInVO trainerResolve( Integer sequence, EReasoningAnalytic analytic, String dataSet, String[ ] sequential ) throws ReasoningException {
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

	public void classifierResolver( RNDataInVO in, String[ ] classifier ) throws ReasoningException {
		if ( Objects.nonNull( in.getAnalytic() ) ) {
			switch ( in.getAnalytic() ) {
				case REGRESSAO_LOGISTICA_MULTINOMIAL:
					new RegressaoLogistica().classifier( in, classifier );
					break;
				case MULTILAYER_PERCEPTRON:
					new MultilayerPerceptron().classifier( in, classifier );
					break;
				default:
					break;
			}
		} else if ( Objects.nonNull( in.getLogic() ) ) {
			switch ( in.getLogic() ) {
				case POISSON:
					new Poisson().classifier( in, classifier );
					break;
				case NAIVE_BAYES:
					new NaiveBayes().classifier( in, classifier );
					break;
				default:
					break;
			}
		}
	}

}