package org.jboss.tools.switchyard.reddeer.component;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;

/**
 * 
 * @author apodhrad
 *
 */
public class MainComponent extends Component {

	public MainComponent(String tooltip) {
		super(tooltip);
	}
	
	public MainComponent(SWTBotGefEditPart editPart) {
		this.editPart = editPart;
	}
}
