package br.com.jonathan.docner.vo;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.jonathan.docner.reasonings.EReasoningAnalytic;
import br.com.jonathan.docner.reasonings.EReasoningLogic;

@XmlRootElement
public class RNDataInVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer sequence;
	private EReasoningAnalytic analytic;
	private EReasoningLogic logic;
	private ByteArrayOutputStream model;
	private Map< Integer, Map< String, Double > > resultSequential;

	public RNDataInVO( Integer sequence, EReasoningAnalytic analytic, ByteArrayOutputStream model, Map< Integer, Map< String, Double > > resultSequential ){
		this( sequence, null, analytic, model, resultSequential );
	}

	public RNDataInVO( Integer sequence, EReasoningLogic logic, ByteArrayOutputStream model, Map< Integer, Map< String, Double > > resultSequential ){
		this( sequence, logic, null, model, resultSequential );
	}

	public RNDataInVO( Integer sequence, EReasoningLogic logic, EReasoningAnalytic analytic, ByteArrayOutputStream model, Map< Integer, Map< String, Double > > resultSequential ){
		this.sequence = sequence;
		this.logic = logic;
		this.analytic = analytic;
		this.model = model;
		this.resultSequential = resultSequential;
	}

	public EReasoningAnalytic getAnalytic() {
		return analytic;
	}

	public void setAnalytic( EReasoningAnalytic analytic ) {
		this.analytic = analytic;
	}

	public EReasoningLogic getLogic() {
		return logic;
	}

	public void setLogic( EReasoningLogic logic ) {
		this.logic = logic;
	}

	public ByteArrayOutputStream getModel() {
		return model;
	}

	public void setModel( ByteArrayOutputStream model ) {
		this.model = model;
	}

	public Map< Integer, Map< String, Double > > getResultSequential() {
		return resultSequential;
	}

	public void setResultSequential( Map< Integer, Map< String, Double > > resultSequential ) {
		this.resultSequential = resultSequential;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence( Integer sequence ) {
		this.sequence = sequence;
	}

}