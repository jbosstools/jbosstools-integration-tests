package org.jboss.tools.maven.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

public class MavenProjectWizardSecondPage extends WizardPage{
	
	public void selectArchetype(String catalog, String archetype){
		new DefaultCombo(0).setSelection(catalog);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new WaitUntil(new TableHasRows(new DefaultTable()),TimePeriod.LONG);
		// TODO: RedDeer has to be enhanced to find item by text of other rows then just first one
		new DefaultTable().getItem(archetype,1).select();
	}

}
