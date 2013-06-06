package org.jboss.tools.teiid.ui.bot.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.eclipse.datatools.ui.DatabaseProfile;
import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileDatabasePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileFlatFilePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionWizard;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.preference.DriverDefinitionPreferencePageExt;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.TeiidView;
import org.jboss.tools.teiid.reddeer.wizard.ConnectionProfileXmlPage;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBDriverWizard;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionProfileWizard;

/**
 * Bot operations specific for Teiid Designer.
 * 
 * @author apodhrad
 * 
 */
public class TeiidBot {

	public TeiidView showTeiidView() {
		TeiidView teiidView = new TeiidView();
		teiidView.open();
		return teiidView;
	}

	public ModelProject createModelProject(String name) {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		return modelExplorer.createModelProject(name);
	}

	public ModelExplorer modelExplorer() {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		return modelExplorer;
	}

	public ModelEditor modelEditor(String title) {
		SWTBotEditor editor = Bot.get().editorByTitle(title);
		ModelEditor modelEditor = new ModelEditor(editor.getReference(), Bot.get());
		return modelEditor;
	}

	public FlatFileProfile createFlatFileProfile(String name, String folder) {
		FlatFileProfile flatProfile = new FlatFileProfile();
		flatProfile.setName(name);
		flatProfile.setFolder(folder);
		flatProfile.setCharset("UTF-8");
		flatProfile.setStyle("CSV");

		ConnectionProfileWizard connWizard = new ConnectionProfileWizardExt();
		connWizard.open();
		connWizard.createFlatFileProfile(flatProfile);
		return flatProfile;
	}

	public void createXmlProfile(String name, String path) {
		String xmlProfile = "XML Local File Source";
		if (xmlProfile.startsWith("http")) {
			xmlProfile = "XML File URL Source";
		}

		TeiidConnectionProfileWizard wizard = new TeiidConnectionProfileWizard();
		wizard.open();

		ConnectionProfileSelectPage selectPage = wizard.getFirstPage();
		selectPage.setConnectionProfile(xmlProfile);
		selectPage.setName(name);

		wizard.next();

		ConnectionProfileXmlPage xmlPage = (ConnectionProfileXmlPage) wizard.getSecondPage();
		xmlPage.setPath(toAbsolutePath(path));

		wizard.finish();
	}


	public void createDatabaseProfile(String name, String fileName) {
		Properties props = new Properties();
		try {
			props.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		createDatabaseProfile(name, props);

	}

	public void createDatabaseProfile(String name, Properties props) {
		DriverTemplate drvTemp = new DriverTemplate(props.getProperty("db.template"),
				props.getProperty("db.version"));

		DriverDefinition driverDefinition = new DriverDefinition();
		driverDefinition.setDriverName(name + "Driver");
		driverDefinition.setDriverTemplate(drvTemp);
		String driverPath = new File(props.getProperty("db.jdbc_path")).getAbsolutePath();
		driverDefinition.setDriverLibrary(driverPath);

		DriverDefinitionPreferencePageExt prefPage = new DriverDefinitionPreferencePageExt();
		prefPage.open();
		prefPage.addDriverDefinition(driverDefinition);
		prefPage.ok();

		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setName(name);
		dbProfile.setDatabase(props.getProperty("db.name"));
		dbProfile.setDriverDefinition(driverDefinition);
		dbProfile.setHostname(props.getProperty("db.hostname"));
		dbProfile.setUsername(props.getProperty("db.username"));
		dbProfile.setPassword(props.getProperty("db.password"));
		dbProfile.setVendor(props.getProperty("db.vendor"));
		dbProfile.setPort(props.getProperty("db.port"));

		TeiidConnectionProfileWizard wizard = new TeiidConnectionProfileWizard();
		wizard.open();
		wizard.createDatabaseProfile(dbProfile);
	}
	
	/**
	 * Create connection profile to HSQL database
	 * @param properties path to properties file (e.g. resources/db/mydb.properties)
	 * @param jdbcProfile name of profile (e.g. HSQLDB Profile)
	 * @param addDriver true if driver should be added
	 * @param setLocksFalse true if locks on database shouldn't be created
	 */
	public void createHsqlProfile(String properties, String jdbcProfile, boolean addDriver, boolean setLocksFalse){
		//load properties
		Properties props = new Properties();
		try {
			props.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if (addDriver) {
			// create new generic driver for HSQL
			new HSQLDBDriverWizard(props.getProperty("db.jdbc_path")).create();
		}
		//create new connection profile to HSQLDB 
		new HSQLDBProfileWizard("HSQLDB Driver", props.getProperty("db.name"),
				props.getProperty("db.hostname")).setUser(props.getProperty("db.username"))
				.setPassword(props.getProperty("db.password")).setName(jdbcProfile).create(setLocksFalse);
	}
	
	

	public String toAbsolutePath(String path) {
		return new File(path).getAbsolutePath();
	}

	private class ConnectionProfileWizardExt extends ConnectionProfileWizard {

		@Override
		public void createFlatFileProfile(FlatFileProfile flatProfile) {
			ConnectionProfileSelectPage selectPage = getFirstPage();
			selectPage.setConnectionProfile("Flat File Data Source");
			selectPage.setName(flatProfile.getName());

			next();

			ConnectionProfileFlatFilePage flatPage = (ConnectionProfileFlatFilePage) getSecondPage();

			// TODO: LabeledText
			// flatPage.setHomeFolder(flatProfile.getFolder());
			Bot.get().text().setText(flatProfile.getFolder());
			flatPage.setCharset(flatProfile.getCharset());
			flatPage.setStyle(flatProfile.getStyle());

			finish();
		}

	}

}
