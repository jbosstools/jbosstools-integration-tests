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
