package br.com.jonathan.docner.rn;

import java.util.List;

import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.vo.RNVO;

public interface INeuralNetwork{

	public RNVO trainer( String dataSet, List< EReasoningLogic > logics, List< EReasoningAnalytic > analytics ) throws NeuralException;

}