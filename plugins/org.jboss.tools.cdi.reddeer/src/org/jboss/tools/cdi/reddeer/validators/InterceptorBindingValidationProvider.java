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


import org.jboss.tools.cdi.reddeer.annotation.ProblemsType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public class InterceptorBindingValidationProvider extends AbstractValidationProvider {

	public InterceptorBindingValidationProvider() {
		super();
	}
	
	@Override
	void init() {
		
		problems.add(new ValidationProblem(ProblemsType.WARNINGS, ValidationType.NONBINDING, 
				"Annotation-valued member of an interceptor binding type should be annotated @Nonbinding"));
		
		problems.add(new ValidationProblem(ProblemsType.WARNINGS, ValidationType.NONBINDING,
				"Array-valued member of an interceptor binding type must be annotated @Nonbinding"));
		
	}
	
}
