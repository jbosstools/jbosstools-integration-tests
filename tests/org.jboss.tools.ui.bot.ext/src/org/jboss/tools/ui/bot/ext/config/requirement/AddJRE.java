package org.jboss.tools.ui.bot.ext.config.requirement;

import static org.junit.Assert.fail;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
/**
 * adds JRE among Installed JRE's
 * @author lzoubek
 *
 */
public class AddJRE extends RequirementBase {

	private final String version;
	/**
	 * requirement for adding JRE
	 * @param version to add
	 */
	public AddJRE(String version) {
		this.version = version;
	}
	@Override
	public boolean checkFullfilled() {
		return SWTTestExt.configuredState.getJreList().contains(getAddedAsName());
	}

	@Override
	public void handle(){
		SWTTestExt.eclipse.addJavaVM(getAddedAsName(), getJavaHome());
		SWTTestExt.configuredState.getJreList().add(getAddedAsName());
	}
	public String getAddedAsName() {
		return "JRE-"+version;
	}
	private String getJavaHome() {
		if ("1.5".equals(version)) {
			return TestConfigurator.getProperty(TestConfigurator.Keys.JAVA_HOME_15);
		}
		if ("1.6".equals(version)) {
			return TestConfigurator.getProperty(TestConfigurator.Keys.JAVA_HOME_16);
		}
		failParsing();
		return null;
	}
	private void failParsing() {
		fail("Unable to add JRE runtime, unsupported verson :"+version);
	}

}
