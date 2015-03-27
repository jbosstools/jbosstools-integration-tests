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

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;


public class BeansXmlValidationProviderCDI10 extends AbstractValidationProvider {
	
	private final String jsr = "JSR-299"; 

	public BeansXmlValidationProviderCDI10() {
		super();
	}

	@Override
	void init() {
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_CLASS, 
				"There is no class", jsr, Arrays.asList("Create CDI Bean")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_ANNOTATION,
				"There is no annotation",jsr, Arrays.asList("Create CDI")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_ALTERNATIVE_STEREOTYPE,
				"is not @Alternative stereotype annotation",jsr, Arrays.asList("Add annotation @Alternative")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_ALTERNATIVE,
				"is not an alternative bean class",jsr, Arrays.asList("Add annotation @Alternative")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_DECORATOR,
				"There is no class" ,jsr, Arrays.asList("Create CDI Decorator")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_INTERCEPTOR,
				"There is no class" ,jsr, Arrays.asList("Create CDI Interceptor")));
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_BEANS_XML, 
				"Missing beans.xml file in the project",jsr, Arrays.asList("Create File beans.xml")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_INTERCEPTOR,
				"is not an interceptor class" ,jsr, null));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_DECORATOR,
				"is not a decorator bean class" ,jsr, null));
		
	}
	
}
