/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.browsersim.wait;

/**
 * Interface WaitCondition offers API for wait conditions. 
 * 
 * @author mlabuda
 */
public interface WaitCondition {

	/**
	 * Tests whether condition is met or not.
	 * 
	 * @return true if condition is met, false otherwise
	 */
	boolean test();
	
	/**
	 * Gets description of specific condition. This is 
	 * useful in logging.
	 * 
	 * @return description of specific wait condition
	 */
	String description();
	

	/**
	 * Gets error message if the condition fails. This is 
	 * useful in logging.
	 * 
	 * @return description of specific wait condition
	 */
	String errorMessage();
}
