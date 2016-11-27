package br.com.jonathan.docner.rn;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.io.Resources;

import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.vo.RNVO;
import junit.framework.TestCase;

public class TrainerTest extends TestCase{
	protected final Logger LOGGER = LogManager.getLogger( TrainerTest.class );
	
	private final Charset ENCODE = Charset.forName( "UTF-8" );
	private final String FILE = "PlayTennisAllTrainer.txt"; 

	public void test() throws Exception {
		try {
			String trainerSet = Resources.toString( 
					Resources.getResource( FILE ), 
					ENCODE 
			);
			
			assertNotNull( trainerSet );
			
			List< EReasoningLogic > logics = Arrays.asList(
					EReasoningLogic.POISSON, 
					EReasoningLogic.NAIVE_BAYES 
			);
			List< EReasoningAnalytic > analytics = Arrays.asList(
					EReasoningAnalytic.REGRESSAO_LOGISTICA_MULTINOMIAL, 
					EReasoningAnalytic.MULTILAYER_PERCEPTRON
			);
			INeuralNetwork network = new NeuralNetwork();
			
			RNVO vo = network.trainer( trainerSet, logics, analytics );
			
			assertNotNull( vo );
		} catch ( Exception e ) {
			LOGGER.fatal( e );
			throw e;
		}
	}

}