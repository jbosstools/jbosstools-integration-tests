package org.jboss.tools.switchyard.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.tools.switchyard.reddeer.component.MainComponent;

/**
 * SwitchYeard editor
 * 
 * @author apodhrad
 *
 */
public class SwitchYardEditor extends SWTBotGefEditor {

	private MainComponent mainComponent;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 
	
	public SwitchYardEditor() {
		super(bot.editorByTitle("switchyard.xml").getReference(), bot);
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
	
	public void addComponent(String component, Integer[] coords) {
		activateTool(component);
		mainComponent.click(coords[0], coords[1]);
	}

}
