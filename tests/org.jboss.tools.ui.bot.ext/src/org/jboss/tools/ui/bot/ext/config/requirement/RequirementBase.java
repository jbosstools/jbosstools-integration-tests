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
	public void fulfill() throws RequirementNotFulfilledException {
		log.info("Fulfilling requirement '"+this.getClass().getName()+"'");
		try {
		for (RequirementBase dep : getDependsOn()) {
			dep.fulfill();
		}
		if (!checkFulfilled()) {
			handle();
			if (!checkFulfilled()) {
				throw new Exception("Requirement implementation error, checkFulfilled() failed after calling handle();");
			}
		} 
		} catch (Exception ex) {
			log.info("Unable to fulfill requirement '"+this.getClass().getName()+"'");
			throw new RequirementNotFulfilledException("Unable to fulfill requirement "+this.getClass().getCanonicalName(),ex);
		}
		log.info("Requirement '"+this.getClass().getName()+"' fulfilled");
		
	}
	/**
	 * must return true if the Requirement is already fulfilled
	 * @return
	 */
	public abstract boolean checkFulfilled();
	/**
	 * handles (should do everything to fulfill requirement), {@link RequirementBase#checkFulfilled()} 
	 * should return true after calling this method 
	 */
	public abstract void handle();
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	@Override
	public boolean equals(Object obj) { 
		return this.getClass().equals(obj.getClass());
	}
}
