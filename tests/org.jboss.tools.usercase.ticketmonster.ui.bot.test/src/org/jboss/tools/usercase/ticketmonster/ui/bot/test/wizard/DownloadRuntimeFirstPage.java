package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class DownloadRuntimeFirstPage extends WizardPage{
	
	public Map<String,String> getDownloadableRuntimes(){
		Table runtimesTable = new DefaultTable();
		Map<String,String> runtimes = new HashMap<String, String>();
		for(int i=0; i<runtimesTable.rowCount();i++){
			runtimes.put(runtimesTable.getItem(i).getText(),runtimesTable.getItem(i).getText(1));	
		}
		return runtimes;
	}
	
	public void selectRuntime(String runtimeName){
		new DefaultTable().select(runtimeName);
	}

}
