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

package org.jboss.tools.cdi.reddeer.validators;

public class BeansXmlValidationProviderCDI10 extends AbstractValidationProvider {

	public BeansXmlValidationProviderCDI10() {
		super();
		jsr = "JSR-299"; 
	}

	@Override
	void init() {
		super.initTheBeansXmlValidationProvider(true);
	}
	
}
