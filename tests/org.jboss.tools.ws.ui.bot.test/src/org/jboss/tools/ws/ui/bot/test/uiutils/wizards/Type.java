/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.uiutils.wizards;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public enum Type {
	SOAP, REST;
	
	private static final Bundle WSUI_BUNDLE = Platform.getBundle("org.jboss.tools.ws.ui");
	
	public String getLabel() {
		switch (this) {
		case SOAP:
			return getStringFromBundle("%JBOSSWS_GENERATEACTION_LABEL");
		case REST:
			return getStringFromBundle("%restful.wizard.name");
		default:
			throw new IllegalArgumentException("Unknown type: " + this);
		}
	}
	
	private static String getStringFromBundle(String key) {
		return Platform.getResourceString(WSUI_BUNDLE, key);
	}
}
