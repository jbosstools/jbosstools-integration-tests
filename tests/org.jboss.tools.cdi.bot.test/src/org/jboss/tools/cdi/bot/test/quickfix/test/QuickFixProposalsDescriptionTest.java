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

package org.jboss.tools.cdi.bot.test.quickfix.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Test;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class QuickFixProposalsDescriptionTest extends CDITestBase {

	@Override
	public String getProjectName() {
		return "CDIQuickFixProposals";
	}

	@Test
	public void testAddedCode() {
		
		String className = "AddCodeBean.java";
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), className).open();
		TextEditor ed = new TextEditor(className);
		ed.selectText("AddCodeBean");
		
		ContentAssistant ca = ed.openQuickFixContentAssistant();

		String proposeText = null;
		for (String pr: ca.getProposals()) {
			if (pr.contains("Add java.io.Serializable")) {
				ca.chooseProposal(pr);
				proposeText = pr;
				break;
			}
		}
		
		assertNotNull(proposeText);
		
		List<String> affectedLinesInProposal = Arrays.asList("import java.io.Serializable;", 
				"implements Serializable");
		
		for (String affectedLine : affectedLinesInProposal) {
			String text = ed.getText();
			assertTrue(text.contains(affectedLine));
		}

	}
	
	@Test
	public void testRemovedCode() {
	
		String className = "RemoveCodeBean.java";
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), className).open();
		TextEditor ed = new TextEditor(className);
		ed.selectText("@Disposes String param1, @Observes String param2");
		ContentAssistant ca = ed.openQuickFixContentAssistant();

		String proposeText = null;
		
		for (String pr: ca.getProposals()) {
			if (pr.contains("Delete annotation @Disposes")) {
				ca.chooseProposal(pr);
				proposeText = pr;
				break;
			}
		}
		
		assertNotNull(proposeText);
		
		List<String> affectedLinesInProposal = Arrays.
				asList("javax.enterprise.inject.Disposes;", 
				"@Disposes String param1");
		
		for (String affectedLine : affectedLinesInProposal) {
			assertFalse(proposeText.contains(affectedLine));
			String text = ed.getText();
			assertFalse(text.contains(affectedLine));
		}
		
	}
	
	@Test
	public void testEditedCode() {
		
		String className = "EditCodeStereotype.java";
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC,
				getPackageName(), className).open();
		TextEditor ed = new TextEditor(className);
		ed.selectText("@Named(\"name\")");
		
		ContentAssistant ca = ed.openQuickFixContentAssistant();

		String proposeText = null;
		
		for (String pr: ca.getProposals()) {
			if (pr.contains("Change annotation '@Named")) {
				ca.chooseProposal(pr);
				proposeText = pr;
				break;
			}
		}
		
		assertNotNull(proposeText);
		
		List<String> affectedLinesInProposal = Arrays.
				asList("@Named");
		
		for (String affectedLine : affectedLinesInProposal) {
			assertTrue(proposeText.contains(affectedLine));
			String text = ed.getText();
			assertTrue(text.contains(affectedLine));
		}
		
		affectedLinesInProposal = Arrays.
				asList("@Named(\"name\")");
		
		for (String affectedLine : affectedLinesInProposal) {
			String text = ed.getText();
			assertFalse(text.contains(affectedLine));
		}
		
	}
	
}
