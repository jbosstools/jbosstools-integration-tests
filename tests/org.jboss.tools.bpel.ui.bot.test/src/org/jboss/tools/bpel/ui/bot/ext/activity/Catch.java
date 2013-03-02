package org.jboss.tools.bpel.ui.bot.ext.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class Catch extends ContainerActivity {

	public Catch(Activity parent) {
		super(null, "Catch", parent, 0);
	}

	public Catch(String name, Activity parent) {
		super(name, "Catch", parent, 0);
	}

}
