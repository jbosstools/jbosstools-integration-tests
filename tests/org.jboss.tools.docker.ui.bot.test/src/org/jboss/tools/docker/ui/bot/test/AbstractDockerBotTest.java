/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.docker.ui.bot.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * 
 * @author jkopriva
 *
 */

public abstract class AbstractDockerBotTest {
	
	@BeforeClass 
	public static void beforeClass(){
		new WorkbenchShell().maximize();
	}
	
	@After
	public void cleanUp(){
		cleanupShells();
	}
	
	private static void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}
	
	
	protected String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                           new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	
	protected ArrayList<String> getIds(String stringWithIds) {

		ArrayList<String> idList = new ArrayList<String>();

		if (stringWithIds == null || stringWithIds.equals(""))
			return idList;
		
		idList = new ArrayList<String>(Arrays.asList(stringWithIds.split("\\r?\\n")));
		
		return idList;
	}
	
	

}
