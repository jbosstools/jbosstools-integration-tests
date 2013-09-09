package org.jboss.tools.teiid.reddeer.wizard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;


/**
 * Teiid connection import wizard
 * @author lfabriko
 *
 */
public class TeiidConnectionImportWizard extends ImportWizardDialog{

	// type of data source to be imported
	public static final String SQLSERVER_TYPE = "SQLSERVER";
	
	/**
	 * Invoke teiid connection importer
	 */
	public TeiidConnectionImportWizard() {
		super("Teiid Designer", "Teiid Connection >> Source Model");
	}
	
	/**
	 * Create new data source of specified type
	 * @param dataSourcename
	 * @param modelName
	 * @param type of data source (e.g. SQLSERVER)
	 * @param properties path to properties file of data source
	 * @param projectName 
	 * @param tables to be imported
	 */
	public void createNewDataSource(String dataSourcename, String modelName, String type, String properties, String projectName, List<String> tables){
		//Create datasource
		new PushButton("New...").click();
		new LabeledText("Name:").setText(dataSourcename);
		Properties props = loadProperties(properties);
		//set driver
		Table table = new DefaultTable(0);
		int originalRowCount = table.rowCount();
		for (int i = 0; i < originalRowCount; i++){
			if (table.getItem(i).getText(0).equals(props.getProperty("db.driver"))){
				table.select(i);
				break;
			}
		}
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		if (type.equals(SQLSERVER_TYPE)){
			addDataSourceSQLServer(props);
		}
		new PushButton("OK").click();		
		new PushButton("&Next >").click();
		
		//Select (the translator) and target model for the import
		new LabeledText("Name:").setText(modelName);
		new PushButton("&Next >").click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		
		//Get DDL for the Import
		new PushButton("&Next >").click();
		
		//Review Model Updates
		new PushButton("Unselect All").click();
		//choose tables to import
		checkTablesToImport(projectName, modelName, tables);
		new PushButton("Finish").click();
	}
	

	/**
	 * Add data source of type: SQL Server
	 * @param props
	 */
	public void addDataSourceSQLServer(Properties props) {		
		//set connection info
		Table table = new DefaultTable(1);
		
		for (int i = 0; i < table.rowCount(); i++){
			//connection url
			if (table.getItem(i).getText(0).equals("* connection-url")){
				table.select(i);
				SWTBotTable t = Bot.get().table(1);
				t.doubleClick(i, 1);
				String url = "jdbc:sqlserver://"+props.getProperty("db.hostname")
						+":1433;databasename="+props.getProperty("db.name");
				Bot.get().text(1).setText(url);			
			}
			//username
			if (table.getItem(i).getText(0).equals("user-name")){
				table.select(i);
				SWTBotTable t = Bot.get().table(1);
				t.doubleClick(i, 1);
				Bot.get().text(1).setText(props.getProperty("db.username"));
			}
			//password
			if (table.getItem(i).getText(0).equals("password")){
				table.select(i);
				SWTBotTable t = Bot.get().table(1);
				t.doubleClick(i, 1);
				Bot.get().text(1).setText(props.getProperty("db.password"));
				
				//click somewhere else
				table.select(i+1);
			}
		}
	}
	
	/**
	 * 
	 * @param properties path to file with properties of data source
	 * @return properties of data source
	 */
	public Properties loadProperties(String properties){
		// load properties
		Properties props = new Properties();
		try {
			props.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	/**
	 * Review Updated Model - select the tables to be imported
	 * @param projectName
	 * @param modelName
	 * @param tables to be imported
	 */
	public void checkTablesToImport(String projectName, String modelName, List<String> tables){
		String wholeModelName = "file:/" + projectName + "/" + modelName + ".xmi";
		for (String table : tables){
			Bot.get().tree().expandNode(wholeModelName, table).check();
		}
	}

}