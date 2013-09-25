package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Wizard for importing relational model from XML
 * 
 * @author apodhrad
 * 
 */
public class XMLImportWizard extends TeiidImportWizard {

	private boolean isLocal;
	private String name;
	private String profileName;
	private String rootPath;
	private List<String[]> elements;

	public XMLImportWizard() {
		super("File Source (XML) >> Source and View Model");
		elements = new ArrayList<String[]>();
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void addElement(String[] path) {
		elements.add(path);
	}

	public void addElement(String path) {
		elements.add(path.split("/"));
	}

	public void execute() {
		open();
		if (isLocal) {
			new RadioButton("XML file on local file system").click();
		} else {
			new RadioButton("XML file via remote URL").click();
		}

		next();
		new DefaultCombo(0).setSelection(profileName);
		new LabeledText("Name:").setText(name + "Source");

		next();
		new LabeledText("Root Path").setText(rootPath);
		for (String[] path : elements) {
			new DefaultTreeItem(0, path).select();
			new PushButton("Add").click();
		}

		next();
		Bot.get().textWithLabel("Name:").setText(name + "View");
		new LabeledText("New view table name:").setText(name + "Table");

		finish();
	}

	@Override
	public WizardPage getFirstPage() {
		throw new UnsupportedOperationException();
	}

}
