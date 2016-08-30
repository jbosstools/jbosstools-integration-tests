/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.bot.test;

public class ErrorInLog {
	
	private String message;
	private String plugin;
	
	public ErrorInLog(String message, String plugin) {
		this.message =message;
		this.plugin = plugin;
	}

	public String getMessage() {
		return message;
	}

	public String getPlugin() {
		return plugin;
	}
	
	

}
