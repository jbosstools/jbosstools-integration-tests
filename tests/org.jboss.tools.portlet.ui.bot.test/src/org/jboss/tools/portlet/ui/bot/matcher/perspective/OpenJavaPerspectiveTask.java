package org.jboss.tools.portlet.ui.bot.matcher.perspective;

import org.jboss.tools.ui.bot.ext.gen.ActionItem;


/**
 * Opens Java perspective.
 * 
 * @author Lucia Jelinkova
 *
 */
public class OpenJavaPerspectiveTask extends OpenPerspectiveTask {

	public OpenJavaPerspectiveTask() {
		super(ActionItem.Perspective.JAVA.LABEL);
	}
}
