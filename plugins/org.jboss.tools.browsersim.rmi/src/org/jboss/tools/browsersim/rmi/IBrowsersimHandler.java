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
package org.jboss.tools.browsersim.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.eclipse.swt.graphics.Point;

public interface IBrowsersimHandler extends Remote{
	
	public void openPreferences() throws RemoteException;
	public String getBrowserText() throws RemoteException;
	public boolean isStarted() throws RemoteException;
	public void openURL(String url) throws RemoteException;
	public String getURL() throws RemoteException;
	public void executeOnBrowser(String script) throws RemoteException;
	public Object evaluateOnBrowser(String script) throws RemoteException;
	public void browserForward() throws RemoteException;
	public void browserBack() throws RemoteException;
	public List<String> getSkinsMenuItems() throws RemoteException;
	public void enableLivereload() throws RemoteException;
	public boolean isLivereloadEnabled() throws RemoteException;
	public void setBrowsersimLocation(int x, int y) throws RemoteException;
	public Point getBrowsersimSize() throws RemoteException;
	public void getBrowser() throws RemoteException;
	

}