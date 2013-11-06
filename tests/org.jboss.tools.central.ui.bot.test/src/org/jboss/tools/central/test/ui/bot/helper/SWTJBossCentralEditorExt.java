package org.jboss.tools.central.test.ui.bot.helper;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.lang.reflect.Field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.jboss.tools.central.editors.JBossCentralEditor;
import org.jboss.tools.central.editors.xpl.TextSearchControl;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotImageHyperlink;

public class SWTJBossCentralEditorExt extends SWTBotEditorExt{
	
	private TextSearchControl searchControl=null;
	private SWTBotImageHyperlink swtBotImageHyperlink=null;

	public SWTJBossCentralEditorExt(final IEditorReference editorReference,
			SWTWorkbenchBot bot) throws WidgetNotFoundException {
		super(editorReference, bot);
		syncExec(new VoidResult() {
			
			@Override
			public void run() {
				JBossCentralEditor centralEditor = (JBossCentralEditor) editorReference.getEditor(true);
				searchControl = getSearchControl(centralEditor);
				swtBotImageHyperlink = getSwtBotImageHyperlink(centralEditor);
			}
		});
	}
	
	public String getSearchText(){
		return syncExec(new StringResult() {
			
			@Override
			public String run() {
				return searchControl.getText();
			}
		});
	}
	
	public void setSearchText(final String text){
		syncExec(new VoidResult() {
			
			@Override
			public void run() {
				searchControl.setText(text);	
			}
		});
	}
	
	public void performSearch(){
		swtBotImageHyperlink.click();
	}
	
	private Composite getSearchControlComposite(JBossCentralEditor editor){
		Composite searchComposite = null;
		try {
			Field searchControlField = JBossCentralEditor.class.getDeclaredField("searchComposite");
			searchControlField.setAccessible(true);
			searchComposite = (Composite) searchControlField.get(editor);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return searchComposite;
	}
	
	private TextSearchControl getSearchControl(JBossCentralEditor editor){
		return (TextSearchControl) getSearchControlComposite(editor).getChildren()[1];
	}
	
	private SWTBotImageHyperlink getSwtBotImageHyperlink(JBossCentralEditor editor){
		return new SWTBotImageHyperlink((ImageHyperlink) getSearchControlComposite(editor).getChildren()[0]);
	}

}
