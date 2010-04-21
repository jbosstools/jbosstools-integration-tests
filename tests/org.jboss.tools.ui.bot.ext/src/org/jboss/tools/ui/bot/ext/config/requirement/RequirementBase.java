package org.jboss.tools.ui.bot.ext.config.requirement;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
/**
 * An abstract class for all requirements
 * @author lzoubek
 *
 */
public abstract class RequirementBase {
	protected final Logger log = Logger.getLogger(this.getClass());
	public RequirementBase() {	
	}
	
	private List<RequirementBase> dependsOn;
	/**
	 * gets the list of reqs on which this one depends
	 * @return
	 */
	public List<RequirementBase> getDependsOn() {
		if (dependsOn==null) {
			dependsOn = new Vector<RequirementBase>();
		}
		return dependsOn;
	}
	/**
	 * fulfills this requirement. First fulfills the dependent ones, then this.
	 * @throws RequirementNotFulfilledException
	 */
	public void fullfill() throws RequirementNotFulfilledException {
		log.info("Fulfilling requirement '"+this.getClass().getName()+"'");
		try {
		for (RequirementBase dep : getDependsOn()) {
			dep.fullfill();
		}
		if (!checkFullfilled()) {
			handle();
			if (!checkFullfilled()) {
				throw new Exception("Requirement implementation error, checkFullfilled failed after calling handle();");
			}
		} 
		} catch (Exception ex) {
			throw new RequirementNotFulfilledException("Unable to fulfill requirement "+this.getClass().getCanonicalName(),ex);
		}
		log.info("Requirement '"+this.getClass().getName()+"' fulfilled");
		
	}
	/**
	 * must return true if the Requirement is already fulfilled
	 * @return
	 */
	public abstract boolean checkFullfilled();
	/**
	 * handles (should do everything to fulfill requirement), {@link RequirementBase#checkFullfilled()} 
	 * should return true after calling this method 
	 */
	public abstract void handle();
}
