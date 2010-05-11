 /*******************************************************************************
  * Copyright (c) 2007-2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.drools.ui.bot.test.smoke;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.junit.Test;
/**
 * Test opening perspective
 * @author Vladimir Pakan
 *
 */
public class OpenDroolsPerspective extends SWTTestExt{
  /**
   * Test Opening Drools Rules
   */
  @Test
  public void testManageDroolsProject() {
    openDroolsPerspective();
  }
  /**
   * Open Drools Perspective
   */
  private void openDroolsPerspective(){
    eclipse.openPerspective(PerspectiveType.DROOLS);
    boolean wasFound = false;
    try{
      bot.toolbarDropDownButtonWithTooltip(IDELabel.Button.DROOLS_WORKBENCH);
      wasFound = true;
    } catch (WidgetNotFoundException wnfe){
      wasFound = false;
    }
    eclipse.openPerspective(PerspectiveType.JAVA);
    assertTrue("Drools Perspective was not opened properly. Button  " +
      IDELabel.Button.DROOLS_WORKBENCH + " is not present in Workbench",
      wasFound);
  }
}

