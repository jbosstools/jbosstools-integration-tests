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

public enum BatchExceptionType {

	INCLUDE(0, "Includes:"),
	EXCLUDE(1, "Excludes:");
	
	private int index;
	private String type;
	
	private BatchExceptionType(int index, String type) {
		this.index = index;
		this.type = type;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getType() {
		return type;
	}
	
}
