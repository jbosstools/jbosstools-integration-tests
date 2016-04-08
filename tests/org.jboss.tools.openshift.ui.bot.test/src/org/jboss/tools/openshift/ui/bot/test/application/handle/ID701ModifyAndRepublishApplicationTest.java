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
package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter.Version;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID407CreateApplicationFromExistingAndChangeRemoteNameTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

/**
 * Test capabilities of modifying and republishing an application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID701ModifyAndRepublishApplicationTest extends IDXXXCreateTestingApplication {

	@Test
	public void testModifyAndRepublishApplication() {
		modifyAndRepublishApplication(applicationName);
	}
	
	public static void modifyAndRepublishApplication(String applicationName) {
		ProjectExplorer explorer = new ProjectExplorer();
		explorer.getProject(applicationName).getProjectItem("diy", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(ID407CreateApplicationFromExistingAndChangeRemoteNameTest.HTML_TEXT);
		editor.save();
		editor.close();
		
		new ServerAdapter(Version.OPENSHIFT2, applicationName).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.PUBLISH).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Identify Yourself"), TimePeriod.NORMAL);
			new DefaultShell("Identify Yourself").setFocus();
			new PushButton("OK").click();
		} catch (WaitTimeoutExpiredException ex) {}
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PUBLISH_CHANGES), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PUBLISH_CHANGES);
		new DefaultStyledText(0).setText("Commit message");
		
		new WaitUntil(new WidgetIsEnabled(new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH)),
				TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		AbstractWait.sleep(TimePeriod.NORMAL);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, applicationName, "OpSh"), TimePeriod.VERY_LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been deployed successfully. Browser does not "
					+ "contain text of existing project which has been deployed.");
		}
	}
}
