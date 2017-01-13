/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.reddeer.editor;

public enum BatchExceptions {
	SKIPPABLE("Skippable Exception Classes", "Skippable-exception-classes"), 
	RETRYABLE("Retryable Exception Classes", "Retryable-exception-classes"), 
	NOROLLBACK("No Rollback Exception Classes", "No-rollback-exception-classes");

	private final String sectionName;
	private final String type;
	
	private BatchExceptions(String sectionName, String type) {
		this.sectionName = sectionName;
		this.type = type;
	}

	public String getSectionName() {
		return sectionName;
	}

	public String getType() {
		return type;
	}

	
}
