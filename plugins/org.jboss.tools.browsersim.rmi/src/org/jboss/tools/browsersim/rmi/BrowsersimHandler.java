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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.swt.api.MenuItem;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.swt.graphics.Point;
import org.jboss.tools.browsersim.browser.Browser;

public class BrowsersimHandler extends UnicastRemoteObject implements IBrowsersimHandler{

	private static final long serialVersionUID = 1L;

	protected BrowsersimHandler() throws RemoteException {
		super();
	}

	public void openPreferences() throws RemoteException{
		Shell s= BrowsersimWidgetLookup.getBrowsersimShell();
		new ContextMenuItem(s, "Preferences");
		new DefaultShell("Preferences");
	}

	@Override
	public boolean isStarted() throws RemoteException {
		return BrowsersimUtil.isStarted();
	}

	@Override
	public String getBrowserText() throws RemoteException {
		Browser b = new Browser();
		return b.getText();
	}

	@Override
	public void openURL(String url) throws RemoteException {
		Browser b = new Browser();
		b.setURL(url);
	}

	@Override
	public void executeOnBrowser(String script) throws RemoteException {
		Browser b = new Browser();
		b.execute(script);
		
	}

	@Override
	public Object evaluateOnBrowser(String script) throws RemoteException {
		Browser b = new Browser();
		return b.evaluate(script);
	}

	@Override
	public void browserForward() throws RemoteException {
		Browser b = new Browser();
		b.forward();
		
	}

	@Override
	public void browserBack() throws RemoteException {
		Browser b = new Browser();
		b.back();
	}

	@Override
	public String getURL() throws RemoteException {
		Browser b= new Browser();
		return b.getURL();
	}
	
	public void getBrowser(){
		new Browser();
	}

	@Override
	public List<String> getSkinsMenuItems() throws RemoteException {
		Shell s= BrowsersimWidgetLookup.getBrowsersimShell();
		List<MenuItem> skins = new ContextMenuItem(s, "Skin").getChildItems();
		return skins.stream().map(t -> t.getText()).collect(Collectors.toList());
	}

	@Override
	public void enableLivereload() throws RemoteException {
		Shell s= BrowsersimWidgetLookup.getBrowsersimShell();
		new ContextMenuItem(s, "Enable LiveReload");
		
	}

	@Override
	public boolean isLivereloadEnabled() throws RemoteException {
		Shell s= BrowsersimWidgetLookup.getBrowsersimShell();
		return new ContextMenuItem(s, "Enable LiveReload").isSelected();
	}
	
	public void setBrowsersimLocation(final int x, final int y){
		final Shell s= BrowsersimWidgetLookup.getBrowsersimShell();
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				s.getSWTWidget().setLocation(x,y);
			}
		});
	}
	
	public Point getBrowsersimSize(){
		final Shell s= BrowsersimWidgetLookup.getBrowsersimShell();
		return Display.syncExec(new ResultRunnable<Point>() {
			
			@Override
			public Point run() {
				return s.getSWTWidget().getSize();
			}
		});
	}
	
}