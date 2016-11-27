package br.com.jonathan.docner.reasonings;

public enum EReasoningLogic {

	POISSON("poisson"), NAIVE_BAYES("nb");

	private String type;

	EReasoningLogic( String type ){
		this.type = type;
	}

	public String getType() {
		return type;
	}

}