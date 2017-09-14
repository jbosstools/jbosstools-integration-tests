/*******************************************************************************
O * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.test.requirement;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.reddeer.eclipse.ui.ide.ExtendedFileEditorsPreferencePage;
import org.eclipse.reddeer.junit.requirement.Requirement;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.jsf.ui.test.requirement.DoNotUseVPERequirement.DoNotUseVPE;

public class DoNotUseVPERequirement implements Requirement<DoNotUseVPE> {

	private WorkbenchPreferenceDialog prefDialog;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	public @interface DoNotUseVPE {

	}

	@Override
	public void fulfill() {
		ExtendedFileEditorsPreferencePage prefPage = openPreferencePage();
		prefPage.selectFileType("*.html");
		prefPage.makeEditorDefault("Text Editor");
		prefPage.selectFileType("*.xhtml");
		prefPage.makeEditorDefault("Text Editor");
		prefDialog.ok();
	}

	private ExtendedFileEditorsPreferencePage openPreferencePage() {
		ExtendedFileEditorsPreferencePage prefPage = new ExtendedFileEditorsPreferencePage(this.prefDialog);
		prefDialog = new WorkbenchPreferenceDialog();
		prefDialog.open();
		prefDialog.select(prefPage);
		return prefPage;
	}

	@Override
	public void setDeclaration(DoNotUseVPE declaration) {
		// nothing to do here.
	}

	@Override
	public void cleanUp() {
		// no need to cleanup
	}

	@Override
	public DoNotUseVPE getDeclaration() {
		return null;
	}

}
