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
package org.jboss.tools.vpe.ui.bot.test.browsersim;

import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.After;
/**
 * Parent test for BrowserSim tests
 * @author Vladimir Pakan
 *
 */
public abstract class BrowserSimTest extends JBTSWTBotTestCase{
  @Override
  protected void activePerspective() {
    // do nothing here it's not working 
  }
  /**
   * Returns BrowserSimHandler used by child test
   * @return
   */
  protected abstract BrowserSimHandler getBrowserSimHandler ();
  /**
   * Closes BrowsewrSim once test is finished
   * @throws Exception
   */
  @After
  public void closeBrowserSim() throws Exception {
    // close opened BrowserSim
    if (getBrowserSimHandler() != null) {
      getBrowserSimHandler().close(); 
    }
  }
}
