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

public class InterceptorValidationProviderCDI10 extends AbstractValidationProvider {
	
	private final String jsr = "JSR-299"; 

	public InterceptorValidationProviderCDI10() {
		super();
	}
	
	@Override
	void init() {
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NAMED, 
				"Interceptor should not have a name",jsr,Arrays.asList(
				"Delete annotation @Named from class")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.SPECIALIZES,
				"Interceptor should not be annotated @Specializes",jsr,Arrays.asList(
				"Delete annotation @Specializes from class")));
				
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCES, 
				"Producer cannot be declared in an interceptor",jsr,Arrays.asList(
				"Delete annotation @Produces from method")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.DISPOSES, 
				"Interceptor has a method annotated @Disposes",jsr,Arrays.asList(
				"Delete annotation @Disposes from parameter")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVES, 
				"Interceptor cannot have a method with a parameter annotated @Observes",jsr,
				Arrays.asList("Delete annotation @Observes from parameter of")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.STATELESS, 
				"Bean class of a session bean cannot be annotated @Interceptor",jsr,Arrays.asList(
				"Delete annotation @Interceptor from class")));
		
	}

}

