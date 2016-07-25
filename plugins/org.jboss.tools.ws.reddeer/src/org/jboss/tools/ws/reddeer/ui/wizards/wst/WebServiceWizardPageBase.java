/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.reddeer.ui.wizards.wst;

import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringStartsWith;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.matcher.WithLabelMatcher;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.scale.DefaultScale;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public abstract class WebServiceWizardPageBase extends WizardPage {
    public enum SliderLevel {
		TEST, START, INSTALL, DEPLOY, ASSEMBLE, DEVELOP, NO_CLIENT;

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

	protected abstract String getSourceComboLabel();

	/**
	 * Returns warning/error/info text showed on the top of the page.
	 */
	public String getInfoText() {
		return new DefaultText(0).getText();
	}

	public void setSource(String source) {
		Combo c = new LabeledCombo(getSourceComboLabel());
		c.setText(source);
	}

	public void setServerRuntime(String name) {
		new DefaultHyperlink(
				new WithMnemonicTextMatcher(StringStartsWith.startsWith("Server runtime:"))
				).activate();
		new DefaultTreeItem(new DefaultTree(new DefaultGroup("Server runtime:")), "Existing Servers", name).select();
		new PushButton("OK").click();

		new DefaultShell(new WithMnemonicTextMatcher(StringStartsWith.startsWith("Web Service")));
	}

	public void setWebServiceRuntime(String name) {
		new DefaultHyperlink(
				new WithMnemonicTextMatcher(StringStartsWith.startsWith("Web service runtime:"))
				).activate();
		Group runtimeGroup = new DefaultGroup("Web service runtime:");
		new DefaultTreeItem(new DefaultTree(runtimeGroup), name).select();
		new PushButton("OK").click();

		new DefaultShell(new WithMnemonicTextMatcher(StringStartsWith.startsWith("Web Service")));
	}

	protected void setTargetProject(String label, String name) {
		new DefaultHyperlink(
				new WithMnemonicTextMatcher(StringStartsWith.startsWith(label))
				).activate();
		Combo c = new LabeledCombo(label);

		try {
			c.setSelection(name);
		} catch(CoreLayerException e) {
			c.setText(name);
		}
		new PushButton("OK").click();

		new DefaultShell(new WithMnemonicTextMatcher(StringStartsWith.startsWith("Web Service")));
	}

	/**
	 * Sets <var>level</var>
	 * @param level
	 * @param idx
	 */
	protected void setSlider(SliderLevel level, int idx) {
		new DefaultScale(idx).setSelection(level.ordinal());
	}

	// second panel

	/**
	 * Sets package name.
	 *
	 * @param pkgName name of the package to be set
	 */
	public void setPackageName(String pkgName) {
		//there can be text field or combo box
		try {
			Text pkgText = new DefaultText(new WithLabelMatcher(AnyOf.anyOf(
					Is.is("Package name"),
					Is.is("Package Name:")
					)));
			pkgText.setText(pkgName);
		} catch(CoreLayerException e) {
			DefaultCombo combo = new DefaultCombo(0);
			combo.setSelection("/"+pkgName.replaceAll(".", "/"));
		}
	}

	protected boolean isScaleEnabled(int i) {
		return new DefaultScale(i).isEnabled();
	}
}
