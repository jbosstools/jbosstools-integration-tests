/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/
package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Test tab in JUnit configuration
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestTab extends RunConfigurationTab {

	public JUnitTestTab() {
		super("Test");
	}
	
	public void setProject(String text){
		activate();
		new LabeledText("Project:").setText(text);
	}
	
	public void setTestClass(String text){
		activate();
		new LabeledText("Test class:").setText(text);
	}
}
