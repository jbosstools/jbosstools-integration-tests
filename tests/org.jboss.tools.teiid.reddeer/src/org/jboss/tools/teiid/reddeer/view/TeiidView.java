package org.jboss.tools.teiid.reddeer.view;

import org.jboss.reddeer.workbench.view.impl.WorkbenchView;

/**
 * This class represents a teiid view
 * 
 * @author apodhrad
 *
 */
public class TeiidView extends WorkbenchView {

	public static final String CATEGORY = "Teiid Designer";
	public static final String VIEW_TITLE = "Teiid";
	
	public TeiidView() {
		super(CATEGORY, VIEW_TITLE);
	}

}
