package org.jboss.tools.bpel.ui.bot.ext.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class Sequence extends ContainerActivity {

	public Sequence(String name) {
		this(name, null);
	}

	public Sequence(String name, Activity parent) {
		super(name, SEQUENCE, parent, 0);
	}

}
