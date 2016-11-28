package br.com.jonathan.docner.reasonings;

public enum EReasoningLogic {

	REGRESSAO_LOGISTICA_MULTINOMIAL("maxent"), MULTILAYER_PERCEPTRON("mlp");

	private String type;

	EReasoningLogic( String type ){
		this.type = type;
	}

	public String getType() {
		return type;
	}

}