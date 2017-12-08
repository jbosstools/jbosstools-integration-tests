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

import java.util.List;

import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;
public class ValidationProblem {
	
	private ProblemType problemType;
	
	private ValidationType validationType;
	
	private String message;
	
	private String jsr;
	
	private List<String> quickfixes;
	
	public ValidationProblem(ProblemType problemType, ValidationType validationType, String message, 
			String jsr, List<String> quickfixes) {
		this.problemType = problemType;
		this.validationType = validationType;
		this.message = message;
		this.jsr = jsr;
		this.quickfixes =quickfixes;
	}

	public ProblemType getProblemType() {
		return problemType;
	}

	public ValidationType getValidationType() {
		return validationType;
	}

	public String getMessage() {
		return message;
	}
	
	public String getJSR(){
		return jsr;
	}
	
	public List<String> getQuickFixes(){
		return quickfixes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jsr == null) ? 0 : jsr.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((problemType == null) ? 0 : problemType.hashCode());
		result = prime * result
				+ ((validationType == null) ? 0 : validationType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (Problem.class != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if(!other.getDescription().contains(message)){
			return false;
		}
		if(jsr==null || !other.getDescription().contains(jsr)){
			return false;
		}
		if(!other.getProblemType().equals(problemType)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ValidationProblem [problemType=" + problemType + ", message=" + message + "]";
	}
	
	
}
