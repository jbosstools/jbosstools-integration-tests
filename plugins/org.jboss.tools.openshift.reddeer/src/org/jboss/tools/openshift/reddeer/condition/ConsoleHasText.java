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
package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;

/**
 * Condition notifies about not empty console.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ConsoleHasText extends AbstractWaitCondition{

	private ConsoleView consoleView;
	
	public ConsoleHasText() {
		consoleView = new ConsoleView();
		consoleView.open();
	}
	
	@Override
	public boolean test() {
		return !consoleView.getConsoleText().isEmpty();
	}

	@Override
	public String description() {
		return "console contains text";
	}

	
}
