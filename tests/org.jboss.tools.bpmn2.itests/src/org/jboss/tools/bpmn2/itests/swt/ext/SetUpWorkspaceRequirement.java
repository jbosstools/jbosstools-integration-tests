package org.jboss.tools.bpmn2.itests.swt.ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.util.Bot;

import org.jboss.tools.bpmn2.itests.swt.ext.SetUpWorkspaceRequirement.SetUpWorkspace;

/**
 * This requirement ensures, that all projects are deleted from workspace (aka.
 * workspace is clean)
 * 
 * @author rhopp
 * 
 */

public class SetUpWorkspaceRequirement implements Requirement<SetUpWorkspace> {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SetUpWorkspace {
		
	}

	/**
	 * This should be possible every time.
	 */
	public boolean canFulfill() {
		return true;
	}

	/**
	 * Deletes all projects from workspace
	 */
	public void fulfill() {
		closeWelcomeScreen();
		closeAllEditors();
	}

	public void setDeclaration(SetUpWorkspace declaration) {
		// Nothing to do here
	}
	
	private void closeWelcomeScreen() {
		try {
			Bot.get().viewByTitle("Welcome").close();
		} catch (Exception ex) {
			// Ignore
		}
	}
	
	private void closeAllEditors() {
		for (SWTBotEditor e : Bot.get().editors()) {
			if (e.isDirty()) {
				e.save();
			}
			e.close();
		}
	}

}
