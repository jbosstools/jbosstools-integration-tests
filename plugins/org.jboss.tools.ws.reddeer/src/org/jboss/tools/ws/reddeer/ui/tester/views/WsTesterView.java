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
package org.jboss.tools.ws.reddeer.ui.tester.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.uiforms.api.ExpandableComposite;
import org.jboss.reddeer.uiforms.impl.expandablecomposite.DefaultExpandableComposite;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;

/**
 * Represents Web Service Tester view<br/>.
 *
 * Web Services > Web Service Tester
 *
 * @see org.jboss.tools.ws.ui.tester.views.TestWSView
 * 
 * @author Lukas Jungmann
 * @author Radoslav Rabara
 *
 */
public class WsTesterView extends WorkbenchView {


	/**
	 * Constructs the view with Web Service Tester.
	 */
	public WsTesterView() {
		super(IDELabel.View.WEB_SERVICE_TESTER);
	}
	
	/**
	 * Sets type of the request.
	 * 
	 * @param type request type
	 */
	public void setRequestType(RequestType type) {
		getRequestTypeCombo().setSelection(type.toString());
	}

	/**
	 * Returns selected request type.
	 *
	 * @return selected request type
	 */
	public RequestType getRequestType() {
		activate();
		return RequestType.parse(getRequestTypeCombo().getText());
	}

	/**
	 * Sets URL of the service.
	 *
	 * @param url service url
	 */
	public void setServiceURL(String url) {
		getServiceURLCombo().setText("");
		KeyboardFactory.getKeyboard().type(url);
	}

	/**
	 * Returns URL of the service.
	 *
	 * @return service url
	 */
	public String getServiceURL() {
		return getServiceURLCombo().getText();
	}

	/**
	 * Sets <var>text</var> to request body.
	 *
	 * @param text text to be set as request body
	 */
	public void setRequestBody(String text) {
		getRequestBodyTextField().setText(text);
	}

	/**
	 * Returns text of the request body.
	 *
	 * @return request body
	 */
	public String getRequestBody() {
		return getRequestBodyTextField().getText();
	}

	/**
	 * Set expansion state of request parameters section.
	 *
	 * @param expanded the new expanded state
	 */
	public void setRequestParametersSectionExpansion(boolean expanded) {
		setSectionExpansion(JBossWSUIMessages.JAXRSWSTestView2_Parameters_Section, expanded);
	}

	/**
	 * Set expansion state of request headers section.
	 *
	 * @param expanded the new expanded state
	 */
	public void setRequestHeadersSectionExpansion(boolean expanded) {
		setSectionExpansion(JBossWSUIMessages.JAXRSWSTestView2_Headers_Section, expanded);
	}

	/**
	 * Set expansion state of response headers section.
	 *
	 * @param expanded the new expanded state
	 */
	public void setResponseHeadersSectionExpansion(boolean expanded) {
		activate();
		setSectionExpansion(JBossWSUIMessages.JAXRSWSTestView2_ResponseHeaders_Section, expanded);
	}

	/**
	 * Set expansion state of request body section.
	 *
	 * @param expanded the new expanded state
	 */
	public void setRequestBodySectionExpansion(boolean expanded) {
		setSectionExpansion(JBossWSUIMessages.JAXRSWSTestView2_BodyText_Section, expanded);
	}

	/**
	 * Set expansion state of response body section.
	 *
	 * @param expanded the new expanded state
	 */
	public void setResponseBodySectionExpansion(boolean expanded) {
		setSectionExpansion(JBossWSUIMessages.JAXRSWSTestView2_ResponseBody_Section, expanded);
	}

	private void setSectionExpansion(String name, boolean expanded) {
		activate();
		ExpandableComposite composite = new DefaultExpandableComposite(name);
		if(composite.isExpanded() != expanded) {
			composite.setExpanded(expanded);
		}
	}

	/**
	 * Adds new header argument with the specified <var>name</var> and
	 * <var>value</var>.
	 *
	 * @param name header name
	 * @param value header value
	 */
	public void addHeaderRequestArg(String name, String value) {
		addRequestArg(RequestArgType.HEADER, name, value);
	}

	/**
	 * Adds new parameter argument with the specified <var>name</var> and
	 * <var>value</var>.
	 *
	 * @param name parameter name
	 * @param value parameter value
	 */
	public void addParameterRequestArg(String name, String value) {
		addRequestArg(RequestArgType.PARAMETER, name, value);
	}

	private void addRequestArg(WsTesterView.RequestArgType type, String name,
			String value) {
		activate();
		new DefaultText(type.ordinal()).typeText(name + "=" + value);
		new PushButton(type.ordinal(), new WithTextMatcher("Add")).click();
		new DefaultText(type.ordinal()).setText("");
	}

	/**
	 * Gets header request arguments.
	 *
	 * @return header request arguments
	 */
	public Map<String, String> getHeaderRequestArgs() {
		return getRequestArgs(RequestArgType.HEADER);
	}

	/**
	 * Gets parameter request arguments.
	 *
	 * @return parameter request arguments
	 */
	public Map<String, String> getParameterRequestArgs() {
		return getRequestArgs(RequestArgType.PARAMETER);
	}

	private Map<String, String> getRequestArgs(WsTesterView.RequestArgType type) {
		activate();
		setSectionExpansion(type.toString(), true);
		String[] args = new DefaultList(new DefaultExpandableComposite(type.toString())).getListItems();
		Map<String, String> result = new HashMap<String, String>();
		for (String s : args) {
			int i = s.indexOf('=');
			result.put(s.substring(0, i), s.substring(i + 1));
		}
		return result;
	}

