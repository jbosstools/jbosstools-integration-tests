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

package org.jboss.tools.cdi.seam3.bot.test.base;

import java.util.List;

import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.annotation.ProblemsType;
import org.jboss.tools.cdi.reddeer.uiutils.QuickFixHelper;

/**
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
public class SolderAnnotationTestBase extends Seam3TestBase {
	
	protected String APPLICATION_CLASS = "Application.java";
	
	protected static final QuickFixHelper quickFixHelper = new QuickFixHelper();
	
	/**
	 * 
	 * @param projectName
	 */
	protected void testNoBeanValidationProblemExists(String projectName) {
		
		testBeanValidationProblemExists(projectName, true);
		
	}
	
	/**
	 * 
	 * @param projectName
	 */
	protected void testMultipleBeansValidationProblemExists(String projectName) {
		
		testBeanValidationProblemExists(projectName, false);
		
	}
	
	/**
	 * 
	 * @param projectName
	 * @param noBeanEligible
	 */
	private void testBeanValidationProblemExists(String projectName, boolean noBeanEligible) {
		ProblemsView pw = new ProblemsView();
		pw.open();
		AbstractWait.sleep(TimePeriod.NORMAL);
		List<TreeItem> validationProblems = pw.getAllWarnings();
		
		
		//List<TreeItem> validationProblems = quickFixHelper.getProblems(ProblemsType.WARNINGS, projectName);
		assertTrue(validationProblems.size() > 0);
		String validationMessage = noBeanEligible ? CDIConstants.NO_BEAN_IS_ELIGIBLE: CDIConstants.MULTIPLE_BEANS;
		for (TreeItem ti : validationProblems) {
			if (ti.getText().contains(validationMessage)) {
				return;
			}
		}
		fail("CDI Validation problem with text '" 
				+ validationMessage 
				+ "' was not found!");
	}
	
	/**
	 * 
	 * @param projectName
	 * @param openOnString
	 * @param openedClass
	 * @param producer
	 * @param producerMethod
	 */
	protected void testProperInjectBean(String projectName, 
			String openOnString, String openedClass) {
		
		testProperInject(projectName, openOnString, openedClass, false, null);
		
	}
	
	/**
	 * 
	 * @param projectName
	 * @param openOnString
	 * @param openedClass
	 */
	protected void testProperInjectProducer(String projectName, 
			String openOnString, String openedClass, 
			String producerMethod) {
		
		testProperInject(projectName, openOnString, openedClass, true, producerMethod);
		
	}
	
	/**
	 * 
	 * @param projectName
	 * @param openOnString
	 * @param openedClass
	 * @param producer
	 * @param producerMethod
	 */
	private void testProperInject(String projectName, String openOnString, 
			String openedClass, 
			boolean producer, String producerMethod) {
		
		List<TreeItem> validationProblems = quickFixHelper.getProblems(ProblemsType.WARNINGS, projectName);
		assertTrue(validationProblems.size() == 0);
		
		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText(openOnString);
		ContentAssistant ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_INJECT_BEAN)){
				ca.chooseProposal(p);
				break;
			}
		}
		TextEditor t = new TextEditor(openedClass + ".java");
		
		if (producer) {
			assertTrue(t.getSelectedText().equals(producerMethod));
		}
		
	}

}
