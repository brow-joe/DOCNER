package br.com.jonathan.docner.reasonings;

public enum EReasoningAnalytic {

	REGRESSAO_LOGISTICA_MULTINOMIAL("maxent"), MULTILAYER_PERCEPTRON("mlp");

	private String type;

	EReasoningAnalytic( String type ){
		this.type = type;
	}

	public String getType() {
		return type;
	}

}