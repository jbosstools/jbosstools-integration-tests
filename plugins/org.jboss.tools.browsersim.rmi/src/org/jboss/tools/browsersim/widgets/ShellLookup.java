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
package org.jboss.tools.browsersim.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.wait.ShellWithTextIsAvailable;
import org.jboss.tools.browsersim.wait.TimePeriod;
import org.jboss.tools.browsersim.wait.WaitUntil;

public class ShellLookup {
	
	public static Shell getShell(final String shellTitle , TimePeriod timePeriod) {
		if (!timePeriod.equals(TimePeriod.NONE)){
			new WaitUntil(new ShellWithTextIsAvailable(shellTitle), timePeriod, false);
		}
		
		return RDDisplay.syncExec(new ResultRunnable<Shell>() {
			
			@Override
			public Shell run() {
				Shell[] shell = RDDisplay.getDisplay().getShells(); 
				for(Shell s: shell){
					if(shellTitle.equals(s.getText())){
						return s;
					}
				}
				return null;
			}
		});
	}
	
	/**
	 * Gets currently Active Shell without waiting for shell to become active.
	 * 
	 * @return active shell or null if there is no active shell
	 */
	public static Shell getCurrentActiveShell () {
		return RDDisplay.syncExec(new ResultRunnable<Shell>() {
			
			@Override
			public Shell run() {
				Shell s = RDDisplay.getDisplay().getActiveShell();
				if(s!=null && s.isVisible()){
					return s;
				}
				return null;
			}
		});
	}
	
	/**
	 * Gets all visible shells.
	 * 
	 * @return array of all visible shells
	 */
	public static Shell[] getShells() {
		
		return RDDisplay.syncExec(new ResultRunnable<Shell[]>() {
			
			@Override
			public Shell[] run() {
				List<Shell> visibleShells = new ArrayList<Shell>();
				Shell[] shells = RDDisplay.getDisplay().getShells();
				for (Shell s : shells) {
					if (!s.isDisposed() && s.isVisible()) {
						visibleShells.add(s);
					}
				}
				return visibleShells.toArray(new Shell[visibleShells.size()]);
			}
			
		});
	}

}
