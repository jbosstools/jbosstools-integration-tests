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
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
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
public class SeamConfigEEOpenOnTest extends Seam3TestBase {

	private static String projectName = "seamConfigEEOpenOn";
	private static final String SEAM_CONFIG = "seam-beans.xml";

	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		disableSourceLookup();
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
	public void testAlternativeOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Alternative");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Alternative.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Alternative");

	}

	@Test
	public void testDecoratorOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Decorator");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Decorator.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.decorator.Decorator");

	}

	@Test
	public void testInjectOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Inject");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Inject.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.inject.Inject");

	}

	@Test
	public void testInterceptorOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Interceptor");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Interceptor.class");
		/* test opened object */
		assertExpectedOpenedClass("javax.interceptor.Interceptor");

	}

	@Test
	public void testInterceptorBindingOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:InterceptorBinding");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("InterceptorBinding.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.interceptor.InterceptorBinding");

	}

	@Test
	public void testObservesOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Observes");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Observes.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.event.Observes");

	}

	@Test
	public void testProducesOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Produces");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Produces.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Produces");

	}

	@Test
	public void testQualifierOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Qualifier");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Qualifier.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.inject.Qualifier");

	}

	@Test
	public void testSpecializesOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Specializes");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Specializes.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Specializes");

	}

	@Test
	public void testStereotypeOpenOn() {

		/* open on bean class */
		new DefaultStyledText().selectText("s:Stereotype");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		new DefaultEditor("Stereotype.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Stereotype");

	}

	private void assertExpectedOpenedClass(String qualifiedName) {
		String toolTip = new DefaultEditor().getTitleToolTip();
		String dotToolTip = toolTip.replace('/', '.');
		assertTrue(qualifiedName.contains(dotToolTip));
	}
	
}
