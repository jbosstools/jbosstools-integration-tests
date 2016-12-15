/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.websocket;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper.createProject;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsHelper.*;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing WebSocket stub methods integration in ContentAssistant proposals.
 *
 * @author Jan Novak
 *
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.3.0.Final.html#webservices
 */
@JBossServer(state=ServerReqState.PRESENT)
public class StubMethodsTest extends WSTestBase {

	@Before
	public void setup() {
		if (!projectExists(getWsProjectName()))
			createProject(getWsProjectName());
	}

	@After
	public void tearDown() {
		deleteAllProjects();
	}

	@Test
	public void testStubMethods() {
		callAllStubProposals(prepareClass(SERVER_CLASS_NAME));
		callAllStubProposals(prepareClass(CLIENT_CLASS_NAME));
	}

	@Test
	public void testProposalsWithPrefixes() {
		TextEditor editor = prepareClass(SERVER_CLASS_NAME);
		String[] prefixes = {"o", "on", "onC", "onClose"};

		for (String prefix : prefixes) {
			setPrefixIntoFirstClassLine(editor, prefix);
			Collection<String> matchingProposals = filterStubProposalsWithPrefix(prefix);
			assertThereAreOnlySpecifiedStubProposals(editor, matchingProposals);
			clearFirstClassLine(editor);
		}
	}
	
	@Test
	public void testDeleteGeneratedMethod() {
		TextEditor editor = prepareClass(SERVER_CLASS_NAME);
		String proposal = EXPECTED_PROPOSALS.get(0);

		int numberOfLinesBeforeProposalCall = editor.getNumberOfLines();
		int numberOfImportsBeforeProposalCall = countImports(editor);

		//call first proposal
		ContentAssistant contentAssistant = openContentAssistant(editor);
		contentAssistant.chooseProposal(proposal);

		//after call the proposal it should NOT be in contentAssistant
		contentAssistant = openContentAssistant(editor);
		assertFalse(contentAssistant.getProposals().contains(proposal));
		contentAssistant.close();

		//delete generated method
		int linesAdded = editor.getNumberOfLines() - numberOfLinesBeforeProposalCall;
		int newImportsCount = countImports(editor) - numberOfImportsBeforeProposalCall;
		for (int i = 0; i < linesAdded - newImportsCount; i++) {
			delFirstClassLine(editor);
		}

		//check if there is the called proposal again
		contentAssistant = openContentAssistant(editor);
		assertTrue(contentAssistant.getProposals().contains(proposal));
		contentAssistant.close();		
	}

	@Override
	protected String getWsProjectName() {
		return PROJECT_NAME;
	}	 

	static class Constants {
		static final String PROJECT_NAME = "stubMethodTestProject";
		static final String PROJECT_PACKAGE = "org.websocket.test";
		static final String SERVER_CLASS_NAME = "ServerEndpointExample";
		static final String CLIENT_CLASS_NAME = "ClientEndpointExample";
		static final List<String> EXPECTED_PROPOSALS = Arrays.asList(
				"onClose(Session session, CloseReason closeReason): void - @OnClose method for WebSocket endpoint",
				"onError(Session session, Throwable throwable): void - @OnError method for WebSocket endpoint",
				"onMessage(byte[] message): void - @OnMessage method with binary message for WebSocket endpoint",
				"onMessage(PongMessage message): void - @OnMessage method with pong message for WebSocket endpoint",
				"onMessage(String message): void - @OnMessage method with text message for WebSocket endpoint",
				"onOpen(Session session, EndpointConfig endpointConfig): void - @OnOpen method for WebSocket endpoint"
		);
	}

}
