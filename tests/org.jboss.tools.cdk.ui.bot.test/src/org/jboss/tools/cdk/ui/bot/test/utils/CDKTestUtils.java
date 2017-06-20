/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdk.ui.bot.test.utils;

import java.util.Map;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.WidgetIsFound;
import org.jboss.reddeer.core.matcher.ClassMatcher;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

/**
 * Utilities for CDK tests
 * @author odockal
 *
 */
public class CDKTestUtils {

	private static Logger log = Logger.getLogger(CDKTestUtils.class);
	
	public static String getSystemProperty(String systemProperty) {
		String property = System.getProperty(systemProperty);
		if (!(property == null || property.equals("") || property.startsWith("${"))) { //$NON-NLS-1$ //$NON-NLS-2$
			return property;
		}
		return null;
	}
	
	public static void checkParameterNotNull(Map<String, String> dict) {
		for (String key : dict.keySet()) {
			String value = dict.get(key);
			if (value == null) {
				throw new RedDeerException("Given key " + key + " value is null"); //$NON-NLS-1$
			}
			log.info("Given key " + key + " value is " + value);
		}	
	}
	
	public static void deleteCDEServer(String adapter) {
		log.info("Deleting Container Development Environment server adapter:" + adapter); //$NON-NLS-1$
		ServersView servers = new ServersView();
		servers.open();
		try {
			servers.getServer(adapter).delete(true);
		} catch (EclipseLayerException exc) {
			log.error(exc.getMessage());
			exc.printStackTrace();
		}
	}
	
	public static NewServerWizardDialog openNewServerWizardDialog() {
		log.info("Adding new Container Development Environment server adapter"); //$NON-NLS-1$
		// call new server dialog from servers view
		ServersView view = new ServersView();
		view.open();
		NewServerWizardDialog dialog = view.newServer();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		return dialog;
	}
	
	// removes access redhat com credentials used for first cdk run
	public static void removeAccessRedHatCredentials(String domain, String username) {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		dialog.select("JBoss Tools", "Credentials"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
	        new WaitUntil(new WidgetIsFound<org.eclipse.swt.custom.CLabel>(
	        		new ClassMatcher(org.eclipse.swt.custom.CLabel.class), 
	        		new WithMnemonicTextMatcher("Credentials")), TimePeriod.NORMAL); //$NON-NLS-1$
	        new DefaultCLabel("Credentials"); //$NON-NLS-1$
	        DefaultTree tree = new DefaultTree(1);
	        TreeItem item = TreeViewerHandler.getInstance().getTreeItem(tree, new String[]{domain, username});
	        item.select();
	        new PushButton(new WithTextMatcher("Remove User")).click(); //$NON-NLS-1$
	        new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
        } catch (WaitTimeoutExpiredException exc) {
        	log.error("JBoss Tools - Credentials preferences page has timed out"); //$NON-NLS-1$
        	exc.printStackTrace();
        } catch (JFaceLayerException exc) {
        	log.error("JBoss Tools - Credentials does not contain required username to be deleted"); //$NON-NLS-1$
        	exc.printStackTrace();
        } finally {
        	dialog.ok();
		}
	}

}
