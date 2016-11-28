package br.com.jonathan.docner.rn;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.io.Resources;

import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.vo.RNDataOutVO;
import br.com.jonathan.docner.vo.ResultSetVO;
import junit.framework.TestCase;

public class TrainerTest extends TestCase{
	protected final Logger LOGGER = LogManager.getLogger( TrainerTest.class );
	
	private final Charset ENCODE = Charset.forName( "UTF-8" );
	private final String TRAINER_FILE = "PlayTennisAllTrainer.txt"; 
	private final String TEST_FILE = "PlayTennisAllClassifier.txt";//*/
	
	/*private final String TRAINER_FILE = "PlayTennis70Trainer.txt"; 
	private final String TEST_FILE = "PlayTennis30Classifier.txt";//*/

	public void test() throws Exception {
		try {
			RNDataOutVO model = trainer();
			
			assertNotNull( model );
			
			classifier( model );
		} catch ( Exception e ) {
			LOGGER.fatal( e );
			throw e;
		}
	}

	private void classifier( RNDataOutVO model ) throws IOException, NeuralException {
		INeuralNetwork network = new NeuralNetwork();
		
		String classifierSet = Resources.toString( 
				Resources.getResource( TEST_FILE ), 
				ENCODE 
		);
		
		assertNotNull( classifierSet );
		
		List< ResultSetVO > resultSet = network.classifier(classifierSet, model);
		assertNotNull( resultSet );
		
		for ( ResultSetVO result : resultSet ) {
			String outcome = result.getOutcome();
			if(StringUtils.equals( outcome, "?" )){
				outcome = "*** " + result.getProbs()
						.entrySet().stream()
						.max( 
								(l,r) -> Double.compare(
										l.getValue(), r.getValue() 
								) 
						).get().getKey();
			}
			System.out.println( result.getIndex() + " - " + outcome + " : " + result.getOutcome());
			for ( Entry< String, Double > out : result.getProbs().entrySet() ) {
				System.out.println( "\t" + out.getKey() + " : " + out.getValue() );
			}
		}
	}

	private RNDataOutVO trainer() throws IOException, NeuralException {
		INeuralNetwork network = new NeuralNetwork();
		
		String trainerSet = Resources.toString( 
				Resources.getResource( TRAINER_FILE ), 
				ENCODE 
		);
		
		assertNotNull( trainerSet );
		
		List< EReasoningAnalytic > analytics = Arrays.asList(
				EReasoningAnalytic.POISSON, 
				EReasoningAnalytic.NAIVE_BAYES 
		);
		List< EReasoningLogic > logics = Arrays.asList(
				EReasoningLogic.REGRESSAO_LOGISTICA_MULTINOMIAL, 
				EReasoningLogic.MULTILAYER_PERCEPTRON
		);
		
		RNDataOutVO vo = network.trainer( trainerSet, analytics, logics );

		return vo;
	}

}