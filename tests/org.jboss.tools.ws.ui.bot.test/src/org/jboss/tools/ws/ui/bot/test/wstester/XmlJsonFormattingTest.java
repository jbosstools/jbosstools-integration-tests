/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.wstester;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.IsEqual;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.RUNNING, cleanup=false)
public class XmlJsonFormattingTest extends RESTfulTestBase {

	private static String projectName = "usersRestManager";
	private WsTesterView wsTesterView;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
	
	private final static String XML_RESPONSE_FORMAT = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR +
			"<collection>" + LINE_SEPARATOR + "    <user>" + LINE_SEPARATOR + "        <id>1</id>" + LINE_SEPARATOR + "        " +
			"<name>James</name>" + LINE_SEPARATOR + "        <phoneNumber>6545646</phoneNumber>" +
			"" + LINE_SEPARATOR + "    </user>" + LINE_SEPARATOR + "    <user>" + LINE_SEPARATOR + "        <id>2</id>" + LINE_SEPARATOR + "        " +
			"<name>John</name>" + LINE_SEPARATOR + "        <phoneNumber>8546544</phoneNumber>" + LINE_SEPARATOR + "    " +
			"</user>" + LINE_SEPARATOR + "    <user>" + LINE_SEPARATOR + "        <id>3</id>" + LINE_SEPARATOR + "        <name>Paul</name>" +
			"" + LINE_SEPARATOR + "        <phoneNumber>1287475</phoneNumber>" + LINE_SEPARATOR + "    </user>" + LINE_SEPARATOR + "</collection>" + LINE_SEPARATOR;	

	private final static String JSON_RESPONSE_FORMAT = 
			"[\r\n{\r\n    \"id\":1,\r\n    \"name\":\"James\"," +
			"\r\n    \"phoneNumber\":6545646\r\n},\r\n    {\r\n    " +
			"\"id\":2,\r\n    \"name\":\"John\",\r\n    " +
			"\"phoneNumber\":8546544\r\n},\r\n    {\r\n    \"id\":3,\r\n    " +
			"\"name\":\"Paul\",\r\n    \"phoneNumber\":1287475\r\n}\r\n]";	

	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importWSTestProject(getWsProjectName());
		}
		ServersViewHelper.runProjectOnServer(getWsProjectName());
		ServersViewHelper.waitForDeployment(getWsProjectName(), getConfiguredServerName());
		wsTesterView = new WsTesterView();
		wsTesterView.open();
	}

	@Override
	public void cleanup() {
		if (wsTesterView.isOpened()) {
			wsTesterView.close();
		}
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
	}

	@Override
	protected String getWsProjectName() {
		return projectName;
	}

	@Test
	public void testWSTesterXMLFormatting() {
		testWSTesterFormatting(Format.XML);
	}

	@Test
	public void testWSTesterJSONFormatting() {
		testWSTesterFormatting(Format.JSON);
	}

	private void testWSTesterFormatting(Format format) {
		restWebServicesNode = new RESTfulWebServicesNode(getWsProjectName());

		runRestServiceOnServer(getProperRestService(restWebServicesNode, format));

		assertWebServiceTesterIsActive();

		invokeMethodInWSTester(wsTesterView, RequestType.GET);

		assertThat(wsTesterView.getResponseBody(), 
				IsEqual.equalTo(format.formattedMessage));
	}

	private RESTfulWebService getProperRestService(RESTfulWebServicesNode restWebServicesNode, 
			Format format) {
		for (RESTfulWebService service : restWebServicesNode.getWebServices()) {
			if (service.getPath().contains(format.formatType)) { 
				return service;
			}
		}
		return null;
	}

	enum Format {
		XML("xml", XML_RESPONSE_FORMAT), JSON("json", JSON_RESPONSE_FORMAT);

		private String formatType;

		private String formattedMessage;

		private Format(String formatType, String formattedMessage) {
			this.formatType = formatType;
			this.formattedMessage = formattedMessage;
		}

		public String formatType() {
			return formatType;
		}

		public String expectedFormattedMessage() {
			return formattedMessage;
		}
	}
}
