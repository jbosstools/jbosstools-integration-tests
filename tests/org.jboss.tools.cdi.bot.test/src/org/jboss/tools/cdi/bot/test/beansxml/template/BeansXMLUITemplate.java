/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;
import org.junit.After;
import org.junit.Test;

public class BeansXMLUITemplate extends CDITestBase {

	private static final Logger logger = Logger.getLogger(BeansXMLUITemplate.class);
	protected IValidationProvider validationProvider = null;

	@After
	public void cleanup() {
		cleanUp();
	}

	@Test
	public void testBeanDiscoveryModes() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		assertTrue(beans.isBeanDiscoveryModeEnabled());
		assertEquals("annotated", beans.getBeanDiscoveryMode());
		List<String> modes = beans.getBeanDiscoveryModes();
		logger.debug("Discovery modes:");
		for (String mode : modes) {
			logger.debug("  " + mode);
		}
		assertEquals(3, modes.size());
		assertTrue(modes.contains("all"));
		assertTrue(modes.contains("none"));
		assertTrue(modes.contains("annotated"));
	}

	@Test
	public void testBeanDiscoveryTreeEditing() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		beans.setBeanDiscoveryMode("non-existing-mode");
		// TODO check error marker
		setAndCheckTreeDiscoveryMode(beans, "all");
		setAndCheckTreeDiscoveryMode(beans, "none");
		setAndCheckTreeDiscoveryMode(beans, "annotated");
	}

	@Test
	public void testBeanDiscoverySourceEditing() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		setAndCheckSourceDiscoveryMode(beans, "non-existing-mode", ValidationType.INVALID_DISCOVERY_MODE,
				ValidationType.INVALID_DISCOVERY_MODE_ENUM);
		setAndCheckSourceDiscoveryMode(beans, "annotated");
	}

	@Test
	public void testBeansVersion() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		
		// JBIDE-26947
		if (CDIVersion.equals("1.2")) {
			assertEquals("1.1", beans.getVersion());
		} else {
			assertEquals(CDIVersion, beans.getVersion());
		}
	}

	@Test
	public void testBeansName() {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		assertEquals("beans", beans.getName());
	}

	private void setAndCheckTreeDiscoveryMode(EditorPartWrapper beans, String mode) {
		beans.activateTreePage();
		beans.selectBeanDiscoveryMode(mode);
		beans.activateSourcePage();
		DefaultStyledText st = new DefaultStyledText();
		st.getText().contains("bean-discovery-mode=\"" + mode + "\"");
		DefaultEditor te = new DefaultEditor();
		assertEquals(0, te.getMarkers().size());
		beans.save();
		assertEquals(0, te.getMarkers().size());
	}

	private void setAndCheckSourceDiscoveryMode(EditorPartWrapper beans, String mode,
			ValidationType... expectedProblems) {
		beans.activateTreePage();
		String currentMode = beans.getBeanDiscoveryMode();

		beans.activateSourcePage();

		editResourceUtil.replaceInEditor("beans.xml", currentMode, mode);

		Editor e = new DefaultEditor();

		if (expectedProblems.length > 0) {
			for (ValidationType validationType : expectedProblems) {
				ValidationProblem expectedProblem = validationProvider.getValidationProblem(validationType);
				List<Problem> foundProblems = validationHelper.findProblems(expectedProblem);
				assertTrue(expectedProblem + " not found. There are these markers: " + e.getMarkers(),
						foundProblems.size() == 1);
			}
		} else {
			new WaitUntil(new EditorHasValidationMarkers(e), TimePeriod.DEFAULT, false);
			new WaitWhile(new EditorHasValidationMarkers(e), TimePeriod.LONG, false);
		}

		beans.activateTreePage();
		assertEquals(mode, beans.getBeanDiscoveryMode());
	}

}
