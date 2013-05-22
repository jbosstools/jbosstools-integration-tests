package org.jboss.tools.switchyard.reddeer.editor;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.component.MainComponent;

/**
 * SwitchYeard editor
 * 
 * @author apodhrad
 *
 */
public class SwitchYardEditor extends SWTBotGefEditor {

	private MainComponent mainComponent;

	public SwitchYardEditor() {
		super(Bot.get().editorByTitle("switchyard.xml").getReference(), Bot.get());
		mainComponent = new MainComponent(mainEditPart().children().get(0));
	}

	public void addComponent(String component) {
		activateTool(component);
		clickMainComponent();
	}

	private void clickMainComponent() {
		mainComponent.click();
	}

	public MainComponent getMainComponent() {
		return mainComponent;
	}
	

}
