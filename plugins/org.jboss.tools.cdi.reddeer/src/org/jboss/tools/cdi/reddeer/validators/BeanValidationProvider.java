/*******************************************************************************
 * Copyright (c) 2010-2019 Red Hat, Inc.
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
public class BeanValidationProvider extends AbstractValidationProvider{
	
	private static final String DEL_ANNOTATION_INJECT_FROM_METHOD = "Delete annotation @Inject from method";
	private static final String DEL_ANNOTATION_OBSERVES_FROM_PARAMETER = "Delete annotation @Observes from parameter";
	private static final String DEL_ANNOTATION_DISPOSES_FROM_PARAMETER = "Delete annotation @Disposes from parameter";
	private static final String DEL_ANNOTATION_PRODUCES_FROM_METHOD = "Delete annotation @Produces from method";
	
	public BeanValidationProvider(String jsr) {
		super(jsr);
	}

	@Override
	void init() {
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_BEAN_ELIGIBLE,
				"No bean is eligible for injection to the injection point", jsr, null));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVER_INJECT,
				"Observer method cannot be annotated @Inject", jsr,
				Arrays.asList(DEL_ANNOTATION_INJECT_FROM_METHOD, DEL_ANNOTATION_OBSERVES_FROM_PARAMETER)));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVER_DISPOSES,
				"Observer method has a parameter annotated @Disposes", jsr,
				Arrays.asList(DEL_ANNOTATION_DISPOSES_FROM_PARAMETER, DEL_ANNOTATION_OBSERVES_FROM_PARAMETER)));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCER_DISPOSES,
				"Producer method has a parameter annotated @Disposes", jsr,
				Arrays.asList(DEL_ANNOTATION_DISPOSES_FROM_PARAMETER, DEL_ANNOTATION_PRODUCES_FROM_METHOD)));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCER_OBSERVES,
				"Producer method has a parameter annotated @Observes", jsr,
				Arrays.asList(DEL_ANNOTATION_OBSERVES_FROM_PARAMETER, DEL_ANNOTATION_PRODUCES_FROM_METHOD)));

		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.SERIALIZABLE,
				"which declares a passivating scope SessionScoped must be passivation capable", jsr,
				Arrays.asList("Add java.io.Serializable interface to class")));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCER_INJECT,
				"Producer method or field cannot be annotated @Inject", jsr,
				Arrays.asList(DEL_ANNOTATION_INJECT_FROM_METHOD, DEL_ANNOTATION_PRODUCES_FROM_METHOD)));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.CONSTRUCTOR_DISPOSES,
				"Bean constructor cannot have a parameter annotated @Disposes", jsr,
				Arrays.asList(DEL_ANNOTATION_DISPOSES_FROM_PARAMETER)));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.CONSTRUCTOR_OBSERVES,
				"Bean constructor cannot have a parameter annotated @Observes", jsr,
				Arrays.asList(DEL_ANNOTATION_OBSERVES_FROM_PARAMETER)));

		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.DISPOSER_INJECT,
				"Disposer method cannot be annotated @Inject", jsr,
				Arrays.asList(DEL_ANNOTATION_DISPOSES_FROM_PARAMETER, DEL_ANNOTATION_INJECT_FROM_METHOD)));
	}

}
