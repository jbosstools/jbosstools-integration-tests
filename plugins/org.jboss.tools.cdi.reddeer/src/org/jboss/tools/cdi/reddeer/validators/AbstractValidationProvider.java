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

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public abstract class AbstractValidationProvider implements IValidationProvider {

	protected List<ValidationProblem> problems = null;
	
	public AbstractValidationProvider() {		
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
	
}
