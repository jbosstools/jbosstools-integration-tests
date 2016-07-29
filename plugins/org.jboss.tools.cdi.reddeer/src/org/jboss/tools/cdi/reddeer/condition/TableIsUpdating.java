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
package org.jboss.tools.cdi.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.api.Table;

public class TableIsUpdating extends AbstractWaitCondition{
	
	private Table table;
	private int rowCount;
	private long time;
	private TimePeriod timePeriod;
	
	public TableIsUpdating(Table table, TimePeriod timePeriod) {
		this.table = table;
		this.rowCount = table.rowCount();
		this.time = System.currentTimeMillis();
		this.timePeriod = timePeriod;
	}

	@Override
	public boolean test() {
		int currentRowCount = table.rowCount();
		long currentTime = System.currentTimeMillis();

		if (currentRowCount != rowCount) {
			rowCount = currentRowCount;
			time = currentTime;
			return false;
		}

		return currentTime - time - timePeriod.getSeconds() * 1000 >= 0;
	}

	@Override
	public String description() {
		return "Table is updating";
	}

}
