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

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public class DecoratorValidationProviderCDI10 extends AbstractValidationProvider {

	private final String jsr = "JSR-299"; 
	
	public DecoratorValidationProviderCDI10() {
		super();
	}
	
	@Override
	void init() {
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NAMED, 
				"Decorator should not have a name",jsr,Arrays.asList("Delete annotation @Named from class")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.SPECIALIZES,
				"Decorator should not be annotated @Specializes",jsr,
				Arrays.asList("Delete annotation @Specializes from class")));
				
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCES, 
				"Producer cannot be declared in a decorator",jsr,
				Arrays.asList("Delete annotation @Produces from method")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.DISPOSES, 
				"Decorator has a method annotated @Disposes",jsr,
				Arrays.asList("Delete annotation @Disposes from parameter of")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVES, 
				"Decorator cannot have a method with a parameter annotated @Observes",jsr,
				Arrays.asList("Delete annotation @Observes from parameter of")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.STATELESS, 
				"Bean class of a session bean cannot be annotated @Decorator",jsr,
				Arrays.asList("Delete annotation @Decorator from class")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.DELEGATE, 
				"Bean class that is not a decorator cannot have an injection point annotated @Delegate",jsr,
				null));
	}

}
