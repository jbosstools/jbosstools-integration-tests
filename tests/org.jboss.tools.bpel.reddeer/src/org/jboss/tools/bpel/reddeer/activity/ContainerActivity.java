package org.jboss.tools.bpel.reddeer.activity;

public class ContainerActivity extends Activity {

	public ContainerActivity(String name, String type) {
		super(name, type);
	}

	public ContainerActivity(String name, String type, Activity parent, int index) {
		super(name, type, parent, index);
	}

	public Assign addAssign(String name) {
		add(ASSIGN, name);
		return new Assign(name);
	}

	public void addCompensate(String name) {
		add(COMPENSATE, name);
	}

	public CompensateScope addCompensateScope(String name) {
		add(COMPENSATE_SCOPE, name);
		return new CompensateScope(name);
	}

	public Empty addEmpty(String name) {
		add(EMPTY, name);
		return new Empty(name);
	}

	public Exit addExit(String name) {
		add(EXIT, name);
		return new Exit(name);
	}

	public Flow addFlow(String name) {
		add(FLOW, name);
		return new Flow(name);
	}

	public ForEach addForEach(String name) {
		add(FOR_EACH, name);
		return new ForEach(name);
	}

	public If addIf(String name) {
		add(IF, name);
		return new If(name);
	}

	public Invoke addInvoke(String name) {
		add(INVOKE, name);
		return new Invoke(name);
	}

	public Pick addPick(String name) {
		add(PICK, name);
		return new Pick(name);
	}

	public Receive addReceive(String name) {
		add(RECEIVE, name);
		return new Receive(name);
	}

	public RepeatUntil addRepeatUntil(String name) {
		add(REPEAT_UNTIL, name);
		return new RepeatUntil(name);
	}

	public Reply addReply(String name) {
		add(REPLY, name);
		return new Reply(name);
	}

	public void addRethrow(String name) {
		add(RETHROW, name);
	}

	public Scope addScope(String name) {
		add(SCOPE, name);
		return new Scope(name);
	}

	public Sequence addSequence(String name) {
		add(SEQUENCE, name);
		return new Sequence(name);
	}

	public Throw addThrow(String name) {
		add(THROW, name);
		return new Throw(name);
	}

	public Validate addValidate(String name) {
		add(VALIDATE, name);
		return new Validate(name);
	}

	public Wait addWait(String name) {
		add(WAIT, name);
		return new Wait(name);
	}

	public While addWhile(String name) {
		add(WHILE, name);
		return new While(name);
	}

	public Sequence getSequence() {
		return getSequence(null);
	}

	public Sequence getSequence(String name) {
		return new Sequence(name, this);
	}

	public Scope getScope() {
		return getScope(null);
	}

	public Scope getScope(String name) {
		return new Scope(name, this);
	}
}
