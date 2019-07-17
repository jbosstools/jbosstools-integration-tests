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

import java.util.Arrays;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

/** 
 * 
 * @author zcervink@redhat.com
 * 
 */
public class InterceptorValidationProvider extends AbstractValidationProvider {
	
	private static final String DEL_ANNOTATION_DISPOSES_FROM_PARAMETER = "Delete annotation @Disposes from parameter";
	private static final String DEL_ANNOTATION_PRODUCES_FROM_METHOD = "Delete annotation @Produces from method";

	public InterceptorValidationProvider(String jsr) {
		super(jsr);
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
				DEL_ANNOTATION_PRODUCES_FROM_METHOD)));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.DISPOSES, 
				"Interceptor has a method annotated @Disposes",jsr,Arrays.asList(
				DEL_ANNOTATION_DISPOSES_FROM_PARAMETER)));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVES, 
				"Interceptor cannot have a method with a parameter annotated @Observes",jsr,
				Arrays.asList("Delete annotation @Observes from parameter of")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.STATELESS, 
				"Bean class of a session bean cannot be annotated @Interceptor",jsr,Arrays.asList(
				"Delete annotation @Interceptor from class")));
		
	}

}

