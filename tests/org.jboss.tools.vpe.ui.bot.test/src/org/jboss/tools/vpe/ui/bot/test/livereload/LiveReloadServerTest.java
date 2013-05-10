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
package org.jboss.tools.vpe.ui.bot.test.livereload;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
import org.junit.After;
/**
 * Tests creating and running LiveReload Server
 * @author Vladimir Pakan
 *
 */
@Require(perspective="JBoss")
public class LiveReloadServerTest extends JBTSWTBotTestCase{
  private static final String SERVER_NAME = "LiveReloadTestServer";
  private static final String SERVER_HOST = "localhost";
  private static final int SERVER_PORT = 35729;
  /**
   * Creates and runs LiveReload Server
   */
	public void testLiveReloadServer(){
	  SWTTestExt.eclipse.addServer(ActionItem.Server.BasicLiveReloadServer.LABEL,
	    LiveReloadServerTest.SERVER_NAME);
	  // LiveReload server is defined
	  assertNotNull("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME 
	      + " is not present in Server View",
	    isLiveReloadServerDefined());
	  // no LiveReload server is listening
	  assertFalse("LiverReload server is listening on " 
	      + LiveReloadServerTest.SERVER_HOST + ":"
	      + LiveReloadServerTest.SERVER_PORT,
	    isLiveReloadServerListening());
	  servers.startServer(LiveReloadServerTest.SERVER_NAME);
	  String serverStatus = servers.getServerStatus(LiveReloadServerTest.SERVER_NAME);
	  // LiveReload server status is Started
	  assertTrue("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME
	      + " does not have status Started",
	    serverStatus.equalsIgnoreCase("Started"));
	  // LiveReload server is listening
	  assertTrue("LiverReload server is not listening on " 
        + LiveReloadServerTest.SERVER_HOST + ":"
        + LiveReloadServerTest.SERVER_PORT,
      isLiveReloadServerListening());
	  servers.stopServer(LiveReloadServerTest.SERVER_NAME);
	  serverStatus = servers.getServerStatus(LiveReloadServerTest.SERVER_NAME);
  	// LiveReload server status is Stopped
    assertTrue("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME
        + " does not have status Stopped",
      serverStatus.equalsIgnoreCase("Stopped"));
    // no LiveReload server is listening
    assertFalse("LiverReload server is listening on " 
        + LiveReloadServerTest.SERVER_HOST + ":"
        + LiveReloadServerTest.SERVER_PORT,
      isLiveReloadServerListening());
    servers.deleteServer(LiveReloadServerTest.SERVER_NAME);
    // LiveReload server is not defined
    assertFalse("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME 
        + " is present in Server View",
      isLiveReloadServerDefined());
	}
  @Override
  protected void activePerspective() {
    // do nothing here it's not working 
  }
	/**
	 * Final cleanup
	 */
  @After
	public void tearDown(){
    // Stops LiveReload server if is running
	  if ((servers != null) 
	    && (isLiveReloadServerDefined())
	    && (servers.getServerStatus(LiveReloadServerTest.SERVER_NAME).equalsIgnoreCase("Started"))){
	    servers.stopServer(LiveReloadServerTest.SERVER_NAME);
	    servers.deleteServer(LiveReloadServerTest.SERVER_NAME);
	  }
	}
	/**
	 * Returns true if LiveReload server is listening on locahost and default port
	 * @return
	 */
  private boolean isLiveReloadServerListening() {
    boolean result = false;
    try {
      Socket socket = new Socket(LiveReloadServerTest.SERVER_HOST, LiveReloadServerTest.SERVER_PORT);
      result = true;
      socket.close();
    } catch (UnknownHostException e) {
      result = false;
    } catch (IOException e) {
      result = false;
    }
    return result;
  }
  /**
   * Returns true when test LiveReload server is present in serverView 
   */
  private boolean isLiveReloadServerDefined(){
    boolean result = false;
    try{
      servers.findServerByName(LiveReloadServerTest.SERVER_NAME);
      result = true;
    } catch (WidgetNotFoundException wnfe){
      result = false;
    }
    
    return result;
  }
}