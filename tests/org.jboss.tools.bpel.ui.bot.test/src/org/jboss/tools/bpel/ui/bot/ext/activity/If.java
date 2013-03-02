package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class If extends ContainerActivity {

	public If(String name) {
		super(name, IF);
	}

	public If setCondition(String condition) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setCondition(condition);
		return this;
	}

	public ElseIf addElseIf() {
		menu("Add ElseIf");
		return new ElseIf(this);
	}

	public Else addElse() {
		menu("Add Else");
		return new Else(this);
	}
}
