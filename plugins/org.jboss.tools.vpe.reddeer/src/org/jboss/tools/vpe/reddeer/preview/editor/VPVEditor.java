/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.preview.editor;

import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Event;
import org.jboss.reddeer.core.handler.BrowserHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.ui.dialogs.FilteredPreferenceDialog;
import org.jboss.tools.vpe.preview.editor.VpvEditorPart;
import org.jboss.tools.vpe.reddeer.VisualEditor;
import org.jboss.tools.vpe.reddeer.resref.core.VpvResourcesDialog;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jboss.tools.common.model.ui.views.palette.IPaletteAdapter;
import org.jboss.tools.jst.reddeer.palette.JQueryMobilePalette;
import org.jboss.tools.jst.reddeer.web.ui.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.PalettePageImpl;
import org.jboss.tools.jst.web.ui.palette.PaletteAdapter;

public class VPVEditor extends VisualEditor{
	
	public VPVEditor() {
		super();
	}
	
	@SuppressWarnings("restriction")
	public String getSelectedTextInBrowser(){
		VpvEditorPart vpvEditor = (VpvEditorPart)editor.getVisualEditor();
		Browser vpvBrowser = getBrowser();
		Long selectedElementId = getSelectedElementId(vpvEditor);
		String pageText = BrowserHandler.getInstance().getText(vpvBrowser);
	    org.jsoup.nodes.Document document = Jsoup.parse(pageText);
	    if(selectedElementId == null){
	    	return null;
	    }
	    Elements selectedElements = document.getElementsByAttributeValue("data-vpvid", selectedElementId.toString());
	    if(selectedElements.size() > 0){
	    	return selectedElements.get(0).text();
	    }
	    return null;
	}
	
	private Long getSelectedElementId(final VpvEditorPart vpvEditor){
		return Display.syncExec(new ResultRunnable<Long>() {

			@Override
			public Long run() {
				return vpvEditor.getVisualEditor().getCurrentSelectedElementId();
			}
		});
	}
	
	private Browser getBrowser(){
		VpvEditorPart vpvEditor = (VpvEditorPart)editor.getVisualEditor();
		return vpvEditor.getVisualEditor().getBrowser();
	}
	
	public Object evaluateScript(final String script){
		return Display.syncExec(new ResultRunnable<Object>() {
			
			@Override
			public Object run() {
				return getBrowser().evaluate(script);
			}
		});
	}
	
	public void clickInBrowser(String text){
		Object[] coord = (Object[])evaluateScript("var elements = document.getElementsByTagName('*'); var i;"
				+ "for(i=0; i<elements.length; i++){ "
		+ "if(elements[i].textContent == '"+text+"'){"
				+ "var coord = new Array(); coord[0]= elements[i].getBoundingClientRect().right-"
				+ "((elements[i].getBoundingClientRect().right-elements[i].getBoundingClientRect().left)/2);"
				+ "coord[1]= elements[i].getBoundingClientRect().top - "
				+ "((elements[i].getBoundingClientRect().top-elements[i].getBoundingClientRect().bottom)/2);"
				+ "return coord}}");
		final Event ev = new Event();
		ev.x=(int)((Double)coord[0]).doubleValue();
		ev.y=(int)((Double)coord[1]).doubleValue();
			
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				getBrowser().notifyListeners(SWT.MouseDown, ev);
				getBrowser().notifyListeners(SWT.MouseUp, ev);
				
			}
		});
	}
	
	public String getBrowserURL(){
		return Display.syncExec(new ResultRunnable<String>() {

			@Override
			public String run() {
				return getBrowser().getUrl();
			}
		});
	}
	
	public VpvEditorPart getVpvEditor(){
		return (VpvEditorPart)editor.getVisualEditor();
	}
	
	public JQueryMobilePalette getPalette(){
		PaletteViewer viewer = Display.syncExec(new ResultRunnable<PaletteViewer>() {
			@Override
			public PaletteViewer run() {
				PalettePage page = (PalettePage)editor.getAdapter(PalettePage.class);
				PalettePageImpl palettePage = (PalettePageImpl)page;
				IPaletteAdapter adapter = palettePage.getAdapter();
				PaletteViewer viewer = ((PaletteAdapter)adapter).getViewer();
				return viewer;
			}
		});
		JQueryMobilePalette jqp = new JQueryMobilePalette(viewer);
		return jqp;
	}
	
	public boolean isBackEnabled(){
		return new DefaultToolItem("Back").isEnabled();
	}
	
	public void back(){
		new DefaultToolItem("Back").click();
	}
	
	public boolean isForwardEnabled(){
		return new DefaultToolItem("Forward").isEnabled();
	}
	
	public void forward(){
		new DefaultToolItem("Forward").click();
	}
	
	public FilteredPreferenceDialog openPreferences(){
		new DefaultToolItem("Preferences").click();
		new DefaultShell(FilteredPreferenceDialog.DIALOG_TITLE);
		return new FilteredPreferenceDialog();
	}
	
	public VpvResourcesDialog openPageDesignOptions(){
		new DefaultToolItem("Page Design Options").click();
		return new VpvResourcesDialog();
	}

}
