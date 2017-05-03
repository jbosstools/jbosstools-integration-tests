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

import java.util.Arrays;

import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public class QualifierValidationProviderCDI11 extends AbstractValidationProvider {
	
	private final String jsr = "JSR-346"; 

	public QualifierValidationProviderCDI11() {
		super();
	}
	
	@Override
	void init() {
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.RETENTION, 
				"Qualifier annotation type must be annotated with @Retention(RUNTIME)",jsr,
				Arrays.asList("to @Retention(RUNTIME)")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_RETENTION, 
				"Qualifier annotation type must be annotated with @Retention(RUNTIME)",jsr,
				Arrays.asList("Add annotation @Retention(RUNTIME) to class")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.TARGET,
				"Qualifier annotation type must be annotated with @Target",jsr,
				Arrays.asList("to @Target({FIELD, PARAMETER})","to @Target({TYPE, METHOD, FIELD, PARAMETER})")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_TARGET,
				"Qualifier annotation type must be annotated with @Target",jsr,
				Arrays.asList("Add annotation @Target({FIELD, PARAMETER})","Add annotation @Target({TYPE, METHOD, FIELD, PARAMETER})")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.ANNOTATION_NONBINDING,
				"Annotation-valued member of a qualifier type should be annotated @Nonbinding",jsr,
				Arrays.asList("Add annotation @Nonbinding to method")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.ARRAY_NONBINDING,
				"Array-valued member of a qualifier type should be annotated @Nonbinding",jsr,
				Arrays.asList("Add annotation @Nonbinding to method")));
				
	}

}
