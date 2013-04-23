package org.eclipse.bpmn2.ui.editor.constructs.gateways;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.util.Bot;


/**
 * ISSUES: 
 * 	1) Description is mixed with the pictogram.
 *  2) When adding a new condition 
 *  	+ select flow
 *      + click pencil
 *      + click add condition
 *      - condition expression is not visible
 *  
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class EventBasedGateway extends AbstractGateway {
	
	public enum Type {
		EXCLUSIVE, PARALLEL
	}
	
	/**
	 * 
	 * @param name
	 */
	public EventBasedGateway(String name) {
		super(name, ConstructType.EVENT_BASED_GATEWAY);
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		String visibleText = type.name().charAt(0) + 
				type.name().substring(1).toLowerCase();
		
		properties.selectTab("Gateway");
		new DefaultCombo("Event Gateway Type").setSelection(visibleText);
	}

	/**
	 * 
	 */
	public void setInstantiate(boolean b) {
		properties.selectTab("Gateway");
		properties.selectCheckBox(Bot.get().checkBox(), b);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway#setDirection(org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway#setCondition(java.lang.String, java.lang.String)
	 */
	@Override
	public void setCondition(String branch, String condition) {
		super.setCondition(branch, condition);
	}
	
}