	/**
	 * Edits header request argument.
	 *
	 * @param oldName header name to edit
	 * @param oldValue header value to edit
	 * @param newName new name of the edited header
	 * @param newValue new value of the edited header
	 *
	 * @return header request arguments
	 */
	public void editHeaderRequestArg(String oldName, String oldValue,
			String newName, String newValue) {
		editRequestArg(RequestArgType.HEADER, oldName, oldValue, newName, newValue);
	}

	/**
	 * Edits parameter request argument.
	 *
	 * @param oldName parameter name to edit
	 * @param oldValue parameter value to edit
	 * @param newName new name of the edited parameter
	 * @param newValue new value of the edited parameter
	 *
	 * @return parameter request arguments
	 */
	public void editParameterRequestArg(String oldName, String oldValue,
			String newName, String newValue) {
		editRequestArg(RequestArgType.PARAMETER, oldName, oldValue, newName, newValue);
	}

	private void editRequestArg(WsTesterView.RequestArgType type,
			String oldName, String oldValue, String newName, String newValue) {
		activate();
		new DefaultList(type.ordinal()).select(oldName + "=" + oldValue);
		new PushButton(type.ordinal(), new WithTextMatcher("Edit")).click();
		new DefaultShell("Edit Value");
		new DefaultText().typeText(newName + "=" + newValue);
		new PushButton(IDialogConstants.OK_LABEL).click();
	}

	/**
	 * Removes header request argument.
	 *
	 * @param name name of header to be removed
	 * @param value value of header to be removed
	 */
	public void removeHeaderRequestArg(String name, String value) {
		removeRequestArg(RequestArgType.HEADER, name, value);
	}

	/**
	 * Removes parameter request argument.
	 *
	 * @param name name of parameter to be removed
	 * @param value value of parameter to be removed
	 */
	public void removeParameterRequestArg(String name, String value) {
		removeRequestArg(RequestArgType.HEADER, name, value);
	}

	private void removeRequestArg(WsTesterView.RequestArgType type,
			String name, String value) {
		activate();
		new DefaultList(type.ordinal()).select(name + "=" + value);
		new PushButton(type.ordinal(), new WithTextMatcher("Remove")).click();
	}

	/**
	 * Clears all header request arguments.
	 */
	public void clearHeaderRequestArgs() {
		clearRequestArgs(RequestArgType.HEADER);
	}

	/**
	 * Clears all parameter request arguments.
	 */
	public void clearParameterRequestArgs() {
		clearRequestArgs(RequestArgType.PARAMETER);
	}

	private void clearRequestArgs(WsTesterView.RequestArgType type) {
		activate();
		new PushButton(type.ordinal(), new WithTextMatcher("Clear All")).click();
	}

	/**
	 * Gets text of response body.
	 *
	 * @return response body
	 */
	public String getResponseBody() {
		activate();
		return new DefaultText(new DefaultExpandableComposite(
				JBossWSUIMessages.JAXRSWSTestView2_ResponseBody_Section))
				.getText();
	}

	/**
	 * Gets response headers.
	 *
	 * @return response headers
	 */
	public String[] getResponseHeaders() {
		setResponseHeadersSectionExpansion(true);
		return new DefaultList(JBossWSUIMessages.JAXRSWSTestView2_ResponseHeaders_Section)
				.getListItems();
	}

	/**
	 * Perform Invoke action.
	 */
	public void invoke() {
		activate();
		new DefaultToolItem(JBossWSUIMessages.JAXRSWSTestView2_Go_Tooltip)
				.click();
		new WaitUntil(new ShellWithTextIsActive(
				JBossWSUIMessages.JAXRSWSTestView_Invoking_WS_Status),
				TimePeriod.NORMAL, false);
		new WaitWhile(new ShellWithTextIsActive(
				JBossWSUIMessages.JAXRSWSTestView_Invoking_WS_Status),
				TimePeriod.LONG);
	}

	/**
	 * Perform Get from WSDL action which opens {@link SelectWSDLDialog}.
	 *
	 * @return dialog that will be opened after the action is performed
	 */
	public SelectWSDLDialog invokeGetFromWSDL() {
		// change default value of combo box to "JAX-WS" to activate tool bar button
		setRequestType(RequestType.JAX_WS);

		new DefaultToolItem(JBossWSUIMessages.JAXRSWSTestView2_GetFromWSDL_Tooltip)
				.click();

		return new SelectWSDLDialog();
	}

	/**
	 * HTTP Method:
	 */
	private Combo getRequestTypeCombo() {
		activate();
		return new DefaultCombo(1);
	}

	/**
	 * Service URL:
	 */
	private Combo getServiceURLCombo() {
		activate();
		return new DefaultCombo(0);
	}

	private Text getRequestBodyTextField() {
		activate();
		return new DefaultText(new DefaultExpandableComposite(
				JBossWSUIMessages.JAXRSWSTestView2_BodyText_Section));
	}

	/**
	 * Enumerates types of request.
	 */
	public enum RequestType {
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

		public static RequestType parse(String s) {
			return "JAX-WS".equals(s) ? JAX_WS : valueOf(s);
		}
	}

	private enum RequestArgType {
		HEADER(JBossWSUIMessages.JAXRSWSTestView2_Headers_Section),
		PARAMETER(JBossWSUIMessages.JAXRSWSTestView2_Parameters_Section);

		private String type;

		RequestArgType(String s) {
			type = s;
		}

		public String getType() {
			return type;
		}

		@Override
		public String toString() {
			return getType();
		}
	}
}