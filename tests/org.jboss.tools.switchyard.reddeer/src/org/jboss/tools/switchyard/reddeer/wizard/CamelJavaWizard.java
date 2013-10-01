package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelJavaWizard extends ServiceWizard<CamelJavaWizard> {

	public static final String DIALOG_TITLE = "New Java Class";
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 

	public CamelJavaWizard() {
		super();
	}

	public CamelJavaWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
		return this;
	}

	public CamelJavaWizard setName(String name) {
		new LabeledText("Name:").setText(name);
		return this;
	}

	public CamelJavaWizard open() {
		new SwitchYardEditor().addComponent("Camel (Java)");
		return this;
	}

	@Override
	protected void browse() {
		new PushButton(2).click();
	}

	@Override
	public void finish() {
		activate();
		super.finish();
	}

}
