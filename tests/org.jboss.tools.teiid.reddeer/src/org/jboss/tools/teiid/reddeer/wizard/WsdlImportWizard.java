package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.teiid.reddeer.condition.IsItemAdded;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlImportWizard extends TeiidImportWizard {

	public static final String IMPORTER = "WSDL File or URL >> Source and View Model (SOAP)";

	private String profile;
	private List<String> requestElements;
	private List<String> responseElements;

	public WsdlImportWizard() {
		super(IMPORTER);
		requestElements = new ArrayList<String>();
		responseElements = new ArrayList<String>();
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void addRequestElement(String path) {
		requestElements.add(path);
	}

	public void addResponseElement(String path) {
		responseElements.add(path);
	}

	@Override
	public void open() {
		log.info("Open " + IMPORTER);
		new ShellMenu(getMenuPath()).select();
		new DefaultShell(getDialogTitle());
		log.info("Select " + IMPORTER);
		new DefaultTreeItem("Teiid Designer", IMPORTER).select();
		stupidWait();
		next();
		stupidWait();
	}

	@Override
	public void execute() {
		open();
		new DefaultCombo(0).setSelection(profile);
		next();
		stupidWait();
		next();
		stupidWait();

		// Add request elements
		for (String path : requestElements) {
			addElement("Request", path);
		}
		// Add response elements
		for (String path : responseElements) {
			addElement("Response", path);
		}

		finish();
	}

	private void addElement(String tab, String path) {
		log.info("Add " + tab + " element '" + path + "'");
		new DefaultTabItem(tab).activate();
		// TODO: DefaultTreeItem
		try {
			new DefaultTreeItem(1, path.split("/")).select();
		} catch (Exception e) {
		}
		try {
			new DefaultTreeItem(3, path.split("/")).select();
		} catch (Exception e) {
		}
		new PushButton("Add").click();
		String lastItem = getLastItem(path);
		new WaitUntil(new IsItemAdded(lastItem, tab), TimePeriod.NORMAL);
	}

	private String getLastItem(String path) {
		String[] items = path.split("/");
		return items[items.length - 1];
	}

	// The are some problems on Win7_32. We need to wait due to possible WSDL
	// reading (not sure).
	private void stupidWait() {
		long time = 10 * 1000;
		log.info("Stupid waiting for " + time + " ms");
		Bot.get().sleep(time);
	}
}
