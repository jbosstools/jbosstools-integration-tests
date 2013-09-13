package org.jboss.tools.switchyard.reddeer.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * REST binding page
 * 
 * @author apodhrad
 * 
 */
public class RESTBindingPage extends WizardPage {

	public static final String CONTEXT_PATH = "Context Path";
	public static final String Address = "Address";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public RESTBindingPage setContextPath(String contextPath) {
		new LabeledText(CONTEXT_PATH).setFocus();
		new LabeledText(CONTEXT_PATH).setText(contextPath);
		return this;
	}

	public String getContextPath() {
		return new LabeledText(CONTEXT_PATH).getText();
	}

	public RESTBindingPage setAddress(String address) {
		// new LabeledText("Address").setText(address);
		bot.textWithLabel("Address").typeText(address);
		return this;
	}

	public RESTBindingPage addInterface(String javaInterface) {
		new PushButton("Add").click();
		new DefaultText().setText(javaInterface);
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new PushButton("OK").click();
		return this;
	}

	public List<String> getInterfaces() {
		String[] items = bot.listWithLabel("RESTful Interfaces").getItems();
		return new ArrayList<String>(Arrays.asList(items));
	}

}
