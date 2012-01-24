package org.jboss.tools.portlet.ui.bot.task.importing.project;

import org.jboss.tools.portlet.ui.bot.task.importing.ImportingTask;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

/**
 * Loads dialog for importing existing project into the workspace. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingProjectImportTask extends ImportingTask {

	public ExistingProjectImportTask() {
		super(ActionItem.Import.GeneralExistingProjectsintoWorkspace.LABEL.getGroupPath().get(0), ActionItem.Import.GeneralExistingProjectsintoWorkspace.LABEL.getName());
	}
}
