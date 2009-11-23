 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext.types;

import static org.junit.Assert.fail;

/**
 * Provides functionality for evaluation particular entity location like projects types, file types, etc.
 * @author jpeterka
 */
public enum EntityType {
	HIBERNATE_MAPPING_FILE, JAVA_PROJECT, JAVA_CLASS;
		
	public String getGroupLabel() {
		String groupLabel = "";
		
		
		switch (this) {
		case HIBERNATE_MAPPING_FILE: groupLabel = IDELabel.EntityGroup.HIBERNATE; break;
		case JAVA_PROJECT: groupLabel = IDELabel.EntityGroup.JAVA; break;
		case JAVA_CLASS: groupLabel = IDELabel.EntityGroup.JAVA; break;
		default: fail("Unknown Entity Type");
		}
		
		return groupLabel;
	}
	
	/**
	 * Return entity label acc
	 * @return
	 */
	public String getEntityLabel() {
		String entityLabel = "";
		
		switch (this) {
		case HIBERNATE_MAPPING_FILE: entityLabel = IDELabel.EntityLabel.HIBERNATE_MAPPING_FILE; break;
		case JAVA_PROJECT: entityLabel = IDELabel.EntityLabel.JAVA_PROJECT; break;
		case JAVA_CLASS: entityLabel = IDELabel.EntityLabel.JAVA_CLASS; break;
		default: fail("Unknown Entity Type");
		}		
		
		return entityLabel;
	}
}