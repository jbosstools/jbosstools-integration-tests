package org.jboss.tools.jbpm.ui.bot.test.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.jbpm.ui.bot.test.suite.PerspectiveRequirement.Perspective;

/**
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
