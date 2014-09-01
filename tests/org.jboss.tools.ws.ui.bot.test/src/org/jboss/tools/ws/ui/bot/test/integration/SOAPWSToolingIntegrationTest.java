/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.integration;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ws.reddeer.swt.condition.WsTesterNotEmptyResponseText;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class SOAPWSToolingIntegrationTest extends WSTestBase {

	private final static String projectName = "integration2";
	private final String[] pathToRestExplorer = {projectName, "wsdl"};
	private final String request = 
			"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?> " + LINE_SEPARATOR + 
			"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
			"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " + LINE_SEPARATOR + 
			"<soap:Header> " + LINE_SEPARATOR + 
			"</soap:Header>" + LINE_SEPARATOR + 
			"<soap:Body> " + LINE_SEPARATOR + 
			"<webs:sayHello xmlns:webs=\"http://webservices.samples.jboss.org/\">" + LINE_SEPARATOR + 
			"<arg0>User</arg0>" + LINE_SEPARATOR + 
			"</webs:sayHello>" + LINE_SEPARATOR + 
			"</soap:Body>" + LINE_SEPARATOR + 
			"</soap:Envelope>";  
	
	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importWSTestProject(getWsProjectName());
			serversViewHelper.runProjectOnServer(getWsProjectName());
		}
	}
	
	@Override
	public void cleanup() {
		
	}
	
	@Override
	protected String getWsProjectName() {
		return projectName;
	}
	
	@Test
	public void testSimpleIntegration() {
		WsTesterView wsTesterView = openWSDLFileInWSTester();
		testWSDLInWSTester(wsTesterView);
	}
	
	private SWTBotTreeItem getWSDLTreeItem() {
		return projectExplorer.selectTreeItem("HelloWorldService.wsdl", 
				pathToRestExplorer).expand();
	}
	
	private SWTBotTree getProjectExplorerTree() {
		return projectExplorer.bot().tree();
	}
	
	private WsTesterView openWSDLFileInWSTester() {
		SWTBotTree tree = getProjectExplorerTree();
		tree.expandNode(getWsProjectName());
		ContextMenuHelper.prepareTreeItemForContextMenu(
				tree, getWSDLTreeItem());
		SWTBotMenu menu = new SWTBotMenu(ContextMenuHelper.
				getContextMenu(tree, "Web Services", false));
		menu.menu("Test in JBoss Web Service Tester").click();
		WsTesterView tester = new WsTesterView();
		tester.open();
		return tester;
	}
	
	private void testWSDLInWSTester(WsTesterView wsTesterView) {
		wsTesterView.setRequestType(RequestType.JAX_WS);
		wsTesterView.invokeGetFromWSDL().ok();
		wsTesterView.setRequestBody(request);
		wsTesterView.invoke();
		new WaitUntil(new WsTesterNotEmptyResponseText());
        String rsp = wsTesterView.getResponseBody();
        assertTrue(rsp.trim().length() > 0);
        assertTrue(rsp, rsp.contains("Hello User!"));
	}
	
}
