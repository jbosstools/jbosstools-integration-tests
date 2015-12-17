/*******************************************************************************

 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.palette;

import java.util.LinkedList;
import java.util.List;

import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests if some richFaces components are present in JBoss Palette
 * 
 * @author vlado pakan
 *
 */
public class RichFacesComponentTest extends VPEAutoTestCase {

	/**
	 * Check if some richFaces components are present in JBoss Tools Palette
	 */
	@Test
	public void testRichFacesComponents() {
		openPage();
		openPalette();
		StringBuffer sbMissingComponents = new StringBuffer("");
		for (String componentLabel : RichFacesComponentTest.getRichFacesComponentsToCheck()) {
			if (!SWTBotWebBrowser.paletteContainsPaletteEntry(componentLabel)) {
				if (sbMissingComponents.length() != 0) {
					sbMissingComponents.append("\n");
				}
				sbMissingComponents.append(componentLabel);
			}
		}
		assertTrue("There are these missing richFaces components within JBoss Tools Palette: " + sbMissingComponents,
				sbMissingComponents.length() == 0);
	}

	/**
	 * Returns list of richFaces tags to check for presence within JBoss Tools
	 * Palette
	 * 
	 * @return
	 */
	private static List<String> getRichFacesComponentsToCheck() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("editor");
		result.add("pickList");
		result.add("calendar");
		result.add("tree");
		result.add("inplaceSelect");
		result.add("notifyMessage");
		result.add("tooltip");
		result.add("select");

		return result;
	}

}
