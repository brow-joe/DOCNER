package br.com.jonathan.docner.rn;

public class RNException extends Exception{
	private static final long serialVersionUID = 1L;

	public RNException( String msg ){
		super( msg );
	}

	public RNException( Throwable throwable ){
		super( throwable );
	}

	public RNException( String msg, Throwable throwable ){
		super( msg, throwable );
	}

}
