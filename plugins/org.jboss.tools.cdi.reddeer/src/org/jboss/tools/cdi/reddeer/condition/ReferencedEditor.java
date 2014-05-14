package org.jboss.tools.cdi.reddeer.condition;

import org.eclipse.ui.IWorkbenchPart;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

public class ReferencedEditor extends DefaultEditor{

	protected IWorkbenchPart getReference(){
		return editorPart;
	}
	
}
