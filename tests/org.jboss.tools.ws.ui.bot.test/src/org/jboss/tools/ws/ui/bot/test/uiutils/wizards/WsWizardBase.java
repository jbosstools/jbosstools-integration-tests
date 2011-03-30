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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.jboss.tools.ui.bot.ext.parts.SWTBotHyperlinkExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;

public abstract class WsWizardBase extends Wizard {

	public enum Slider_Level {
		TEST, START, INSTALL, DEPLOY, ASSEMBLE, DEVELOP, NO_CLIENT;
		
		@Override
		public String toString() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}
	
	public WsWizardBase() {
		super();
	}
	
	public WsWizardBase(Shell shell) {
		super(shell);
	}

	public WsWizardBase setSource(String s) {
		SWTBotCombo c = bot().comboBoxWithLabel(getSourceComboLabel());
		c.typeText(s);
		return this;
	}

	public WsWizardBase setServerRuntime(String name) {
		setServerRuntime(name, 0);
		return this;
	}

	public WsWizardBase setWebServiceRuntime(String name) {
		setWebServiceRuntime(name, 0);
		return this;
	}

	protected abstract String getSourceComboLabel();
	
	protected WsWizardBase setServerRuntime(String name, int idx) {
		findLink("Server runtime:").get(idx).click();
		SWTBotShell sh = bot().activeShell();
		SWTBotTree tree = sh.bot().treeInGroup("Server runtime:");
		tree.expandNode("Existing Servers", name).select();
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}

	protected WsWizardBase setWebServiceRuntime(String name, int idx) {
		findLink("Web service runtime:").get(idx).click();
		sleep(100);
		SWTBotShell sh = bot().activeShell();
		sleep(100);
		SWTBotTree tree = sh.bot().treeInGroup("Web service runtime:");
		tree.select(name);
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}

	protected WsWizardBase setTargetProject(String label, String name) {
		findLink(label).get(0).click();
		SWTBotShell sh = bot().activeShell();
		sh.bot().comboBoxWithLabel(label).setSelection(name);
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}

	public WsWizardBase setSlider(Slider_Level level, int idx) {
		scale(idx).setSelection(level.ordinal());
		return this;
	}
	
	//second panel
	public WsWizardBase setPackageName(String pkg) {
		bot().textWithLabel("Package name").typeText(pkg);
		return this;
	}


	protected boolean isScaleEnabled(int i) {
		return scale(i).isEnabled();
	}
	
	private List<SWTBotHyperlinkExt> findLink(String text) {
		List<? extends Hyperlink> widgets = bot().widgets(WidgetMatcherFactory.widgetOfType(Hyperlink.class));
		List<SWTBotHyperlinkExt> ret = new ArrayList<SWTBotHyperlinkExt>();
		for (Hyperlink h: widgets) {
			if (h.getText().indexOf(text) > -1) {
				ret.add(new SWTBotHyperlinkExt(h));
			}
		}
		return ret;
	}

	private SWTBotScaleExt scale(int i) {
		List<? extends Scale> widgets = bot().widgets(WidgetMatcherFactory.widgetOfType(Scale.class));
		List<SWTBotScaleExt> ret = new ArrayList<SWTBotScaleExt>();
		for (Scale s: widgets) {
				ret.add(new SWTBotScaleExt(s));
		}
		return ret.get(i);
	}
}
