/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
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
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;

public class ContextMenuIsEnabled extends AbstractWaitCondition {

	private String[] path;
	
	public ContextMenuIsEnabled(String... path) {
		this.path = path;
	}

	@Override
	public boolean test() {
		return new ContextMenuItem(path).isEnabled();
	}

	@Override
	public String description() {
		return "context menu " + String.join("/", path) + "item is enabled";
	}
	
}
