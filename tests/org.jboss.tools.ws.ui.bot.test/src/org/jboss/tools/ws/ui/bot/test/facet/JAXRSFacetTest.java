/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.facet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebServicesNode;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test if adding JAX-RS facet into project causes enabling
 * jboss tools JAX-RS tooling
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
public class JAXRSFacetTest extends RESTfulTestBase {

	private static String wsProjectName = "jaxrsFacet";
	
	private static final String PROJECT_PROPERTIES = "Properties for " + wsProjectName;
	
	@Override
	protected String getWsProjectName() {
		return wsProjectName;
	}

	@Override
	public void cleanup() {

	}

	@Test
	public void testJAXRSFacet() {
		setJAXRSFacet();
		checkJAXRSTooling();
	}

	private void setJAXRSFacet() {
		invokePropertiesForProject();
		setJAXRSFacetOnProject();
		confirmProjectProperties();
	}
	
	private void checkJAXRSTooling() {
		RESTfulWebServicesNode restWebServicesNode = new RESTfulWebServicesNode(wsProjectName);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertThat("Different count of rest services was expected", 
				restWebServicesNode.getWebServices().size(), Is.is(1));
	}

	private void invokePropertiesForProject() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		projectExplorer.getProject(getWsProjectName()).select();
		new ContextMenu("Properties").select();
		
		new DefaultShell(PROJECT_PROPERTIES);
	}
	
	private void setJAXRSFacetOnProject() {
		new DefaultTreeItem("Project Facets").select();
		
		new DefaultTreeItem(new DefaultTree(1), "JAX-RS (REST Web Services)").setChecked(true);
		assertTrue("OK Button should be enabled.", getOkButton().isEnabled());
		
		handleAdditionalConfiguration();
	}
	
	private void confirmProjectProperties() {
		new DefaultShell(PROJECT_PROPERTIES);
		
		Button okButton = getOkButton();
		assertTrue("OK Button should be enabled.", okButton.isEnabled());
		okButton.click();

		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20), false);
		
		new WaitWhile(new ShellWithTextIsActive(PROJECT_PROPERTIES));
	}
	
	private void handleAdditionalConfiguration() {
		new DefaultHyperlink().activate();
		
		new DefaultShell("Modify Faceted Project");
		new LabeledCombo("Type:").setSelection("Pure JEE6 Implementation");
		
		Button okButton = getOkButton();
		assertTrue("OK Button should be enabled.", okButton.isEnabled());
		okButton.click();
		
		new WaitWhile(new ShellWithTextIsActive("Modify Faceted Project"));
		
		/** workaround **/
		new DefaultHyperlink().activate();
		new DefaultShell("Modify Faceted Project");
		getOkButton().click();
		
		new WaitWhile(new ShellWithTextIsActive("Modify Faceted Project"));
		/** end of workaround **/
	}
	
	private Button getOkButton() {
		return new PushButton("OK");
	}
}
