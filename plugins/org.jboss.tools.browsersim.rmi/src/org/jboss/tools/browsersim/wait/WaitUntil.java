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
 * WaitUntil condition represents a wait condition waiting until specific
 * condition is met.
 * 
 * @author Vlado Pakan
 * @author Lucia Jelinkova
 * 
 */
public class WaitUntil extends AbstractWait {

	/**
	 * Waits until condition is met for default period. Throws
	 * WaitTimeoutExpiredException if condition is not met after expiration
	 * of default time period.
	 * 
	 * @param condition condition to wait until it is met
	 */
	public WaitUntil(WaitCondition condition) {
		super(condition);
	}

	/**
	 * Waits until condition is met for specified time period.
	 * WaitTimeoutExpiredException if condition is not met after expiration
	 * of specified time period.
	 * 
	 * @param condition condition to wait until it is met
	 * @param timeout period to wait for
	 */
	public WaitUntil(WaitCondition condition, TimePeriod timeout) {
		super(condition, timeout);
	}

	/**
	 * Waits until condition is met for specified time period. Can throw
	 * WaitTimeoutExpiredException if condition is not met after expiration
	 * of specified time period.
	 * 
	 * @param condition condition to wait until it is met
	 * @param timeout period to wait for
	 * @param throwWaitTimeoutExpiredException whether exception
	 * should be thrown or not
	 */
	public WaitUntil(WaitCondition condition, TimePeriod timeout,
			boolean throwWaitTimeoutExpiredException) {
		super(condition, timeout, throwWaitTimeoutExpiredException);
	}

	/**
	 * Waits until condition is met for specified time period. Can throw
	 * WaitTimeoutExpiredException if condition is not met after expiration
	 * of specified time period. This constructor also allows to set custom 
	 * test period - time elapsed before another execution of a wait condition 
	 * is performed.
	 * 
	 * @param condition condition to wait until it is met
	 * @param timeout period to wait for
	 * @param throwWaitTimeoutExpiredException whether exception
	 * should be thrown or not
	 * @param testPeriod time to wait before another testing of a wait
	 * condition is performed
	 * 
	 */
	public WaitUntil(WaitCondition condition, TimePeriod timeout,
			boolean throwWaitTimeoutExpiredException, TimePeriod testPeriod) {
		super(condition, timeout, throwWaitTimeoutExpiredException, testPeriod);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.wait.AbstractWait#stopWaiting(org.jboss.reddeer.common.condition.WaitCondition)
	 */
	@Override
	protected boolean stopWaiting(WaitCondition condition) {
		return condition.test();
	}

	/* (non-Javadoc)
	 * @see org.jboss.reddeer.common.wait.AbstractWait#description()
	 */
	@Override
	protected String description() {
		return "Waiting until ";
	}
}
