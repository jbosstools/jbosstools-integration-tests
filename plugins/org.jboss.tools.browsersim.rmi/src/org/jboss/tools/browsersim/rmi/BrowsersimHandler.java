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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.wait.ShellWithTextIsAvailable;
import org.jboss.tools.browsersim.wait.WaitUntil;
import org.jboss.tools.browsersim.widgets.Browser;
import org.jboss.tools.browsersim.widgets.RDDisplay;
import org.jboss.tools.browsersim.widgets.ResultRunnable;
import org.jboss.tools.browsersim.widgets.WidgetHandler;
import org.jboss.tools.browsersim.widgets.WidgetLookup;

public class BrowsersimHandler extends UnicastRemoteObject implements IBrowsersimHandler{

	private static final long serialVersionUID = 1L;

	protected BrowsersimHandler() throws RemoteException {
		super();
	}

	public void openPreferences() throws RemoteException{
		Menu menu = getBrowsersimMenu();
		MenuItem[] items = WidgetHandler.getMenuItems(menu);
		
		WidgetHandler.menuItemClick(items, "Preferences");
		new WaitUntil(new ShellWithTextIsAvailable("Preferences"));
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
		Menu menu = getBrowsersimMenu();
		
		MenuItem[] items = WidgetHandler.getMenuItems(menu);
		MenuItem[] skinItems = WidgetHandler.getMenuItemsFromMenuItem("Skin", items);
		return WidgetHandler.getMenuItemsText(skinItems);
	}
	
	private Menu getBrowsersimMenu(){
		Control bsControl = WidgetLookup.getBrowsersimControl(WidgetLookup.getBrowsersimShell());
		
		WidgetHandler.notify(SWT.MenuDetect,bsControl);
		
		
		return WidgetHandler.getMenu(bsControl);
	}

	@Override
	public void enableLivereload() throws RemoteException {
		Menu menu = getBrowsersimMenu();
		MenuItem[] items = WidgetHandler.getMenuItems(menu);
		
		WidgetHandler.menuItemClick(items, "Enable LiveReload");
		
	}

	@Override
	public boolean isLivereloadEnabled() throws RemoteException {
		Menu menu = getBrowsersimMenu();
		MenuItem[] items = WidgetHandler.getMenuItems(menu);
		return WidgetHandler.isMenuSelected(items, "Enable LiveReload");
	}
	
	public void setBrowsersimLocation(final int x, final int y){
		RDDisplay.syncExec(new Runnable() {
			
			@Override
			public void run() {
				Shell browsersimShell = WidgetLookup.getBrowsersimShell();
				browsersimShell.setLocation(x, y);
			}
		});
	}
	
	public Point getBrowsersimSize(){
		return RDDisplay.syncExec(new ResultRunnable<Point>() {
			
			@Override
			public Point run() {
				Shell browsersimShell = WidgetLookup.getBrowsersimShell();
				return browsersimShell.getSize();
			}
		});
	}
	
}
