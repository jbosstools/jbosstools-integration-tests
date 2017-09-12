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

import org.eclipse.reddeer.eclipse.jdt.junit.ui.TestRunnerViewPart;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;

/**
 * Checks if there is running test in JUnit view. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestIsRunningCondition extends AbstractWaitCondition {

	private TestRunnerViewPart view;
	
	public JUnitTestIsRunningCondition() {
		view = new TestRunnerViewPart();
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
