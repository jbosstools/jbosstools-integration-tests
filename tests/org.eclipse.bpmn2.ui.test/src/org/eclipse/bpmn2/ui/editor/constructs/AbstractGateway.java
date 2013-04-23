package org.eclipse.bpmn2.ui.editor.constructs;

import org.eclipse.bpmn2.ui.editor.ConstructType;
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
		String visibleText = direction.name().charAt(0) + 
				direction.name().substring(1).toLowerCase();
		
		properties.selectTab("Gateway");
		new DefaultCombo("Gateway Direction").setSelection(visibleText);
	}
	
	/**
	 * 
	 * @param condition
	 */
	protected void setCondition(String branch, String condition) {
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch, 0);
		Bot.get().toolbarButtonWithTooltip("Edit").click();
		new PushButton("Add Condition").click();
		new DefaultCombo("Script Language").setSelection("XPath");
		new LabeledText("Constraint").setText(condition);
		Bot.get().toolbarButtonWithTooltip("Close").click();
	}
	
	/**
	 * 
	 * @param branch
	 */
	protected void setDefaultBranch(String branch) {
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch, 0);
		Bot.get().toolbarButtonWithTooltip("Edit").click();
		Bot.get().checkBox().click();
		Bot.get().toolbarButtonWithTooltip("Close").click();
	}
	
}
