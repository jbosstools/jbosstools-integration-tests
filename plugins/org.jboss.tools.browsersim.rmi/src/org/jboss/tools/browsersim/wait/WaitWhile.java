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
 * WaitWhile condition represents a wait condition waiting while specific
 * condition is met.
 * 
 * @author Vlado Pakan
 * @author Lucia Jelinkova
 * 
 */
public class WaitWhile extends AbstractWait {

	/**
	 * Waits while condition is fulfilled for a default time period.Throws
	 * WaitTimeoutExpiredException if condition is still met and time period expires.
	 * 
	 * @param condition condition to be met while waiting
	 */
	public WaitWhile(WaitCondition condition) {
		super(condition);
	}

	/**
	 * Waits while condition is fulfilled for specified time period.Throws
	 * WaitTimeoutExpiredException if condition is still met and time period expires.
	 * 
	 * @param condition condition to be met while waiting
	 * @param timeout time period to wait for
	 */
	public WaitWhile(WaitCondition condition, TimePeriod timeout) {
		super(condition, timeout);
	}

	/**
	 * Waits while condition is fulfilled for specified time period. Can throw
	 * WaitTimeoutExpiredException if condition is still met and time period expires.
	 * 
	 * @param condition condition to be met while waiting
	 * @param timeout time period to wait for
	 * @param throwWaitTimeoutExpiredException whether exception
	 * should be thrown or not
	 */
	public WaitWhile(WaitCondition condition, TimePeriod timeout,
			boolean throwWaitTimeoutExpiredException) {
		super(condition, timeout, throwWaitTimeoutExpiredException);
	}

	/**
	 * Waits while condition is fulfilled for specified time period. Can throw
	 * WaitTimeoutExpiredException if condition is still met and time period expires.
	 * This constructor also allows to set custom test period - time elapsed before 
	 * another execution of a wait condition is performed.
	 * 
	 * @param condition condition to be met while waiting
	 * @param timeout time period to wait for
	 * @param throwWaitTimeoutExpiredException whether exception
	 * should be thrown or not
	 * @param testPeriod time to wait before another testing of a wait
	 * condition is performed
	 */
	public WaitWhile(WaitCondition condition, TimePeriod timeout,
			boolean throwWaitTimeoutExpiredException, TimePeriod testPeriod) {
		super(condition, timeout, throwWaitTimeoutExpiredException, testPeriod);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.wait.AbstractWait#stopWaiting(org.jboss.reddeer.common.condition.WaitCondition)
	 */
	@Override
	protected boolean stopWaiting(WaitCondition condition) {
		return !condition.test();
	}

	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.wait.AbstractWait#description()
	 */
	@Override
	protected String description() {
		return "Waiting while ";
	}
}
