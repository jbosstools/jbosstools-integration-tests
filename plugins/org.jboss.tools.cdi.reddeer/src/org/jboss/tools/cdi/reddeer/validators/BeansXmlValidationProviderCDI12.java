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
package org.jboss.tools.cdi.reddeer.validators;

public class BeansXmlValidationProviderCDI12 extends AbstractValidationProvider {
	
	public BeansXmlValidationProviderCDI12() {
		super();
		jsr = "JSR-346";
	}

	@Override
	void init() {
		super.initTheBeansXmlValidationProvider(false);
	}

}
