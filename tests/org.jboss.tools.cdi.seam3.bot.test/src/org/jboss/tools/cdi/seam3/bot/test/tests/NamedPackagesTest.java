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
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.uiutils.CollectionsUtil;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
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
public class NamedPackagesTest extends Seam3TestBase {

	private static final String CDI_SEAM_PACKAGE = "cdi.seam";
	private static final String CDI_TEST_PACKAGE = "cdi.test";
	private static final String ORG_JBOSS_PACKAGE = "org.jboss";
	private static final String projectName = "named";
	
	private static final String PACKAGE_INFO_JAVA_CDI = "package-info.java.cdi";
	private static final String PACKAGE_INFO_JAVA = "package-info.java";
	
	private static final String MANAGER_JAVA = "Manager.java";
	
	@InjectRequirement
    private ServerRequirement sr;
	
	@After
	public void waitForJobs() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.deleteAllProjects();	
	} 
	
	@Test
	public void testNoNamedPackaged() {
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, ORG_JBOSS_PACKAGE, MANAGER_JAVA).open();
		new DefaultEditor(MANAGER_JAVA);
		
		List<String> beansProposal = editResourceUtil.getProposalList(MANAGER_JAVA, "\"#{}\"",3);
		List<String> nonexpectedList = Arrays.asList("bean1 : Bean1", "bean2 : Bean2", 
				"bean3 : Bean3", "bean4 : Bean4");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonexpectedList));
		
	}
	
	@Test
	public void testOneNamedPackage() {
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		editResourceUtil.renameFileInExplorerBase(projectName, PACKAGE_INFO_JAVA, 
				CDIConstants.SRC,CDI_SEAM_PACKAGE, PACKAGE_INFO_JAVA_CDI);
		cleanProjects();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, ORG_JBOSS_PACKAGE, MANAGER_JAVA).open();
		new DefaultEditor(MANAGER_JAVA);

		List<String> beansProposal = editResourceUtil.getProposalList(
				MANAGER_JAVA, "\"#{}\"",3);
		
		List<String> nonExpectedList = Arrays.asList("bean3 : Bean3", "bean4 : Bean4");
		assertTrue(CollectionsUtil.checkNoMatch(beansProposal, nonExpectedList));
		List<String> expectedList = Arrays.asList("bean1 : Bean1", "bean2 : Bean2");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}
	
	@Test
	public void testBothNamedPackages() {
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		
		editResourceUtil.renameFileInExplorerBase(projectName, PACKAGE_INFO_JAVA, 
				CDIConstants.SRC,CDI_SEAM_PACKAGE, PACKAGE_INFO_JAVA_CDI);
		editResourceUtil.renameFileInExplorerBase(projectName, PACKAGE_INFO_JAVA, 
				CDIConstants.SRC,CDI_TEST_PACKAGE, PACKAGE_INFO_JAVA_CDI);
		cleanProjects();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, ORG_JBOSS_PACKAGE, MANAGER_JAVA).open();
		new DefaultEditor(MANAGER_JAVA);
		
		List<String> beansProposal = editResourceUtil.getProposalList(
				MANAGER_JAVA, "\"#{}\"",3);
		
		List<String> expectedList = Arrays.asList("bean1 : Bean1", "bean2 : Bean2", 
				"bean3 : Bean3", "bean4 : Bean4");
		assertTrue(CollectionsUtil.checkMatch(beansProposal, expectedList));
		
	}
	
}
