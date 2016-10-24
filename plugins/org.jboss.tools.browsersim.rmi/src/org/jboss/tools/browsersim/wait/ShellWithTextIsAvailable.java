/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.browsersim.wait;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.widgets.ShellLookup;

/**
 * Condition is met when a shell with specific text (title) is available.
 * 
 * @author Andrej Podhradsky (andrej.podhradsky@gmail.com)
 * @author jniederm
 */
public class ShellWithTextIsAvailable extends AbstractWaitCondition { 
	
	private String title;

	/**
	 * Constructs ShellWithTextIsAvailable wait condition.
	 * Condition is met when a shell with the specified title is available.
	 *
	 * @param title the title
	 */
	public ShellWithTextIsAvailable(String title) {
		this.title =title;
	}

	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.condition.WaitCondition#test()
	 */
	@Override
	public boolean test() {
		Shell shell = ShellLookup.getShell(title, TimePeriod.NONE);
		return shell != null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.condition.AbstractWaitCondition#description()
	 */
	@Override
	public String description() {
		return "shell with title " + title + " is available";
	}
}
