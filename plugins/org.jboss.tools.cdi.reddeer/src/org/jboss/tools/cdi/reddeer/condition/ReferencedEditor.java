package org.jboss.tools.cdi.reddeer.condition;

import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.ui.IWorkbenchPart;

public class ReferencedEditor extends DefaultEditor{

	protected IWorkbenchPart getReference(){
		return editorPart;
	}
	
}
