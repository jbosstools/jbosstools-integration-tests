package org.teiid.designer.ui.bot.ext.teiid;

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
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileSelectPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.TeiidView;
import org.jboss.tools.teiid.reddeer.wizard.ConnectionProfileXmlPage;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionProfileWizard;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.teiid.designer.ui.bot.ext.teiid.editor.ModelEditor;
import org.teiid.designer.ui.bot.ext.teiid.preference.DriverDefinitionPreferencePageExt;
import org.teiid.designer.ui.bot.test.Activator;

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

		ConnectionProfileWizard connWizard = new ConnectionProfileWizard();
		connWizard.open();
		connWizard.createFlatFileProfile(flatProfile);
		return flatProfile;
	}
	
	public void createXmlProfile(String name, String path) {
		String xmlProfile = "XML Local File Source";
		if(xmlProfile.startsWith("http")) {
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

		DriverDefinitionPreferencePage prefPage = new DriverDefinitionPreferencePageExt();
		prefPage.open();
		prefPage.addDriverDefinition().create(driverDefinition);
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
	
	public String toAbsolutePath(String path) {
		return ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, path);
	}

}
