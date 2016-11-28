package br.com.jonathan.docner.vo;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultSetVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer index;
	private String outcome;
	private Map< String, Double > probs;

	public Integer getIndex() {
		return index;
	}

	public void setIndex( Integer index ) {
		this.index = index;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome( String outcome ) {
		this.outcome = outcome;
	}

	public Map< String, Double > getProbs() {
		return probs;
	}

	public void setProbs( Map< String, Double > probs ) {
		this.probs = probs;
	}

}