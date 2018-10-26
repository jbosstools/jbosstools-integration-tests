/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.xhtml;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;

public class NewXHTMLTemplatesWizardPage extends WizardPage {
	
	public NewXHTMLTemplatesWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void useXHTMLTeplate(boolean useTeplate){
		new CheckBox("Use XHTML Teplate").toggle(useTeplate);
	}
	
	public void selectTeplate(TableItem teplate){
		teplate.select();
	}
	
	public Table getTeplates(){
		return new DefaultTable();
	}

}
