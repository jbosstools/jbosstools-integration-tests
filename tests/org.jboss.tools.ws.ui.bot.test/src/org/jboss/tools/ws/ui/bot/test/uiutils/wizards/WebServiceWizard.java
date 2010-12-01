/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
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

public class WebServiceWizard extends Wizard {

	public enum Service_Type {
		BOTTOM_UP, TOP_DOWN;
		
		String getDescription() {
			String prefix = "";
			switch (this) {
			case BOTTOM_UP:
				prefix += "Bottom up";
				break;
			case TOP_DOWN:
				prefix += "Top down";
				break;
			default:
				throw new IllegalArgumentException("Unknown type: " + this);
			}
			return prefix + " Java bean Web Service";
		}
	}
	
	public enum Slider_Level {
		TEST, START, INSTALL, DEPLOY, ASSEMBLE, DEVELOP, NO_CLIENT;
		
		@Override
		public String toString() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}
	
	public WebServiceWizard() {
		super();
	}
	
	public WebServiceWizard(Shell shell) {
		super(shell);
	}

	public WebServiceWizard setServiceType(Service_Type type) {
		bot().comboBoxWithLabel("Web service type:").setSelection(type.getDescription());
		return this;
	}
	
	public Service_Type getServiceType() {
		String s = bot().comboBoxWithLabel("Web service type:").getText();
		return s.startsWith("Bottom up") ? Service_Type.BOTTOM_UP : Service_Type.TOP_DOWN;
	}
	
	public WebServiceWizard setServiceSource(String s) {
		SWTBotCombo c = null;
		switch (getServiceType()) {
		case BOTTOM_UP:
			c = bot().comboBoxWithLabel("Service implementation:");
			break;
		case TOP_DOWN:
			c = bot().comboBoxWithLabel("Service definition:");
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + this);
		}
		c.typeText(s);
		return this;
	}
	
	public WebServiceWizard setServerRuntime(String name) {
		findLink("Server runtime:").get(0).click();
		SWTBotShell sh = bot().activeShell();
		SWTBotTree tree = sh.bot().treeInGroup("Server runtime:");
		tree.expandNode("Existing Servers", name).select();
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}

	public WebServiceWizard setWebServiceRuntime(String name) {
		findLink("Web service runtime:").get(0).click();
		SWTBotShell sh = bot().activeShell();
		SWTBotTree tree = sh.bot().treeInGroup("Web service runtime:");
		tree.select(name);
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}

	public WebServiceWizard setServiceProject(String name) {
		findLink("Service project:").get(0).click();
		SWTBotShell sh = bot().activeShell();
		sh.bot().comboBoxWithLabel("Service project:").setSelection(name);
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}

	public WebServiceWizard setServiceEARProject(String name) {
		findLink("Service EAR project:").get(0).click();
		SWTBotShell sh = bot().activeShell();
		sh.bot().comboBoxWithLabel("Service EAR project:").setSelection(name);
		sh.bot().button("OK").click();
		sleep(50);
		return this;
	}
	
	public WebServiceWizard setServiceSlider(Slider_Level level) {
		if (Slider_Level.NO_CLIENT == level) {
			throw new UnsupportedOperationException("Unsupported level: " + level);
		}
		scale(0).setSelection(level.ordinal());
		return this;
	}
	
	public WebServiceWizard setClientSlider(Slider_Level level) {
		scale(1).setSelection(level.ordinal());
		return this;
	}
	
	public boolean isClientEnabled() {
		return scale(1).isEnabled();
	}
	
	//second panel
	public WebServiceWizard setPackageName(String pkg) {
		bot().textWithLabel("Package name").typeText(pkg);
		return this;
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
