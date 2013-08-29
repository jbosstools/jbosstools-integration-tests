package org.jboss.tools.switchyard.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Wizard for creating a SwitchYard project.
 * 
 * @author apodhrad
 * 
 */
public class SwitchYardProjectWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New SwitchYard Project";
	public static final String DEFAULT_VERSION = "0.8.0.Final";

	private String name;
	private String groupId;
	private String packageName;
	private String version;
	private List<String[]> components;

	public SwitchYardProjectWizard(String name) {
		super("SwitchYard", "SwitchYard Project");
		this.name = name;
		// this.version = DEFAULT_VERSION;
		components = new ArrayList<String[]>();
	}

	public SwitchYardProjectWizard groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public SwitchYardProjectWizard packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public SwitchYardProjectWizard impl(String... component) {
		for (int i = 0; i < component.length; i++) {
			components.add(new String[] { "Implementation Support", component[i] });
		}
		return this;
	}

	public SwitchYardProjectWizard binding(String... component) {
		for (int i = 0; i < component.length; i++) {
			components.add(new String[] { "Gateway Bindings", component[i] });
		}
		return this;
	}

	public SwitchYardProjectWizard version(String version) {
		this.version = version;
		return this;
	}
	
	public SwitchYardProjectWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

	public void create() {
		open();
		setText("Project name:", name);
		next();
		activate();
		setText("Group Id:", groupId);
		setText("Package Name:", packageName);
		setVersion(version);
		selectComponents(components);
		finish();
	}

	@Override
	public void finish() {
		log.info("Finish wizard");

		Button button = new PushButton("Finish");
		checkButtonEnabled(button);
		button.click();

		TimePeriod timeout = TimePeriod.getCustom(20 * 60 * 1000);
		new WaitWhile(new ShellWithTextIsActive(DIALOG_TITLE), timeout);
		new WaitWhile(new JobIsRunning(), timeout);
	}

	private void setText(String label, String value) {
		if (value != null) {
			new LabeledText(label).setText(value);
		}
	}

	private void setVersion(String version) {
		Combo combo = new DefaultCombo("Runtime Version:");
		if (version != null && !combo.getSelection().equals(version)) {
			combo.setSelection(version);
		}
	}

	private void selectComponents(List<String[]> components) {
		for (String[] component : components) {
			new DefaultTreeItem(component).setChecked(true);
		}
	}
}
