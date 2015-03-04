package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * New Hibernate Mapping File Page
 * Here is summary what hbm.xml file will be created
 * @author jpeterka
 *
 */
public class NewHibernateMappingFilePage extends WizardPage {
	
	/**
	 * Select class for further hbm.xml generation
	 * @param clazz class name
	 */
	public void selectClasses(String clazz) {
		int headerIndex = new DefaultTable().getHeaderIndex("Class name");
		TableItem item = new DefaultTable().getItem(clazz,headerIndex);
		item.select();
	}
}
