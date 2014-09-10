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
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestFullExplorer;
import org.jboss.tools.ws.reddeer.jaxrs.core.RestService;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView;
import org.jboss.tools.ws.reddeer.ui.tester.views.WsTesterView.RequestType;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

@Require(server = @Server(state = ServerState.Running))
@JBossServer(state=ServerReqState.RUNNING)
public class XmlJsonFormattingTest extends RESTfulTestBase {

	private static String projectName = "usersRestManager";
	private WsTesterView wsTesterView = new WsTesterView();
	
	private final static String XML_RESPONSE_FORMAT = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<collection>\n    <user>\n        <id>1</id>\n        " +
			"<name>James</name>\n        <phoneNumber>6545646</phoneNumber>" +
			"\n    </user>\n    <user>\n        <id>2</id>\n        " +
			"<name>John</name>\n        <phoneNumber>8546544</phoneNumber>\n    " +
			"</user>\n    <user>\n        <id>3</id>\n        <name>Paul</name>" +
			"\n        <phoneNumber>1287475</phoneNumber>\n    </user>\n</collection>\n";	
	
	private final static String JSON_RESPONSE_FORMAT = 
			"[\r\n{\r\n    \"id\":1,\r\n    \"name\":\"James\"," +
			"\r\n    \"phoneNumber\":6545646\r\n},\r\n    {\r\n    " +
			"\"id\":2,\r\n    \"name\":\"John\",\r\n    " +
			"\"phoneNumber\":8546544\r\n},\r\n    {\r\n    \"id\":3,\r\n    " +
			"\"name\":\"Paul\",\r\n    \"phoneNumber\":1287475\r\n}\r\n]";	
	
	@Override
	public void setup() {
		if (!projectExists(getWsProjectName())) {
			importRestWSProject(getWsProjectName());
			serversViewHelper.addProjectToServer(getWsProjectName(),
					configuredState.getServer().name);
			serversViewHelper.serverClean(configuredState.getServer().name);
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
	public void testWSTesterXMLFormatting() {
		
		testWSTesterFormatting(Format.XML);
		
	}
	
	@Test
	public void testWSTesterJSONFormatting() {
		
		testWSTesterFormatting(Format.JSON);
	}
	
	private void testWSTesterFormatting(Format format) {
		
		restfulWizard = new RestFullExplorer(getWsProjectName());
		
		runRestServiceOnServer(getProperRestService(restfulWizard, format));
		
		assertWebServiceTesterIsActive();
		
		invokeMethodInWSTester(wsTesterView, RequestType.GET);
		
		assertThat(wsTesterView.getResponseBody(), 
				IsEqual.equalTo(format.formattedMessage));
	}
	
	private RestService getProperRestService(RestFullExplorer explorer, 
			Format format) {
		for (RestService service : explorer.getAllRestServices()) {
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
