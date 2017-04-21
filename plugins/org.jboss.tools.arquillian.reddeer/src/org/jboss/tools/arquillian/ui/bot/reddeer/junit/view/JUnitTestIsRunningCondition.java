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

package org.jboss.tools.arquillian.ui.bot.reddeer.junit.view;

import org.jboss.reddeer.eclipse.jdt.ui.junit.JUnitView;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;

/**
 * Checks if there is running test in JUnit view. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestIsRunningCondition extends AbstractWaitCondition {

	private JUnitView view;
	
	public JUnitTestIsRunningCondition() {
		view = new JUnitView();
		view.open();
	}
	
	@Override
	public boolean test() {
		view.activate();
		return new DefaultToolItem("Stop JUnit Test Run").isEnabled();
	}

	@Override
	public String description() {
		return "at least one JUnit test is running";
	}
}
