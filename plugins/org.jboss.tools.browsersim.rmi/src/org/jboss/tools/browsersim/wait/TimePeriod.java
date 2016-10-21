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
 * Represents the time period for how long the user operation might last. 
 * Predefined values should cover most of the use cases. There is also a 
 * possibility to define own time period - see {@link #getCustom(long)} method. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class TimePeriod {

	/** Time period 0 seconds. */
	public static final TimePeriod NONE = new TimePeriod(0);

	/** Time period 1 second. */
	public static final TimePeriod SHORT = new TimePeriod(1);

	/** Time period 10 seconds. */
	public static final TimePeriod NORMAL = new TimePeriod(10);

	/** Time period 60 seconds. */
	public static final TimePeriod LONG = new TimePeriod(60);

	/** Time period 300 seconds. */
	public static final TimePeriod VERY_LONG = new TimePeriod(300);

	/** Time period for eternity */
	public static final TimePeriod ETERNAL = new TimePeriod(Long.MAX_VALUE);

	private static Float FACTOR = new Float(1.0);

	private long seconds;

	private TimePeriod(long seconds) {
		this.seconds = seconds;
	}

	/**
	 * Gets duration of a time period in seconds.
	 * 
	 * @return duration of time period in seconds
	 */
	public long getSeconds() {
		if (seconds == Long.MAX_VALUE || seconds == 0) {
			return seconds;
		}

		if (FACTOR == 1) {
			return seconds;
		}

		// do not exceed max value of Long
		if (seconds >= Long.MAX_VALUE / FACTOR) {
			return Long.MAX_VALUE;
		} else {
			return Math.round((double) seconds * FACTOR);
		}
	}

	/**
	 * Gets custom duration of a time period specified by seconds.
	 * 
	 * @param seconds how many seconds should time period last
	 * @return time period with custom time frame in seconds
	 */
	public static TimePeriod getCustom(long seconds) {
		if (seconds < 0) {
			throw new IllegalArgumentException("Time in seconds has to be positive number");
		}
		return new TimePeriod(seconds);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Time period " + seconds + " s (factor " + FACTOR + " was used).";
	}
}
