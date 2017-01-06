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
package org.jboss.tools.forge.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.forge.reddeer.view.ForgeConsoleView;

/**
 * Returns true if a console has no change for the specified time period.
 * 
 * @author jkopriva@redhat.com
 * 
 */
public class ForgeConsoleHasNoChange extends AbstractWaitCondition {

	private String consoleText;

	/**
	 * Constructs the condition.
	 * 
	 */
	public ForgeConsoleHasNoChange() {
		this.consoleText = getConsoleText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.reddeer.common.condition.WaitCondition#test()
	 */
	@Override
	public boolean test() {
		String currentConsoleText = getConsoleText();

		if (!currentConsoleText.equals(consoleText)) {
			consoleText = currentConsoleText;
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.reddeer.common.condition.AbstractWaitCondition#description()
	 */
	@Override
	public String description() {
		return "Forge console is still changing";
	}

	/**
	 * Returns Forge Console text.
	 * 
	 * @return String
	 *            Console Text
	 */
	private static String getConsoleText() {
		ForgeConsoleView forgeConsoleView = new ForgeConsoleView();
		forgeConsoleView.open();
		return forgeConsoleView.getConsoleText();
	}
}
