package org.jboss.tools.ui.bot.ext.parts;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.editor.ObjectMultiPageEditor;

/**
 * 
 * @author jpeterka
 *
 */
public class ObjectMultiPageEditorBot {
	
	String partName;
	IEditorReference ref;
	
	public ObjectMultiPageEditorBot(String title) {
		ref = getEditorReference(title);
	}
		
	private IEditorReference getEditorReference(final String title) {		
		// Search for editor
		IEditorReference ref = UIThreadRunnable.syncExec(new Result<IEditorReference>() {
			public IEditorReference run() {
				IEditorReference ref = null;
				IEditorReference[] editorReferences = null;
				editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
				
				try {
					for (IEditorReference reference: editorReferences) {
						IEditorInput input = reference.getEditorInput();
						String name = input.getName();
						if (name.equals(title)) {
							return reference;
						}
					}
				} catch (PartInitException ex) {
					fail(ex.toString());
				}
				return ref;				
			}
		});
		
		assertNotNull(ref);
		return ref;
	}
	
	public void selectPage(final String pageName) {
		//assertTrue(ref.getPart(true) instanceof Hibernate3CompoundEditor);
		assertTrue(ref.getPart(true) instanceof ObjectMultiPageEditor);
		
		//final Hibernate3CompoundEditor editor = (Hibernate3CompoundEditor)ref.getPart(true);		
		final ObjectMultiPageEditor editor = (ObjectMultiPageEditor)ref.getPart(true);
		
		// Select page
		Display.getDefault().syncExec(new Runnable() {
			
			public void run() {
				editor.selectPageByName(pageName);
			}
		});		
	}
}

	