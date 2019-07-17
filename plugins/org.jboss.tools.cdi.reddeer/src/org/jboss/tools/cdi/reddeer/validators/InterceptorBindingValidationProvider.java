/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.validators;

/** 
 * 
 * @author zcervink@redhat.com
 * 
 */
import java.util.Arrays;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public class InterceptorBindingValidationProvider extends AbstractValidationProvider {

	public InterceptorBindingValidationProvider(String jsr) {
		super(jsr);
	}
	
	@Override
	void init() {
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.ANNOTATION_NONBINDING, 
				"Annotation-valued member of an interceptor binding type should be annotated @Nonbinding",jsr,
				Arrays.asList("Add annotation @Nonbinding to method")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.ARRAY_NONBINDING,
				"Array-valued member of an interceptor binding type must be annotated @Nonbinding",jsr,
				Arrays.asList("Add annotation @Nonbinding to method")));
		
	}
	
}
