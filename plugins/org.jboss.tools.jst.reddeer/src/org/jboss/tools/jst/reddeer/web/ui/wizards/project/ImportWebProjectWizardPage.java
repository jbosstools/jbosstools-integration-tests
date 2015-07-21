/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
/**
 * Represents JSF Project Import wizard page
 * @author vlado pakan
 */
package org.jboss.tools.jst.reddeer.web.ui.wizards.project;

import org.jboss.reddeer.swt.impl.text.DefaultText;

public class ImportWebProjectWizardPage {
	public void setWebXmlLocation(String location){
		new DefaultText(1).setText(location);
	}
}
