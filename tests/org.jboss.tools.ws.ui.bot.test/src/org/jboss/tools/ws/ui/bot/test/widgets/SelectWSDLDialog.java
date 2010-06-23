package org.jboss.tools.ws.ui.bot.test.widgets;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;

public class SelectWSDLDialog extends SWTBotShell {

	public SelectWSDLDialog(Shell shell) throws WidgetNotFoundException {
		super(shell);
		assert JBossWSUIMessages.WSDLBrowseDialog_Dialog_Title.equals(getText());
	}

	public void openURL() {
		bot().button(JBossWSUIMessages.WSDLBrowseDialog_URL_Browse).click();
	}

	// public void setURI(String s) {
	// bot().comboBoxWithLabel(JBossWSUIMessages.WSDLBrowseDialog_WSDL_URI_Field).setText(s);
	// }

	public String getURI() {
		return bot().comboBoxWithLabel(JBossWSUIMessages.WSDLBrowseDialog_WSDL_URI_Field).getText();
	}

	public List<String> getServices() {
		return getItems(JBossWSUIMessages.WSDLBrowseDialog_Service_Field);
	}

	public List<String> getPorts() {
		return getItems(JBossWSUIMessages.WSDLBrowseDialog_Port_Field);
	}

	public List<String> getOperations() {
		return Arrays.asList(getOperationsList().getItems());
	}

	public void selectOperation(String op) {
		getOperationsList().select(op);
	}

	public void ok() {
		bot().button(IDialogConstants.OK_LABEL).click();
	}

	private List<String> getItems(String label) {
		String[] items = bot().comboBoxWithLabel(label).items();
		return Arrays.asList(items);
	}

	private SWTBotList getOperationsList() {
		return bot().listWithLabel(JBossWSUIMessages.WSDLBrowseDialog_Operation_Field);
	}
}