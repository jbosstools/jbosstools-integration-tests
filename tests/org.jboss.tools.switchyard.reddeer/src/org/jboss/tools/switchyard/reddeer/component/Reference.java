package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.widget.ContextButton;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;

/**
 * Reference component.
 * 
 * @author apodhrad
 * 
 */
public class Reference extends Component {

	public Reference(String tooltip) {
		super(tooltip, 0);
	}

	public Reference(String tooltip, int index) {
		super(tooltip, index);
	}

	public PromoteServiceWizard promoteReference() {
		click();
		new ContextButton("Promote Reference").click();
		return new PromoteServiceWizard("New Component Reference");
	}
}
