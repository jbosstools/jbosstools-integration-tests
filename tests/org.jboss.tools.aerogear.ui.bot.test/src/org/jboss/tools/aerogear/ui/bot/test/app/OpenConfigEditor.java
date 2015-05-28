/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.junit.Test;

@CleanWorkspace
public class OpenConfigEditor extends AerogearBotTest {
	@Test
	public void canOpenConfigXmlEditor() {
		openInConfigEditor(CORDOVA_PROJECT_NAME);
		assertNotNull(new DefaultEditor(CORDOVA_APP_NAME));
		
	}
	@Override
	public void tearDown() {
		// close config editor before deleting project
		new DefaultEditor().close();
		super.tearDown();
	}

}
