/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.menu.ToolItemMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;


/**
 * Reddeer implementation for Hybrid Mobile (Cordova) Application Project wizard.
 * @author Pavol Srna
 *
 */
public class NewHybridProjectWizard extends NewWizardDialog {

	/**
	 * Constructs the wizard with Mobile > Hybrid Mobile (Cordova) Application Project
	 */
	public NewHybridProjectWizard() {
		super("Mobile", "Hybrid Mobile (Cordova) Application Project");
	}
	
	public void open(){
		super.open();
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new WidgetIsEnabled(new CancelButton()), TimePeriod.LONG);
	}
	
	public void finish(){
		this.finish(TimePeriod.LONG);
	}
	
	public void finihs(TimePeriod period){
		super.finish(period);
		new WaitUntil(new ConsoleHasCordovaLaunches());
	}
	
	private class ConsoleHasCordovaLaunches extends AbstractWaitCondition {

		@Override
		public boolean test() {
			ConsoleView cw = new ConsoleView();
			cw.open();
			try{
				new ToolItemMenu(new DefaultToolItem("Display Selected Console"), 
						new RegexMatcher(".*<terminated> cordova prepare "));
				
				new ToolItemMenu(new DefaultToolItem("Display Selected Console"), 
						new RegexMatcher(".*<terminated> cordova platform add"));
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
	}

}
