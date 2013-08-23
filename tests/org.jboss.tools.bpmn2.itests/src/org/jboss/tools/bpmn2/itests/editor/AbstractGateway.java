package org.jboss.tools.bpmn2.itests.editor;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;


/**
 * ISSUES:
 * 	1) Cannot select default branch when conditions are already in place.
 * 	
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractGateway extends Construct {

	public enum Direction {
		CONVERGING, DIVERGING, MIXED
	}
	
	/**
	 * 
	 * @param name
	 */
	public AbstractGateway(String name, ConstructType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param direction
	 */
	protected void setDirection(Direction direction) {
		select();
		String visibleText = direction.name().charAt(0) + 
				direction.name().substring(1).toLowerCase();
		
		properties.selectTab("Gateway");
		new DefaultCombo("Gateway Direction").setSelection(visibleText);
	}
	
	/**
	 * 
	 * @param condition
	 */
	protected void setCondition(String branch, String lang, String condition) {
		select();
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch);
		properties.toolbarButton("Sequence Flow List", "Edit").click();
		new PushButton("Add Condition").click();
		new DefaultCombo("Script Language").setSelection(lang);
		new LabeledText("Constraint").setText(condition);
		properties.toolbarButton("Sequence Flow Details", "Close").click();
	}
	
	/**
	 * 
	 * @param branch
	 */
	protected void setDefaultBranch(String branch) {
		select();
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch);
		Bot.get().toolbarButtonWithTooltip("Edit").click();
		Bot.get().checkBox().click();
		Bot.get().toolbarButtonWithTooltip("Close").click();
	}
	
}
