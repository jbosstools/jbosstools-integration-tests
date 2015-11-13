package org.jboss.tools.maven.ui.bot.test.utils;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RepositoryExists{
	
	private String repoID;
	private String settingsPath;
	
	public RepositoryExists(String repoID){
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		MavenUserPreferencePage mu = new MavenUserPreferencePage();
		wd.select(mu);
		settingsPath = mu.getUserSettings();
		wd.ok();
		this.repoID=repoID;
	}

	public boolean test() {
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(settingsPath);
		} catch (Exception e) {
			throw new EclipseLayerException("unable to parse settings.xml file "+settingsPath);
		}
		NodeList nl = doc.getElementsByTagName("repository");
		for(int i=0; i<nl.getLength();i++){
			if(nl.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nl.item(i);
				if(element.getElementsByTagName("id").item(0).getTextContent().equals(repoID)){
					return true;
				}
			}
		}
		return false;
	}
}
