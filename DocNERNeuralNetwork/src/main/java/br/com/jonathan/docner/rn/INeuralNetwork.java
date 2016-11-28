package br.com.jonathan.docner.rn;

import java.util.List;

import br.com.jonathan.docner.reasonings.EReasoningLogic;
import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.vo.RNDataOutVO;
import br.com.jonathan.docner.vo.ResultSetVO;

public interface INeuralNetwork{

	public RNDataOutVO trainer( String dataSet, List< EReasoningAnalytic > logics, List< EReasoningLogic > analytics ) throws NeuralException;

	public List< ResultSetVO > classifier( String classifierSet, RNDataOutVO model ) throws NeuralException;

}