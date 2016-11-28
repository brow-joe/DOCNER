package br.com.jonathan.docner.reasonings;

import br.com.jonathan.docner.vo.RNDataInVO;

public interface IReasoning{

	public RNDataInVO trainer( Integer sequence, String dataSet, String[ ] sequential ) throws ReasoningException;

	public void classifier( RNDataInVO in, String[ ] classifier ) throws ReasoningException;
}