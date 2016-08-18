/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.editor.design;

import org.junit.Test;

public class DesignSplitElementTest extends DesignFlowElementsTestTemplate {

	private static final String SPLIT_ID = "My-split";
	
	private static final String FLOW_ID = "My-split-flow";
	
	@Test
	public void createSplit(){
		addSplit(SPLIT_ID);
		addFlowIntoSplit(SPLIT_ID, FLOW_ID);
	}

}
