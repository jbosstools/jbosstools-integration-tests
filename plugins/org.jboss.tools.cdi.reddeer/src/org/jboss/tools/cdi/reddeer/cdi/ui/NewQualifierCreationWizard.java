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
package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class NewQualifierCreationWizard extends NewMenuWizard{
	
	public static final String NAME="Qualifier Annotation";
	public static final String SHELL_TEXT = "New Qualifier";
	
	public NewQualifierCreationWizard(){
		super(SHELL_TEXT, CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setPackage(String packageName){
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public void setInherited(boolean inherited){
		new CheckBox("Add @Inherited").toggle(inherited);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
}
