package org.jboss.tools.bpel.ui.bot.ext.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class FaultHandler extends Activity {

	public FaultHandler() {
		this(null);
	}

	public FaultHandler(Activity parent) {
		super(null, "FaultHandler", parent, 0);
	}

	public void addCatch() {
		menu("Add Catch");
	}

	public void addCatchAll() {
		menu("Add Catch All");
	}
}
