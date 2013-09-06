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

import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RESTFullExplorer;
import org.junit.Test;
/**
 * Test if adding jaxrs facet into project causes enabling
 * jboss tools jaxrs tooling
 * 
 * @author jjankovi
 *
 */
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
		RESTFullExplorer restExplorer = new RESTFullExplorer(wsProjectName);
		assertThat("Different count of rest services was expected", 
				restExplorer.getAllRestServices().length, Is.is(1));
			
	}

	private void invokePropertiesForProject() {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		
		packageExplorer.selectProject(getWsProjectName());
		new ContextMenu(IDELabel.Menu.PROPERTIES).select();
		
		new WaitUntil(new ShellWithTextIsActive(PROJECT_PROPERTIES));
		new DefaultShell(PROJECT_PROPERTIES);
	}
	
	private void setJAXRSFacetOnProject() {
		new DefaultTreeItem("Project Facets").select();
		
		new DefaultTreeItem(1, "JAX-RS (REST Web Services)").setChecked(true);
		Button okButton = new PushButton(IDELabel.Button.OK);
		assertFalse("OK Button should be disabled.", okButton.isEnabled());
		
		handleAdditionalConfiguration();
		
	}
	
	private void confirmProjectProperties() {
		new DefaultShell(PROJECT_PROPERTIES);
		
		Button okButton = new PushButton(IDELabel.Button.OK);
		assertTrue("OK Button should be enabled.", okButton.isEnabled());
		okButton.click();
		
		util.waitForNonIgnoredJobs();
		
		new WaitWhile(new ShellWithTextIsActive(PROJECT_PROPERTIES));
	}
	
	private void handleAdditionalConfiguration() {
		bot.hyperlink().click();
		
		new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"));
		new DefaultShell();
		new DefaultCombo("Type:").setSelection("Pure JEE6 Implementation");
		
		Button okButton = new PushButton(IDELabel.Button.OK);
		assertTrue("OK Button should be enabled.", okButton.isEnabled());
		okButton.click();
		
		new WaitWhile(new ShellWithTextIsActive("Modify Faceted Project"));
		
		/** workaround **/
		bot.hyperlink().click();
		new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"));
		new DefaultShell();
		new PushButton(IDELabel.Button.OK).click();
		
		new WaitWhile(new ShellWithTextIsActive("Modify Faceted Project"));
		/** end of workaround **/
	}
	
}
