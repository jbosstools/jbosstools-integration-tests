package org.jboss.tools.ws.reddeer.ui.preferences;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents dialog for setting up jboss ws runtime
 * 
 * @author jjankovi
 *
 */
public class JBossWSRuntimeListFieldEditor extends DefaultShell {

	public JBossWSRuntimeListFieldEditor() {
		this(false); // default shell is New JBossWS Runtime
	}
	
	public JBossWSRuntimeListFieldEditor(boolean edit) {
		super(edit?"Edit JBossWS Runtime":"New JBossWS Runtime");
	}
	
	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}
	
	public String getName() {
		return new LabeledText("Name:").getText();
	}
	
	public void setVersion(JBossWSRuntimeVersion version) {
		new LabeledCombo("Version:").setSelection(version.version());
	}
	
	public String getVersion() {
		return new LabeledCombo("Version").getSelection();
	}
	
	public void setHomeFolder(String homeFolderLocation) {
		new LabeledText("Home Folder:").setText(homeFolderLocation);
	}
	
	public String getHomeFolder() {
		return new LabeledText("Home Folder:").getText();
	}
	
	public String getRuntimeImplementation() {
		return new DefaultLabel(5).getText();
	}
	
	public String getRuntimeVersion() {
		return new DefaultLabel(7).getText();
	}
	
	public void finish() {
		new PushButton("Finish").click();
	}
	
	public enum JBossWSRuntimeVersion {
		
		RUNTIME20("2.0"), RUNTIME30("3.0"); 
		
		private String version;
		
		private JBossWSRuntimeVersion(String version) {
			this.version = version;
		}
		
		public String version() {
			return version;
		}
		
	}
}