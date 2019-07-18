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
public class StereotypeValidationProviderCDI20 extends AbstractValidationProvider {
	
	private static final String JSR = "JSR-365"; 
	
	public StereotypeValidationProviderCDI20() {
		super();
	}
	
	@Override
	void init() {
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.TARGET, 
				"Stereotype annotation type must be annotated with one of", JSR, null));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.RETENTION,
				"Stereotype annotation type must be annotated with @Retention(RUNTIME)", JSR, 
				Arrays.asList("to @Retention(RUNTIME)")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_RETENTION,
				"Stereotype annotation type must be annotated with @Retention(RUNTIME)", JSR, 
				Arrays.asList("Add annotation @Retention(RUNTIME) to class")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.TYPED,
				"A stereotype should not be annotated @Typed", JSR, Arrays.asList(
				"Delete annotation @Typed")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NAMED, 
				"Stereotype declares a non-empty @Named annotation", JSR, Arrays.asList(
				"to @Named")));
						
	}
		
}
