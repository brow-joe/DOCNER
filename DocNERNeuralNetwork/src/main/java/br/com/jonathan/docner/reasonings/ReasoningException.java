package br.com.jonathan.docner.reasonings;

public class ReasoningException extends Exception{
	private static final long serialVersionUID = 1L;

	public ReasoningException( String msg ){
		super( msg );
	}

	public ReasoningException( Throwable throwable ){
		super( throwable );
	}

	public ReasoningException( String msg, Throwable throwable ){
		super( msg, throwable );
	}
}