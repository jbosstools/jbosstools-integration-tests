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
 * Wait condition for shells checking whether specified shell is available
 * 
 * @author rawagner
 */
public class ShellIsAvailable extends AbstractWaitCondition {
	
	private Shell shell;
	
	/**
	 * Fulfilled, when available shell is equal to given shell.
	 * 
	 * @param shell Shell to compare to.
	 */
	public ShellIsAvailable(Shell shell){
		this.shell = shell;
	}

	@Override
	public boolean test() {
		for(org.eclipse.swt.widgets.Shell s: ShellLookup.getShells()){
			if(shell.equals(s)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String description() {
		return "shell is available";
	}

}
