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

package org.jboss.tools.cdi.seam3.bot.test.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.annotation.ProblemsType;
import org.jboss.tools.cdi.reddeer.uiutils.CollectionsUtil;
import org.jboss.tools.cdi.seam3.bot.test.base.SolderAnnotationTestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class FullyQualifiedTest extends SolderAnnotationTestBase {
	
	@InjectRequirement
    private ServerRequirement sr;

	@After
	public void waitForJobs() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.deleteAllProjects();	
	} 
	
	@Test
	public void testNonNamedBean() {

		String projectName = "fullyQualified1";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		List<String> beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		List<String> nonexpectedList = Arrays.asList("cdi.seam.manager : Manager");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
	}

	@Test
	public void testQualifiedPackage() {
		
		String projectName = "fullyQualified2";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		List<String> beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		List<String> nonexpectedList = Arrays.asList("cdi.test.myBean3 : MyBean3", 
				"cdi.test.myBean4 : MyBean4");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		List<String> expectedList = Arrays.asList("cdi.seam.myBean1 : MyBean1", 
				"cdi.seam.myBean2 : MyBean2");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
		expectedList = Arrays.asList("myBean3 : MyBean3", 
				"myBean4 : MyBean4");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}

	@Test
	public void testDifferentExistedPackage() {
		
		String projectName = "fullyQualified3";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		List<String> beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		List<String> nonexpectedList = Arrays.asList("cdi.seam.myBean1 : MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		List<String> expectedList = Arrays.asList("cdi.test.myBean1 : MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}

	@Test
	public void testDifferentNonExistedPackage() {
		
		String projectName = "fullyQualified4";
		String myBean1 = "MyBean1.java";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), myBean1).open();
		new DefaultEditor(myBean1);
		
		List<TreeItem> validationProblems = quickFixHelper.getProblems(
				ProblemsType.ERRORS, projectName);
		assertTrue(validationProblems.size() > 0);
		assertTrue(validationProblems.size() == 1);
		assertTrue(validationProblems.get(0).getText().contains("cannot be resolved to a type"));
		
		editResourceUtil.replaceInEditor("cdi.test.MyBean1", "cdi.seam.MyBean2");
		validationProblems = quickFixHelper.getProblems(
				ProblemsType.ERRORS, projectName);
		assertTrue(validationProblems.size() > 0);
		assertTrue(validationProblems.size() == 1);
		assertTrue(validationProblems.get(0).getText().contains("cannot be resolved to a type"));
		
	}

	@Test
	public void testFullyNamedBean() {
		
		String projectName = "fullyQualified5";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		List<String> beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		List<String> nonexpectedList = Arrays.asList("cdi.seam.myBean1 : MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		List<String> expectedList = Arrays.asList("cdi.seam.bean : MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), "MyBean1.java").open();
		new DefaultEditor("MyBean1.java");
		
		editResourceUtil.replaceInEditor("@FullyQualified", 
				"@FullyQualified(cdi.test.MyBean2.class)");
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		
		nonexpectedList = Arrays.asList("cdi.seam.bean : MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		expectedList = Arrays.asList("cdi.test.bean : MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}

	@Test
	public void testProducerMethod() {
		
		String projectName = "fullyQualified6";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		List<String> beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		List<String> nonexpectedList = Arrays.asList("cdi.seam.myBean1 : MyBean1", 
				"cdi.seam.myBean1 : MyBean1 - MyBean1", "cdi.seam.myBean1 : bean - MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		List<String> expectedList = Arrays.asList("cdi.seam.uniqueBean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), "MyBean1.java").open();
		new DefaultEditor("MyBean1.java");
		
		editResourceUtil.replaceInEditor("@Named", 
				"@Named(\"bean\")");
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		
		nonexpectedList = Arrays.asList("cdi.seam.uniqueBean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		expectedList = Arrays.asList("cdi.seam.bean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), "MyBean1.java").open();
		new DefaultEditor("MyBean1.java");
		
		editResourceUtil.replaceInEditor("@FullyQualified", 
				"@FullyQualified(cdi.test.MyBean2.class)");
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		
		nonexpectedList = Arrays.asList("cdi.seam.uniqueBean : MyBean1 - MyBean1",
				"cdi.seam.bean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		expectedList = Arrays.asList("cdi.test.bean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}
		
	@Test
	public void testProducerField() {
		
		String projectName = "fullyQualified7";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		List<String> beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		List<String> nonexpectedList = Arrays.asList("cdi.seam.myBean1 : MyBean1", 
				"cdi.seam.myBean1 : MyBean1 - MyBean1", "cdi.seam.myBean1 : bean - MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		List<String> expectedList = Arrays.asList("cdi.seam.uniqueBean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), "MyBean1.java").open();
		new DefaultEditor("MyBean1.java");
		
		editResourceUtil.replaceInEditor("@FullyQualified", 
				"@FullyQualified(cdi.test.MyBean2.class)");
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		
		nonexpectedList = Arrays.asList("cdi.seam.uniqueBean : MyBean1 - MyBean1",
				"cdi.seam.bean : MyBean1 - MyBean1", "cdi.test.bean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		expectedList = Arrays.asList("cdi.test.uniqueBean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), "MyBean1.java").open();
		new DefaultEditor("MyBean1.java");
		
		editResourceUtil.replaceInEditor("@Named", 
				"@Named(\"bean\")");
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		beansProposal = editResourceUtil.getProposalList(APPLICATION_CLASS, 
				"\"#{}\"", 3);
		
		nonexpectedList = Arrays.asList("cdi.seam.uniqueBean : MyBean1 - MyBean1",
				"cdi.seam.bean : MyBean1 - MyBean1", "cdi.test.uniqueBean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
		expectedList = Arrays.asList("cdi.test.bean : MyBean1 - MyBean1");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}
	
}
