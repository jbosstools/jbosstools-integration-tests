package org.jboss.tools.maven.reddeer.project.examples.wait;

import org.jboss.reddeer.swt.api.Link;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.link.DefaultLink;

/**
 * Waits for link with information about RH enterprise maven repo not being in settings.xml.
 * 
 * @author rhopp
 *
 */
public class MavenRepositoryNotFound extends AbstractWaitCondition{
	
	@Override
	public String description() {
		return "Waiting for maven repository to be found";
	}
	
	@Override
	public boolean test() {
		Link link = null;
		try{
			link = new DefaultLink();
			return link.getText().startsWith("This project has a dependency");
		}catch(SWTLayerException ex){
			return false;
		}
	}
}