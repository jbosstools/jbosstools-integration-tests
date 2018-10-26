/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;

public class TableItemIsFound extends AbstractWaitCondition{
	
	private Table table;
	private String item;
	
	public TableItemIsFound(Table table, String item){
		this.table = table;
		this.item = item;
	}

	@Override
	public boolean test() {
		for(TableItem i: table.getItems()){
			if(i.getText().toLowerCase().contains(item.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		return "Table item "+item+" is in table";
	}

}
