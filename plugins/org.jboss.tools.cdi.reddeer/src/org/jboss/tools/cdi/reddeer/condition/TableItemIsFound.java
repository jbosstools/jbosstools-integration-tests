package org.jboss.tools.cdi.reddeer.condition;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class TableItemIsFound implements WaitCondition{
	
	private Table table;
	private String item;
	
	public TableItemIsFound(Table table, String item){
		this.table = table;
		this.item = item;
	}

	@Override
	public boolean test() {
		for(TableItem i: table.getItems()){
			if(i.getText().contains(item)){
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
