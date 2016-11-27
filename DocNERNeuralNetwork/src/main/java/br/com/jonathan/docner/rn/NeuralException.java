package br.com.jonathan.docner.rn;

public class NeuralException extends Exception{
	private static final long serialVersionUID = 1L;

	public NeuralException( String msg ){
		super( msg );
	}

	public NeuralException( Throwable throwable ){
		super( throwable );
	}

	public NeuralException( String msg, Throwable throwable ){
		super( msg, throwable );
	}
}