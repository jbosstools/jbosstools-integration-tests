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

import java.util.Arrays;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class SeamConfigCodeCompletionTest extends Seam3TestBase {

	private static String projectName = "seamConfigCodeCompletion";
	private static final String SEAM_CONFIG = "seam-beans.xml";
	
	@InjectRequirement
    private static ServerRequirement sr;
	
	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		openSeamConfig();
	}
	
	/**
	 * In context of <beans> ... </beans>
	 * Suggest all classes in available packages.
	 */
	@Test
	public void testClassesCodeCompletion() {

		List<String> expectedProposalList = Arrays.asList("r:Envelope - test","r:Report - test");
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Report >"));
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);

		
	}
	
	/**
	 * In context of <beans> ... </beans>
	 * Suggest all annotation types in available packages.
	 */
	@Test
	public void testAnnotationsCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("r:Q1 - test","r:S1 - test");
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Report >"));
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	 *  In context of tag header <r:Report | >
		Suggest all fields available if Report is class;
	 */
	@Test
	public void testClassInFieldsCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("annotatedValue");
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Report >")+10);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
	}
	
	/**
	 *  In context of tag header <r:Report | >
		Suggest all methods available if Report is annotation type.
	 */
	@Test
	public void testAnnotationInMethodsCodeCompletion() {		
		List<String> expectedProposalList = Arrays.asList("someMethod");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:S1 >")+6);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	 *  In context of tag content <r:Report> | </r:Report>
		Suggest <s:replaces/> and <s:modifies/> if Report is class;
	 */
	@Test
	public void testReplacesModifiesCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("s:replaces", "s:modifies");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Report >")+11);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	 * In context of tag content <r:Report> | </r:Report>
	   Suggest <s:parameters> if Report is class;
	 */
	@Test
	public void testParametersCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("s:parameters");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Report >")+11);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
	}
	
	/**
	 * In context of tag content <r:Report> | </r:Report>
	   Suggest all fields and methods available if Report is class;
	 */
	@Test
	public void testClassFieldsAndMethodsCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("r:value", "r:someMethod");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Envelope >")+13);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	 * In context of tag content <r:Report> | </r:Report>
	   Suggest all methods available if Report is annotation type;
	 */
	@Test
	public void testAnnotationMethodsCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("r:someMethod");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:S1 >")+7);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	 * In context of tag content <r:Report> | </r:Report>
	   Suggest all annotation types in available packages.
	 */
	@Test
	public void testAnnotationsInPackageCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("r:Q1 - test", "r:S1 - test");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:Report >")+11);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
	}
	
	/**
	In context of <r:Report> <r:address> | </r:address> </r:Report>
	Suggest <s:value> if 'address' is class field or annotation type method;
	 */
	@Test
	public void testValueCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("s:value");
		
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:annotatedValue>")+18);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	In context of <r:Report> <r:address> | </r:address> </r:Report>
	Suggest <s:entry> if 'address' is class field or annotation type method (maybe we should check that it is map);
	 */
	@Test
	public void testEntryCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("s:entry");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:annotatedValue>")+18);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
	}
	
	/**
	In context of <r:Report> <r:address> | </r:address> </r:Report>
	Suggest <s:parameters> if 'address' is class method;
	 */
	@Test
	public void testMethodParametersCodeCompletion() {
	
		List<String> expectedProposalList = Arrays.asList("s:parameters");
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<r:someMethod>")+14);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		
	}
	
	/**
	In context of <s:entry> | </s:entry>
	Suggest <s:value> and <s:key> 
	 */
	@Test
	public void testValueAndKeyCodeCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("s:key", "s:value");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<s:entry>")+9);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);

	}
	
	/**
	In context of <s:value> | </s:value> or <s:key> | </s:key>
	Suggest all classes in available packages since value may be set as an inline bean.
	 */
	@Test
	public void testInlineBeanCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("r:Report - test", 
				"r:Envelope - test", "r:Q1 - test", "r:S1 - test");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<s:value>")+9);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
		e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("<s:key>")+7);
		
		ca = e.openContentAssistant();
		proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
	}
	
	/**
	In context of xmlns:*="|"
	Suggest "urn:java:" with available packages.
	 */
	@Test
	public void testAvailablePackagesCompletion() {
		
		List<String> expectedProposalList = Arrays.asList("test", "org", "com");
		
		Editor e = new DefaultEditor(SEAM_CONFIG);
		new DefaultStyledText().selectPosition(new DefaultStyledText().getPositionOfText("xmlns:s=\"")+9);
		
		ContentAssistant ca = e.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		proposals.containsAll(expectedProposalList);
		
	}
	
	private static void openSeamConfig() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(IDELabel.WebProjectsTree.WEB_CONTENT,
				IDELabel.WebProjectsTree.WEB_INF, SEAM_CONFIG).open();
		new DefaultEditor(SEAM_CONFIG);
		new DefaultCTabItem("Source").activate();
	}
	
}
