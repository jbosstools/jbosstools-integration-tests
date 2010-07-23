/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.widgets;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.view.ViewBase;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class WsTesterView extends ViewBase {

	private static final Bundle WSUI_BUNDLE = Platform.getBundle("org.jboss.tools.ws.ui");

	public enum Request_Type {

		JAX_WS, GET, POST, PUT, DELETE;

		@Override
		public String toString() {
			switch (this) {
			case JAX_WS:
				return "JAX-WS";
			default:
				return super.toString();
			}
		}

		public static Request_Type parse(String s) {
			return "JAX-WS".equals(s) ? JAX_WS : valueOf(s);
		}
	}

	public enum Request_Arg_Type {

		HEADER(JBossWSUIMessages.JAXRSWSTestView2_Headers_Section),
		PARAMETER(JBossWSUIMessages.JAXRSWSTestView2_Parameters_Section);

		private String type;

		private Request_Arg_Type(String s) {
			type = s;
		}

		private String getType() {
			return type;
		}

		@Override
		public String toString() {
			return getType();
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

	public void setRequestType(Request_Type m) {
		getRequestTypeCombo().setSelection(m.toString());
	}

	public Request_Type getRequestType() {
		return Request_Type.parse(getRequestTypeCombo().getText());
	}

	public void setServiceURL(String url) {
		SWTBotCombo c = getServiceURLCombo();
		c.setText("");
		c.typeText(url);
	}

	public String getServiceURL() {
		return getServiceURLCombo().getText();
	}

	public void setRequestBody(String s) {
		// Body Text
		textWithLabel(JBossWSUIMessages.JAXRSWSTestView2_BodyText_Section).setText(s);
	}

	public String getRequestBody() {
		// Body Text
		return textWithLabel(JBossWSUIMessages.JAXRSWSTestView2_BodyText_Section).getText();
	}

	public void expandSection(String name) {
		class X extends SWTBotLabel {
			X(Label y){
				super(y);
			}

			void expand() {
				boolean expanded = syncExec(new BoolResult() {

					public Boolean run() {
						return ((ExpandableComposite) widget.getParent()).isExpanded();
					}
				});
				assert !expanded : "Section '" + getText() + "' is already expanded";
				click(true);
			}
		}
		new X(label(name).widget).expand();
	}

	public void addRequestArg(WsTesterView.Request_Arg_Type type, String name,
			String value) {
		text(type.ordinal()).typeText(name + "=" + value);
		button("Add", type.ordinal()).click();
		text(type.ordinal()).setText("");
	}

	public Map<String, String> getRequestArgs(WsTesterView.Request_Arg_Type type) {
		String[] args = list(type.ordinal()).getItems();
		Map<String, String> result = new HashMap<String, String>();
		for (String s : args) {
			int i = s.indexOf('=');
			result.put(s.substring(0, i), s.substring(i + 1));
		}
		return result;
	}

	public void editRequestArg(WsTesterView.Request_Arg_Type type,
			String oldName, String oldValue, String newName, String newValue) {
		list(type.ordinal()).select(oldName + "=" + oldValue);
		button("Edit", type.ordinal()).click();
		SWTBot sh = activeShell().bot();
		sh.text(0).typeText(newName + "=" + newValue);
		sh.button(IDialogConstants.OK_LABEL).click();
	}

	public void removeRequestArg(WsTesterView.Request_Arg_Type type,
			String name, String value) {
		list(type.ordinal()).select(name + "=" + value);
		button("Remove", type.ordinal()).click();
	}

	public void clearRequestArgs(WsTesterView.Request_Arg_Type type) {
		SWTBotButton b = button("Clear All", type.ordinal());
		b.click();
	}

	public String getResponseBody() {
		return textWithLabel(JBossWSUIMessages.JAXRSWSTestView2_ResponseBody_Section).getText();
	}

	public String[] getResponseHeaders() {
		return listWithLabel(JBossWSUIMessages.JAXRSWSTestView2_ResponseHeaders_Section).getItems();
	}

	public void invoke() {
		toolbarButtonWithTooltip(JBossWSUIMessages.JAXRSWSTestView2_Go_Tooltip).click();
		waitWhile(Conditions.shellIsActive(JBossWSUIMessages.JAXRSWSTestView_Invoke_Label), 120000);
		sleep(500);
	}

	public SelectWSDLDialog getFromWSDL() {
		toolbarButtonWithTooltip(JBossWSUIMessages.JAXRSWSTestView2_GetFromWSDL_Tooltip).click();
		return new SelectWSDLDialog(activeShell().widget);
	}

	private String getStringFromBundle(String key) {
		return Platform.getResourceString(WSUI_BUNDLE, key);
	}

	private SWTBotCombo getRequestTypeCombo() {
		// HTTP Method:
		return comboBox(1);
	}

	private SWTBotCombo getServiceURLCombo() {
		// Service URL:
		return comboBox(0);
	}

}