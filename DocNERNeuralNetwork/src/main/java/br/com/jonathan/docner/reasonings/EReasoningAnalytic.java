package br.com.jonathan.docner.reasonings;

public enum EReasoningAnalytic {

	POISSON("poisson"), NAIVE_BAYES("nb");

	private String type;

	EReasoningAnalytic( String type ){
		this.type = type;
	}

	public String getType() {
		return type;
	}

}