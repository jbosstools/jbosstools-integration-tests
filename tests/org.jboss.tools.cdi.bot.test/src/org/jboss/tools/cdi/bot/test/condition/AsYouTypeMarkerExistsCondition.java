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

package org.jboss.tools.cdi.bot.test.condition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.cdi.bot.test.uiutils.AsYouTypeValidationHelper;

public class AsYouTypeMarkerExistsCondition implements ICondition {

	private AsYouTypeValidationHelper helper = new AsYouTypeValidationHelper();
	
	private String message;
	
	public AsYouTypeMarkerExistsCondition() {
		this(null);
	}
	
	public AsYouTypeMarkerExistsCondition(String message) {
		this.message = message;
	}
	
	public boolean test() throws Exception {
		return helper.markerExists(helper.getAnnotationModel(), null, message);
	}

	public void init(SWTBot bot) {
		// nothing do here
	}

	public String getFailureMessage() {
		return "No as-you-type marker exists in '" + 
				helper.getActiveTextEditor().getTitle() + "'";
	}

}
