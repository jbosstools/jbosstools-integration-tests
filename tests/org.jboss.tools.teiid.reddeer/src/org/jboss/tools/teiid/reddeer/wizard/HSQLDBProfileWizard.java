package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;

import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class HSQLDBProfileWizard extends ConnectionProfileWizard {

	public static final String DEFAULT_NAME = "HSQLDB Profile";

	private String name;
	private String db;
	private String folder;
	private String driver;
	private String user;
	private String password;

	public HSQLDBProfileWizard(String driver, String db, String folder) {
		super();
		this.name = DEFAULT_NAME;
		this.driver = driver;
		this.db = db;
		this.folder = new File(folder).getAbsolutePath();
	}

	public HSQLDBProfileWizard setName(String name) {
		this.name = name;
		return this;
	}

	public HSQLDBProfileWizard setUser(String user) {
		this.user = user;
		return this;
	}

	public HSQLDBProfileWizard setPassword(String password) {
		this.password = password;
		return this;
	}

	public void create() {
		open();
		new DefaultTable().select("Generic JDBC");
		new LabeledText("Name:").setText(name);
		next();
		new DefaultCombo("Drivers:").setSelection(driver);
		new LabeledText("Database:").setText(db);
		new LabeledText("URL:").setText("jdbc:hsqldb:" + folder + "/" + db);
		if (user != null) {
			new LabeledText("User name:").setText(user);
		}
		if (password != null) {
			new LabeledText("Password:").setText(password);
		}
		new CheckBox("Save password").click();
		finish();
	}

}
