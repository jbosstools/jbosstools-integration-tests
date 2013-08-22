package org.jboss.tools.switchyard.ui.bot.test.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;

/**
 * Perspective which ensures that a given perspective will be opened.
 * 
 * @author apodhrad
 * 
 */
public class PerspectiveRequirement implements Requirement<Perspective> {

	public static final String PROPERTIES_FILE = "swtbot.test.properties.file";

	private Perspective perspective;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Perspective {

		String name();
	}

	public PerspectiveRequirement() {
		closeWelcome();
	}

	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		new LabeledPerspective(perspective.name()).open();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@Override
	public void setDeclaration(Perspective perspective) {
		this.perspective = perspective;
	}

	private void closeWelcome() {
		try {
			Bot.get().viewByTitle("Welcome").close();
		} catch (Exception ex) {
			// ok
		}
	}

	private class LabeledPerspective extends AbstractPerspective {

		public LabeledPerspective(String label) {
			super(label);
		}

	}

}
