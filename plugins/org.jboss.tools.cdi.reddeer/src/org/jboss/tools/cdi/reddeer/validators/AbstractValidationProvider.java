/*******************************************************************************
 * Copyright (c) 2010-2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public abstract class AbstractValidationProvider implements IValidationProvider {

	protected List<ValidationProblem> problems = null;
	protected String jsr;
	protected static final String THERE_IS_NO_CLASS_MSG = "There is no class";

	public AbstractValidationProvider() {
		this("");
	}
	
	public AbstractValidationProvider(String jsr) {	
		this.jsr = jsr;
		problems = new ArrayList<ValidationProblem>();		
		init();
	}
	
	abstract void init();

	public List<ValidationProblem> getAllProblems(){
		return problems;
	}
	
	public ValidationProblem getValidationProblem(ValidationType type){
		for(ValidationProblem p: problems){
			if(p.getValidationType().equals(type)){
				return p;
			}
		}
		return null;
	}
	
	protected void initTheBeansXmlValidationProvider(boolean isCDI1O) {
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_CLASS, 
				THERE_IS_NO_CLASS_MSG, jsr, Arrays.asList("Create CDI Bean")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_ANNOTATION,
				"There is no annotation",jsr, Arrays.asList("Create CDI")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_ALTERNATIVE_STEREOTYPE,
				"is not @Alternative stereotype annotation",jsr, Arrays.asList("Add annotation @Alternative")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_ALTERNATIVE,
				"is not an alternative bean class",jsr, Arrays.asList("Add annotation @Alternative")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_DECORATOR,
				THERE_IS_NO_CLASS_MSG ,jsr, Arrays.asList("Create CDI Decorator")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.NO_INTERCEPTOR,
				THERE_IS_NO_CLASS_MSG ,jsr, Arrays.asList("Create CDI Interceptor")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_INTERCEPTOR,
				"is not an interceptor class" ,jsr, null));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.ISNT_DECORATOR,
				"is not a decorator bean class" ,jsr, null));
		
		if (isCDI1O) {
			problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_BEANS_XML,
					"Missing beans.xml file in the project", jsr, Arrays.asList("Create File beans.xml")));
		}
	}
	
}
