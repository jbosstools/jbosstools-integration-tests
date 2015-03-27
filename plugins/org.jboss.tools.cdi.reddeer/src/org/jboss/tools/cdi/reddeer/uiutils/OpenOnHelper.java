package org.jboss.tools.cdi.reddeer.uiutils;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

public class OpenOnHelper {
	
	public void selectProposal(String className, String toSelect, String proposal){
		TextEditor e = new TextEditor(className+".java");
		e.selectText(toSelect);
		ContentAssistant ca = e.openOpenOnAssistant();
		ca.chooseProposal(proposal);
	}
	
	public List<String> getProposals(String className, String toSelect, String proposal){
		selectProposal(className, toSelect, proposal);
		Shell s = new DefaultShell();
		Table table = new DefaultTable();
		List<String> proposals = new ArrayList<String>();
		for(TableItem i: table.getItems()){
			proposals.add(i.getText());
		}
		s.close();
		return proposals;
	}

}
