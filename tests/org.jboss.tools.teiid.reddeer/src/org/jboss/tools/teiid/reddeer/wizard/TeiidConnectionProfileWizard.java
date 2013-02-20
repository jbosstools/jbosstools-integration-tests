package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;

public class TeiidConnectionProfileWizard extends ConnectionProfileWizard {

	public TeiidConnectionProfileWizard() {
		super();
		wizardMap.put("HSQLDB", new ConnectionProfileHsqlPage(this, 2));
		wizardMap.put("XML File URL Source", new ConnectionProfileXmlUrlPage(this, 2));
		wizardMap.put("XML Local File Source", new ConnectionProfileXmlLocalPage(this, 2));
	}
}
