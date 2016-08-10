/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.archives.reddeer.archives.ui.ProjectArchivesView;
import org.junit.Test;

/**
 * Tests if Project Archives View is present in Show View dialog 
 * and can be opened as well
 * 
 * @author jjankovi
 *
 */
public class ViewIsPresentTest extends ArchivesTestBase {

	@Test
	public void testArchivesViewIsPresent() {
		ProjectArchivesView pw = new ProjectArchivesView();
		pw.open();
		assertTrue(pw.isOpened());
	}
	
}
