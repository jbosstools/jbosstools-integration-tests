package org.jboss.tools.ws.ui.bot.test.widgets;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.view.ViewBase;
import org.jboss.tools.ws.ui.bot.test.WsTesterTest;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class WsTesterView extends ViewBase {

	private static final Bundle WSUI_BUNDLE = Platform.getBundle("org.jboss.tools.ws.ui");

	public enum Ws_Type {

		JAX_WS, JAX_RS;

		String getLabel() {
			switch (this) {
			case JAX_WS:
				return "JAX-WS";
			case JAX_RS:
				return "JAX-RS";
			}
			throw new AssertionError("Invalid Web Service Type");
		}
	}

	public enum Http_Method {

		GET, POST, PUT, DELETE;
	}

	public enum Request_Arg_Type {

		HEADER(JBossWSUIMessages.JAXRSWSTestView_Request_Header_Label),
		PARAMETER(JBossWSUIMessages.JAXRSWSTestView_Request_Parameters_Label);

		private String type;

		private Request_Arg_Type(String s) {
			type = s;
		}

		private String getType() {
			return type;
		}
	}

	public WsTesterView() {
		super();
		viewObject = new IView() {

			public String getName() {
				// Web Service Tester
				return getStringFromBundle("%test.view.name");
			}

			public List<String> getGroupPath() {
				// JBoss Tools Web Services
				return Collections.singletonList(getStringFromBundle("%test.view.category.name"));
			}
		};
	}

	@Override
	public SWTBotView show() {
		SWTBotView b = super.show();
		// maximize tester view
		menu(IDEWorkbenchMessages.Workbench_window).menu(
				WorkbenchMessages.MaximizePartAction_text).click();
		return b;
	}

	public void setWebServiceType(WsTesterView.Ws_Type type) {
		// Web Service Type:
		comboBoxWithLabel(JBossWSUIMessages.JAXRSWSTestView_Web_Service_Type_Label)
				.setSelection(type.getLabel());
	}

	// public void setSampleContent() {
	// button(JBossWSUIMessages.JAXRSWSTestView_Set_Sample_Data_Label).click();
	// }

	public void setHttpMethod(WsTesterView.Http_Method m) {
		getHTTPTypeCombo().setSelection(m.toString());
	}

	public WsTesterView.Http_Method getHttpMethod() {
		return Enum.valueOf(WsTesterView.Http_Method.class, getHTTPTypeCombo().getText());
	}

	public boolean isHttpMethodSelectionEnabled() {
		return getHTTPTypeCombo().isEnabled();
	}

	public void setServiceURL(String url) {
		getServiceURLCombo().typeText(url);
	}

	public String getServiceURL() {
		return getServiceURLCombo().getText();
	}

	public void setActionURL(String s) {
		getActionURLText().typeText(s);
	}

	public String getActionURL() {
		return getActionURLText().getText();
	}

	public void setRequestBody(String s) {
		SWTBotTabItem ti = tabItem(JBossWSUIMessages.JAXRSWSTestView_Request_Body_Label);
		ti.activate();
		text(1).setText(s);
	}

	public void addRequestArg(WsTesterView.Request_Arg_Type type, String name,
			String value) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		text(1).typeText(name + "=" + value);
		WsTesterTest.bot.button("Add").click();
		text(1).setText("");
	}

	public Map<String, String> getRequestArgs(WsTesterView.Request_Arg_Type type) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		String[] args = list(0).getItems();
		Map<String, String> result = new HashMap<String, String>();
		for (String s : args) {
			int i = s.indexOf('=');
			result.put(s.substring(0, i), s.substring(i + 1));
		}
		return result;
	}

	public void editRequestArg(WsTesterView.Request_Arg_Type type,
			String oldName, String oldValue, String newName, String newValue) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		WsTesterTest.bot.list(0).select(oldName + "=" + oldValue);
		WsTesterTest.bot.button("Edit").click();
		SWTBot sh = WsTesterTest.bot.activeShell().bot();
		sh.text(0).typeText(newName + "=" + newValue);
		sh.button(IDialogConstants.OK_LABEL).click();
	}

	public void upRequestArg(WsTesterView.Request_Arg_Type type, String name,
			String value) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		list(0).select(name + "=" + value);
		WsTesterTest.bot.button("Up").click();
	}

	public void downRequestArg(WsTesterView.Request_Arg_Type type, String name,
			String value) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		list(0).select(name + "=" + value);
		WsTesterTest.bot.button("Down").click();
	}

	public void removeRequestArg(WsTesterView.Request_Arg_Type type,
			String name, String value) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		list(0).select(name + "=" + value);
		WsTesterTest.bot.button("Remove").click();
	}

	public void clearRequestArgs(WsTesterView.Request_Arg_Type type) {
		SWTBotTabItem ti = WsTesterTest.bot.tabItem(type.getType());
		ti.activate();
		SWTBotButton b = WsTesterTest.bot.button("Clear All");
		if (b.isEnabled()) {
			b.click();
		}
	}

	public String getResponseBody() {
		SWTBotTabItem ti = tabItem(JBossWSUIMessages.JAXRSWSTestView_Results_Body_Label);
		ti.activate();
		return WsTesterTest.bot.text(2).getText();
	}

	public String[] getResponseHeaders() {
		SWTBotTabItem ti = tabItem(JBossWSUIMessages.JAXRSWSTestView_Results_Header_Label);
		ti.activate();
		return list(1).getItems();
	}

	public void invoke() {
		String dlgTitle = JBossWSUIMessages.JAXRSWSTestView_Invoke_Label;
		button(dlgTitle).click();
		waitWhile(Conditions.shellIsActive(dlgTitle), 120000);
		sleep(500);
	}

	public SelectWSDLDialog getFromWSDL() {
		button(JBossWSUIMessages.JAXRSWSTestView_Button_Get_From_WSDL).click();
		return new SelectWSDLDialog(activeShell().widget);
	}

	public void openMonitor() {
		button(JBossWSUIMessages.JAXRSWSTestView_Open_Monitor_Button).click();
	}

	public void configureMonitor() {
		button(JBossWSUIMessages.JAXRSWSTestView_Configure_Monitor_Button).click();
	}

	private String getStringFromBundle(String key) {
		return Platform.getResourceString(WSUI_BUNDLE, key);
	}

	private SWTBotCombo getHTTPTypeCombo() {
		// HTTP Method:
		return comboBoxWithLabel(JBossWSUIMessages.JAXRSWSTestView_HTTP_Method_Label);
	}

	private SWTBotCombo getServiceURLCombo() {
		// Service URL:
		return comboBoxWithLabel(JBossWSUIMessages.JAXRSWSTestView_Service_URL_Label);
	}

	private SWTBotText getActionURLText() {
		// Action URL:
		return textWithLabel(JBossWSUIMessages.JAXRSWSTestView_Action_URL_Label);
	}
}