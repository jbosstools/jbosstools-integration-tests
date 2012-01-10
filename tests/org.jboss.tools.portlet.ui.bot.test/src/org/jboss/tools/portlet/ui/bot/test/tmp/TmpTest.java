package org.jboss.tools.portlet.ui.bot.test.tmp;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.internal.plugin.J2EEPlugin;
import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;
import org.osgi.framework.Bundle;

@Require(db=@DB)
public class TmpTest extends SWTTaskBasedTestCase {

	@Test
	public void testTmp() throws InterruptedException{
		@SuppressWarnings("restriction")
		String pluginId = J2EEPlugin.getPlugin().getPluginID();
		System.out.println("Plugin id: " + pluginId);
		Bundle bundle = Platform.getBundle(pluginId);
		
		if (bundle instanceof BundleHost){
			System.out.println("Bundledata class:");
			System.out.println(((BundleHost) bundle).getBundleData().getClass());
		} else {
			System.out.println("No Bundledata");
		}
		System.out.println("Location: " + bundle.getLocation());
		System.out.println("Jar ext: " + IJ2EEModuleConstants.JAR_EXT);
		System.out.println("Comparison: " + bundle.getLocation().endsWith(IJ2EEModuleConstants.JAR_EXT));
		System.out.println();
//		Thread.sleep(60 * 60 * 1000);
	}
}
