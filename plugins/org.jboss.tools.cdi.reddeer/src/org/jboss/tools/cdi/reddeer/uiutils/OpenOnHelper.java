package org.jboss.tools.cdi.reddeer.uiutils;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;

public class OpenOnHelper {
	
	private static final Logger log = Logger.getLogger(OpenOnHelper.class);
	
	public void selectProposal(String className, String toSelect, String proposal){
		TextEditor e = new TextEditor(className+".java");
		e.selectText(toSelect);
		ContentAssistant ca = e.openOpenOnAssistant();
		log.info("Available content assist proposals: "+ca.getProposals());
		ca.chooseProposal(proposal);
	}

}
