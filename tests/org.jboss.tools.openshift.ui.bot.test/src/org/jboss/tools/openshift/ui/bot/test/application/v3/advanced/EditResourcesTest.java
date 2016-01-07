/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.After;
import org.junit.Test;

public class EditResourcesTest extends AbstractCreateApplicationTest {

	private String customRepo = "https://github.com/mlabuda/jboss-eap-quickstarts";
	private String originalRepo = "https://github.com/jboss-developer/jboss-eap-quickstarts";
	
	private String buildConfig;
	
	@Test
	public void testCanEditResource() {
		getBuildConfig().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT).select();
		
		try {
			new TextEditor("Build Config : eap-app");
			// pass
		} catch (RedDeerException ex) {
			fail("Text editor to modify build config resource has not been opened.");
		}
	}
	
	@Test
	public void testEditBuildConfigAndCheckChangesInExplorer() {
		TextEditor editor = getBuildConfigTextEditor();
		String text = editor.getText();
		if (buildConfig == null) {
			buildConfig = text;
		}
		editor.setText(text.replace(originalRepo, customRepo));
		editor.close(true);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("Changes from updating of a build config should be shown "
				+ "in OpenShift explorer view, but it is not.", 
				getBuildConfig().getAdditionalInfo().equals(customRepo));
	}

	@Test
	public void testIncorrectResourceContent() {
		TextEditor editor = getBuildConfigTextEditor();
		String text = editor.getText();
		if (buildConfig == null) {
			buildConfig = text;
		}
		
		editor.setText(text.replace("\"namespace\" : \"" + DatastoreOS3.PROJECT1 + "\"",
				"\"namespace\" : \"invalid\""));		
		editor.save();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		
		try {
			new DefaultShell("Problem Occurred");
			new OkButton().click();
		} catch (RedDeerException ex) {
			fail("There should be an error dialog, because resource "
					+ "cannot be modified to inappropriate state");
		}
		
		assertTrue("Editor should be rolled back to last correct state, where content was valid",
				editor.getText().equals(buildConfig));
	}
	
	private OpenShiftResource getBuildConfig() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		return explorer.getOpenShift3Connection().getProject().
				getOpenShiftResources(Resource.BUILD_CONFIG).get(0);
	}

	private TextEditor getBuildConfigTextEditor() {
		getBuildConfig().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT).select();
		
		return new TextEditor("Build Config : eap-app");
	}
	
	@After
	public void setOriginalBuildConfigContent() {
		if (buildConfig != null) {
			getBuildConfig().select();
			new ContextMenu(OpenShiftLabel.ContextMenu.EDIT).select();
			
			TextEditor editor = new TextEditor("Build Config : eap-app");
			editor.setText(buildConfig);
			editor.close(true);
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}
	
}
