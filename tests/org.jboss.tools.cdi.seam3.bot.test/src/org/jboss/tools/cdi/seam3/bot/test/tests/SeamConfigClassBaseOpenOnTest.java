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

import org.eclipse.swt.SWT;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.Before;
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
public class SeamConfigClassBaseOpenOnTest extends Seam3TestBase {

	private static final String projectName = "seamConfigOpenOn";
	private static final String SEAM_CONFIG = "seam-beans.xml";
	
	@InjectRequirement
    private static ServerRequirement sr;

	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		//disableSourceLookup();
	}

	@Before
	public void openSeamConfig() {

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(IDELabel.WebProjectsTree.WEB_CONTENT,
				IDELabel.WebProjectsTree.WEB_INF, SEAM_CONFIG).open();
		new DefaultEditor(SEAM_CONFIG);
		new DefaultCTabItem("Source").activate();
	}

	@Test
	public void testBeanOpenOn() {
		/* open on bean class */
		new DefaultStyledText().selectText("b:Bean1");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Bean1.java");

		/* test opened object */
		assertExpectedSelection("Bean1");
	}

	@Test
	public void testConstructorOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:parameters");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Bean1.java");

		/* test opened object */
		assertExpectedSelection("Bean1");
	}

	@Test
	public void testFieldOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("b:value");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Bean1.java");

		/* test opened object */
		assertExpectedSelection("value");
	}

	@Test
	public void testMethodOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("b:method");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Bean1.java");

		/* test opened object */
		assertExpectedSelection("method");
	}

	private void assertExpectedSelection(String selectedString) {
		assertEquals(selectedString, new DefaultStyledText().getSelectionText());
	}

}
