/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.tools;

import org.apache.log4j.Logger;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.ReflectionsHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.vpe.browsersim.BrowserSimRunner;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

/**
 * Handles testing functionality for BrowserSim
 * 
 * @author Vladimir Pakan
 * 
 */
public class BrowserSimHandler {
  private static final Logger log = Logger.getLogger(BrowserSimHandler.class);
  private boolean urlLoadingCompleted = false;
  private boolean urlLoadingChanged = false;
  private BrowserSim browserSim;
  private SWTBotShell browserSimShell;
  private SWTBot bot;
  
  private static boolean isJavaFxAvailable;
  
  static {
    if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) {
      isJavaFxAvailable = false; // JavaFx web engine is not supported on Linux
    } else {
      isJavaFxAvailable = BrowserSimUtil.loadJavaFX();
    }
      //TODO: add engines initialization once done properly in JBT
    }
  /**
   * Opens BrowserSim with url and waits tiemOut for page to be fully loaded
   * @param url - url of page to open in BrowserSim
   * @param timeOut
   * @return
   */
  public BrowserSimHandler(final String url , SWTBot bot , long timeOut){
    
    this.bot = bot;
            
    BrowserSim browserSim = UIThreadRunnable.syncExec(new Result<BrowserSim>() {
      @Override
      public BrowserSim run() {
        BrowserSim newBrowserSim = new BrowserSim(url, Display.getCurrent().getActiveShell());
        newBrowserSim.open(isJavaFxAvailable);
        return newBrowserSim;
      }
    });
    
    this.browserSim = browserSim;
    this.browserSimShell = bot.activeShell();
        
    if (!url.equalsIgnoreCase(BrowserSimRunner.ABOUT_BLANK) && url != null){
      waitWhileUrlIsLoading(Timing.time500MS() , timeOut);
    }
    
  }
  /**
   * Opens BrowserSim with url
   * @param url - url of page to open in BrowserSim
   * @return
   */
  public BrowserSimHandler(final String url , SWTBot bot){
    this(BrowserSimRunner.ABOUT_BLANK , bot , Timing.time10S());
  }
  /**
   * Opens BrowserSim with empty page
   * @return
   */
  public BrowserSimHandler(SWTBot bot){
    this(BrowserSimRunner.ABOUT_BLANK , bot);
  }
  /**
   * Waits while url is loaded in browserSim
   * @param browserSim
   * @param wait
   * @param sleepTime
   */
  public void waitWhileUrlIsLoading(final int sleepTime , final long timeout){
    setUrlLoadingChanged(false);
    setUrlLoadingCompleted(false);
    ProgressListener pr = new ProgressListener() {
      @Override
      public void completed(ProgressEvent arg0) {
        setUrlLoadingCompleted(true);
      }
      @Override
      public void changed(ProgressEvent arg0) {
        setUrlLoadingChanged(true);
      }
    };
    long startTime = System.currentTimeMillis();
    addProgressListener(pr);
    boolean isCompleted = isUrlLoadingCompleted();
    while (!isCompleted && (System.currentTimeMillis() < startTime + timeout)){
      log.info("Waiting while BrowserSim is loading URL");
      bot.sleep(sleepTime);
      isCompleted = isUrlLoadingCompleted();
    }
    removeProgressListener(pr);
    if (!isCompleted && isUrlLoadingChanged()){
      String url = UIThreadRunnable.syncExec(new Result<String>() {
        @Override
        public String run() {
          return browserSim.getBrowser().getUrl();
        }
      });
      log.error("BrowserSim Url: " + url + " was not loaded completelly");
      throw new TimeoutException("BrowserSim Url: " + url + " was not loaded completelly");
    }
  }
  /**
   * Adds ProgressListener to BrowserSim underlying browser
   * @param browserSim
   * @param progressListener
   */
  public void addProgressListener (final ProgressListener progressListener){
    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        browserSim.getBrowser().addProgressListener(progressListener);
      }
    });
  }
  /**
   * Removes ProgressListener to BrowserSim underlying browser
   * @param browserSim
   * @param progressListener
   */
  public void removeProgressListener (final ProgressListener progressListener){
    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        browserSim.getBrowser().removeProgressListener(progressListener);
      }
    });
  }
  
  private boolean isUrlLoadingCompleted () {
    synchronized(this){
      return this.urlLoadingCompleted;
    }  
  }
  private boolean isUrlLoadingChanged () {
    synchronized(this){
      return this.urlLoadingChanged;
    }
  }
  private void setUrlLoadingCompleted (boolean urlLoadingCompleted) {
    synchronized(this){
      this.urlLoadingCompleted = urlLoadingCompleted;  
    }
  }
  private void setUrlLoadingChanged (boolean urlLoadingChanged) {
    synchronized (this) {
      this.urlLoadingChanged = urlLoadingChanged;
    }    
  }
  /**
   * Returns SWTBotBrowserExt instance of BrowserSim browser 
   * @param browserSim
   * @return
   */
  public SWTBotBrowserExt getBrowserSimBrowser (){
    return new SWTBotBrowserExt(((Browser)browserSim.getBrowser()));
  }
  public Menu openBrowserSimContextMenu (){
    browserSimShell.activate();
    getBrowserSimBrowser().setFocus();
    bot.waitUntil(new ShellIsActiveCondition(browserSimShell),Timing.time2S());
    try {
      Menu[] menus = ReflectionsHelper.getPrivateFieldValue(SWTJBTExt.isRunningOnMacOs() ? Display.class : Decorations.class,
          "menus", browserSimShell.widget, Menu[].class);
      Menu browserSimContextMenu = menus[0];
      ContextMenuHelper.showMenu(browserSimContextMenu);
      return browserSimContextMenu;
    } catch (SecurityException se) {
      throw new WidgetNotFoundException(
          "Unable to find MenuItem with label ", se);
    } catch (NoSuchFieldException nsfe) {
      throw new WidgetNotFoundException(
          "Unable to find MenuItem with label ", nsfe);
    } catch (IllegalArgumentException iae) {
      throw new WidgetNotFoundException(
          "Unable to find MenuItem with label ", iae);
    } catch (IllegalAccessException iace) {
      throw new WidgetNotFoundException(
          "Unable to find MenuItem with label ", iace);
    }
  }
  /**
   * Returns browserSim shell
   * @param browserSim
   * @return
   */
  public SWTBotShell getBrowserSimShell (final BrowserSim browserSim){
    return this.browserSimShell;
  }
  /**
   * Loads Url to BrowserSim browser and waits until it's fully loaded
   * @param url
   */
  public void loadUrlToBrowser (final String url , long timeOut){
    UIThreadRunnable.syncExec(new VoidResult() {
      public void run() {
        browserSim.getBrowser().setUrl(url);
     
      }
    });
    waitWhileUrlIsLoading(Timing.time500MS(), timeOut);
  }
  /**
   * Sets URL url in address bar and waits until it's fully loaded
   * @param url
   */
  public void loadUrlFromAddressBar (final String url , long timeOut){
    browserSimShell.activate();
    SWTBotText addressBarText = getAddressText();
    addressBarText.setText(url);
    addressBarText.setFocus();
    addressBarText.pressShortcut(KeyStroke.getInstance(SWT.CR),KeyStroke.getInstance(SWT.LF));
    waitWhileUrlIsLoading(Timing.time500MS(), timeOut);
  }
  
  public SWTBotText getAddressText() {
    return bot.text();
  }
  
  public SWTBotStyledText getTitleStyledText() {
    return bot.styledText();
  }
  /**
   * Closes BrowserSim
   */
  public void close(){
    browserSimShell.close();
  }
  /**
   * Clicks on BrowserSim context menu specified by menuLabels
   * @param menuLabels
   */
  public void clickContextMenu(String... menuLabels){
    ContextMenuHelper.clickContextMenu(openBrowserSimContextMenu(),menuLabels);
  }
  /**
   * Sets checked status of BrowserSim context menu item with label menuLabel to checked parameter value
   * Fires events only if checked status of menu item was changed 
   * @param checked
   * @param menuLabel
   */
  public void checkContextMenu(boolean checked , String menuLabel){
    if (ContextMenuHelper.isMenuItemChecked(openBrowserSimContextMenu(), menuLabel) != checked){
      clickContextMenu(menuLabel);
    }
  }
}
