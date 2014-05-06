package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
/**
 * Create a New Server Adapter wizard page<br/>
 * 
 * Represents JBoss Runtime page displayed when adding JBoss Runtime via New Server dialog.<br/>
 * 
 * It's the next page displayed after invoking next page from New Server page,
 * when any JBoss Runtime had been selected.<br/><br/>
 * 
 * @author Radoslav Rabara
 * @since JBoss Tools 4.2.0.Beta1
 */
public class NewServerAdapterPage extends WizardPage {
	
	public void setProfile(Profile profile) {
		getProfileCombo().setText(profile.getLabel());
	}
	
	public Profile getProfile() {
		String profileLabel = getProfileCombo().getText();
		for(Profile profile : Profile.values()) {
			if(profile.getLabel().equals(profileLabel)) {
				return profile;
			}
		}
		throw new AssertionError("Unrecognized profile: " + profileLabel);
	}
	
	private Combo getProfileCombo() {
		return new DefaultCombo(0);
	}
	
	public void setAssignRuntime(boolean assign) {
		CheckBox check = new CheckBox();
		if(check.isChecked() != assign) {
			check.click();
		}
	}
	
	private static final String NEW_RUNTIME_LABEL = "Create new runtime (next page)";
	
	public List<String> getRuntimes() {
		List<String> items = new LinkedList<String>(getRuntimeCombo().getItems());
		items.remove(NEW_RUNTIME_LABEL);
		return items;
	}	
	
	/**
	 * Sets the specified <var>runtime</var> as runtime required
	 * by the selected profile.
	 * 
	 * If <code>null</code> is given than a new runtime will be used.
	 */
	public void setRuntime(String runtime) {
		Combo combo = getRuntimeCombo();
		if(runtime == null) {
			combo.setText(NEW_RUNTIME_LABEL);
		} else {
			combo.setText(runtime);
		}
	}
	
	public String getRuntime() {
		String runtime = getRuntimeCombo().getSelection();
		if(runtime.equals(NEW_RUNTIME_LABEL)) {
			return null;
		}
		return runtime;
	}
	
	private Combo getRuntimeCombo() {
		return new DefaultCombo(0);
	}
	
	public enum Profile {
		LOCAL("Local"), REMOTE("Remote");
		//TODO: Add profiles preferring management operations
		
		private String label;
		
		Profile(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
	}

	public void checkErrors() {
		List<String> runtimes = getRuntimes();
		
		boolean anotherServerWithSameType = runtimes.size() > 0;		
		if(anotherServerWithSameType) {
			throw new AssertionError("There is another server with the same type.\n"
					+ "Present server: " + Arrays.toString(runtimes.toArray()));
		}
	}
}
