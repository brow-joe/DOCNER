package br.com.jonathan.docner.vo;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RNDataOutVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private List< RNDataInVO > models;
	private List< String > outcomes;
	private ByteArrayOutputStream model;

	public RNDataOutVO( List< RNDataInVO > models ){
		super();
		this.models = models;
	}

	public List< RNDataInVO > getModels() {
		return models;
	}

	public void setModels( List< RNDataInVO > models ) {
		this.models = models;
	}

	public List< String > getOutcomes() {
		return outcomes;
	}

	public void setOutcomes( List< String > outcomes ) {
		this.outcomes = outcomes;
	}

	public ByteArrayOutputStream getModel() {
		return model;
	}

	public void setModel( ByteArrayOutputStream model ) {
		this.model = model;
	}

}