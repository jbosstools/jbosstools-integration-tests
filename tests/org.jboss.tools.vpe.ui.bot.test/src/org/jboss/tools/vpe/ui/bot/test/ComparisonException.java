/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

/**
 * @author Sergey Dzmitrovich
 *
 */
public class ComparisonException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7127064462771778364L;

	public ComparisonException() {
		super();
	}

	public ComparisonException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComparisonException(String message) {
		super(message);
	}

	public ComparisonException(Throwable cause) {
		super(cause);
	}

}
