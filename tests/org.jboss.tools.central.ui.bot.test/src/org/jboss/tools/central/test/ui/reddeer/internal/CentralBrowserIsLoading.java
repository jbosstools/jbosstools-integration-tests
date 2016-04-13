/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.central.test.ui.reddeer.internal;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;

public class CentralBrowserIsLoading extends AbstractWaitCondition{

	@Override
	public boolean test() {
		String html = JavaScriptHelper.getInstance().getHTML();
		System.out.println(html);
		return html.contains("Loading Red Hat Central, please wait");
	}

	@Override
	public String description() {
		return "Central browser is loading";
	}
	
}