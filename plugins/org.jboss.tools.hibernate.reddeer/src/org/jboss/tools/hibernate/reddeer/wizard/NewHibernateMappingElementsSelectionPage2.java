package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.table.DefaultTable;


/**
 * Hibernate Mapping File Element Selection Page
 * Here user select package or classes for further hbm.xml generation
 * @author jpeterka
 *
 */
public class NewHibernateMappingElementsSelectionPage2 extends WizardPage {

	/**
	 * Select items in the list for hbm.xml generation
	 */
	public void selectItem(String... items) {
		new DefaultTable().select(items);
	}
	
}
