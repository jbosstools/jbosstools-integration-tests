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
package org.jboss.tools.ws.ui.bot.test.uiutils.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Scale;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringStartsWith;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.WithLabelMatcher;
import org.jboss.reddeer.swt.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;

public abstract class WsWizardBase extends Wizard {

    public enum Slider_Level {
		TEST, START, INSTALL, DEPLOY, ASSEMBLE, DEVELOP, NO_CLIENT;

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

	protected abstract String getSourceComboLabel();
	
	public void setSource(String source) {
		Combo c = new LabeledCombo(getSourceComboLabel());
		c.setText(source);
	}
	
	public void setServerRuntime(String name) {
		new DefaultHyperlink(
				new WithMnemonicTextMatcher(StringStartsWith.startsWith("Server runtime:"))
				).activate();
		new DefaultTreeItem(new DefaultGroup("Server runtime:"), "Existing Servers", name).select();
		new PushButton("OK").click();
	}

	public void setWebServiceRuntime(String name) {
		new DefaultHyperlink(
				new WithMnemonicTextMatcher(StringStartsWith.startsWith("Web service runtime:"))
				).activate();
		Group runtimeGroup = new DefaultGroup("Web service runtime:");
		new DefaultTreeItem(runtimeGroup, name).select();
		new PushButton("OK").click();
	}

	protected void setTargetProject(String label, String name) {
		new DefaultHyperlink(
				new WithMnemonicTextMatcher(StringStartsWith.startsWith(label))
				).activate();
		Combo c = new LabeledCombo(label);
		//TODO: setText when <var>name</var> is not present in combo
		try {
			c.setSelection(name);
		} catch(SWTLayerException e) {
			c.setText(name);
		}
		new PushButton("OK").click();
	}

	public void setSlider(Slider_Level level, int idx) {
		scale(idx).setSelection(level.ordinal());
	}

	// second panel
	public void setPackageName(String pkg) {
		//there can be text field or combo box
		try {
			Text pkgText = new DefaultText(new WithLabelMatcher(AnyOf.anyOf(
					Is.is("Package name"),
					Is.is("Package Name:")
					)));
			pkgText.setText(pkg);
		} catch(SWTLayerException e) {
			DefaultCombo combo = new DefaultCombo(0);
			combo.setSelection("/"+pkg.replaceAll(".", "/"));
		}
	}

	protected boolean isScaleEnabled(int i) {
		return scale(i).isEnabled();
	}

	private SWTBotScaleExt scale(int i) {
		setFocus();
		List<? extends Scale> widgets = bot().widgets(
				WidgetMatcherFactory.widgetOfType(Scale.class));
		List<SWTBotScaleExt> ret = new ArrayList<SWTBotScaleExt>();
		for (Scale s : widgets) {
			ret.add(new SWTBotScaleExt(s));
		}
		return ret.get(i);
	}
}
