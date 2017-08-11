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

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.requirement.CleanOpenShiftConnectionRequirement.CleanConnection;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftConnectionRequirement.RequiredBasicConnection;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

@RequiredBasicConnection
@CleanConnection
public class EditResourcesTest extends AbstractCreateApplicationTest {

	private String customRepo = "https://github.com/rhopp/jboss-eap-quickstarts";
	private String originalRepo = "https://github.com/jboss-developer/jboss-eap-quickstarts";
	
	private String buildConfig;
	
	private static final String BUILD_CONFIG_EDITOR = "[" + DatastoreOS3.PROJECT1 + "] Build Config : eap-app.json";
	
	@Test
	public void testCanEditResource() {
		getBuildConfig().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT).select();
		
		try {
			new TextEditor(BUILD_CONFIG_EDITOR);
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
				+ "in Properties view, but it is not.", 
				getBuildConfig().getPropertyValue("Source", "URI").equals(customRepo));
	}

	@Test
	public void testIncorrectResourceContent() {
		TextEditor editor = getBuildConfigTextEditor();
		String text = editor.getText();
		if (buildConfig == null) {
			buildConfig = text;
		}
		
		editor.setText(text.replace("\"namespace\" : \"" + DatastoreOS3.PROJECT1 + "\"",
				"\"namespace\" : \"" + DatastoreOS3.PROJECT1 + "\"wtf"));		
		try {
			editor.save();
		} catch (CoreLayerException ex) {
			// ok
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		assertTrue("Editor should be dirty, it should not be able to save incorrect content", editor.isDirty());
		try {
			new DefaultShell("Problem Occurred");
			new OkButton().click();
		} catch (RedDeerException ex) {
			// sometimes it occures, sometimes not
		}
	}
	
	private OpenShiftResource getBuildConfig() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		return explorer.getOpenShift3Connection().getProject().
				getOpenShiftResources(Resource.BUILD_CONFIG).get(0);
	}

	private TextEditor getBuildConfigTextEditor() {
		getBuildConfig().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT).select();
		
		return new TextEditor(BUILD_CONFIG_EDITOR);
	}
	
	@After
	public void setOriginalBuildConfigContent() {
		if (buildConfig != null) {
			getBuildConfig().select();
			new ContextMenu(OpenShiftLabel.ContextMenu.EDIT).select();
			
			TextEditor editor = new TextEditor(BUILD_CONFIG_EDITOR);
			editor.setText(buildConfig);
			editor.close(true);
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}
	
	@AfterClass
	public static void closeEditor() {
		try {
			new TextEditor(new StringContains(BUILD_CONFIG_EDITOR)).close(false);
		} catch (RedDeerException ex) {
			// do nothing, there is no editor
		}
	}
}
