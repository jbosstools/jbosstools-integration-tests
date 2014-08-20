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

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test operates on resource openOn in Seam3 using CDI tools
 * 
 * @author Jaroslav Jankovic
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class ResourceOpenOnTest extends Seam3TestBase {

	private static String projectName = "resource";
	
	@InjectRequirement
	protected ServerRequirement requirement;
	
	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3);
	}

	/**
	 * https://issues.jboss.org/browse/JBIDE-8202
	 */	
	@Test
	public void testResourceOpenOn() {
			
		String className = "MyBean.java";
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, "cdi.seam", className).open();
		
		TextEditor te = new TextEditor(className);
		te.selectText(CDIConstants.RESOURCE_ANNOTATION);
		ContentAssistant ca = te.openOpenOnAssistant();
		assertTrue(ca == null);
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		String[] source = {IDELabel.WebProjectsTree.WEB_CONTENT,
				IDELabel.WebProjectsTree.WEB_INF,IDELabel.WebProjectsTree.BEANS_XML};
		
		String[] dest = {projectName, IDELabel.WebProjectsTree.WEB_CONTENT, CDIConstants.META_INF};
		
		editResourceUtil.moveFileInExplorerBase(projectName, source, dest);
		LOGGER.info("bean.xml was moved to META-INF");
		
		new DefaultEditor(className);
		editResourceUtil.replaceInEditor("WEB", "META");
		
		te = new TextEditor(className);
		te.selectText(CDIConstants.RESOURCE_ANNOTATION);
		ca = te.openOpenOnAssistant();
		assertTrue(ca == null);
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
	}
	
}
	
