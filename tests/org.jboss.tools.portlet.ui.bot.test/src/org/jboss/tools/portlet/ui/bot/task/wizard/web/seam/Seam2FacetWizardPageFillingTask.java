package org.jboss.tools.portlet.ui.bot.task.wizard.web.seam;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

/**
 * Fills in the Seam configuration on the Seam Facet wizard page. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class Seam2FacetWizardPageFillingTask extends AbstractSWTTask 
	implements WizardPageFillingTask {

	private String runtime;

	private String databaseType;

	private String connectionProfile;

	@Override
	public void perform() {
		if (runtime != null){
			getBot().comboBoxWithLabel("Seam 2 Runtime:").setSelection(runtime);
		}

		if (databaseType != null){
			getBot().comboBoxWithLabel("Database Type:").setSelection(databaseType);
		}

		if (connectionProfile != null){
			getBot().comboBoxWithLabel("Connection profile:").setSelection(connectionProfile);
		}
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public void setConnectionProfile(String connectionProfile) {
		this.connectionProfile = connectionProfile;
	}
}
