/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.OpenCDINamedBeanDialog;

public class NamedDialogHasMatchingItems extends AbstractWaitCondition{
	
	
	private OpenCDINamedBeanDialog namedDialog;
	private int matchingItems;
	
	public NamedDialogHasMatchingItems(OpenCDINamedBeanDialog namedDialog, int matchingItems) {
		this.namedDialog = namedDialog;
		this.matchingItems = matchingItems;
	}

	@Override
	public boolean test() {
		return matchingItems == namedDialog.matchingItems().size();
	}
	
	@Override
	public String description() {
		return "CDI Named Dialog has "+matchingItems +" matching items";
	}

}
