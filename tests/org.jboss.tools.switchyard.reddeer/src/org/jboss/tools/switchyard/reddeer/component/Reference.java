package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.widget.ContextButton;

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

	public void promoteReference() {
		click();
		new ContextButton("Promote Reference").click();
	}
}
