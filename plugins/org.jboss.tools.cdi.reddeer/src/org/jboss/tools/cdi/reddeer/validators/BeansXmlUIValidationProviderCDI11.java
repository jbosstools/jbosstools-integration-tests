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
package org.jboss.tools.cdi.reddeer.validators;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public class BeansXmlUIValidationProviderCDI11 extends AbstractValidationProvider {
	
	private final String jsr = "JSR-346";
	
	public BeansXmlUIValidationProviderCDI11() {
		super();
	}

	@Override
	void init() {
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.INVALID_DISCOVERY_MODE, 
				"The value 'non-existing-mode' of attribute 'bean-discovery-mode' on element 'beans' is not valid", jsr, null));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.INVALID_DISCOVERY_MODE_ENUM,
				"Value 'non-existing-mode' is not facet-valid with respect to enumeration '[annotated, all, none]'. It must be a value from the enumeration.",jsr, null));
	}

}
