package org.jboss.tools.runtime.as.ui.bot.test.parametized.seam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.Activator;
import org.jboss.tools.runtime.as.ui.bot.test.SuiteConstants;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.MatrixUtils;
import org.jboss.tools.runtime.as.ui.bot.test.reddeer.Runtime;

public class SeamRuntimeUIConstants {

	/*
	 * DL RT Strings
	 */

	public static final String SEAM_202 = "JBoss Seam 2.0.2.SP1";
	public static final String SEAM_212 = "JBoss Seam 2.1.2.SP1";
	public static final String SEAM_222 = "JBoss Seam 2.2.2";
	public static final String SEAM_230 = "JBoss Seam 2.3.0";

	
	public static final String[] LATEST_MAJORS_FREE_DOWNLOADS = new String[] {
			SEAM_202, SEAM_212, SEAM_222, SEAM_230};
	
	public static final String[] ALL_DOWNLOADS = LATEST_MAJORS_FREE_DOWNLOADS;

	public static final String[] SMOKETEST_DOWNLOADS = new String[] {SEAM_230};

	private static HashMap<String, List<Runtime>> expectations = null;

	private static void initialize() {
		HashMap<String, List<Runtime>> map = new HashMap<String, List<Runtime>>();
		map.put(SEAM_202, asList(createRuntime(SEAM_202, "Seam 2.0.2", "2.0", "Seam", "jboss-seam-2.0.2.Final")));
		map.put(SEAM_212, asList(createRuntime(SEAM_212, "Seam 2.1.2", "2.1", "Seam", "jboss-seam-2.1.2.Final")));
		map.put(SEAM_222, asList(createRuntime(SEAM_222, "Seam 2.2.2", "2.2", "Seam", "jboss-seam-2.2.2.Final")));
		map.put(SEAM_230, asList(createRuntime(SEAM_230, "Seam 2.3.0", "2.3", "Seam", "jboss-seam-2.3.0.Final")));

		expectations = map;
	}

	private static List<Runtime> asList(Runtime r) {
		return Arrays.asList(new Runtime[] { r });
	}


	private static Runtime createRuntime(String key, String name, String version, String type, String suffix) {
		return new Runtime(name, version, type,
				Activator.getDownloadPath(key).append(suffix).toOSString());
	}

	public static List<Runtime> getRuntimesForDownloadable(String dlRuntimeString) {
		if (expectations == null) {
			initialize();
		}
		return expectations.get(dlRuntimeString);
	}

	
    public static Collection<Object[]> getParametersForScope(String scope){
    	// Seam downloads are all free
    	Object[] free = new Object[]{SuiteConstants.FREE};
    	ArrayList<Object[]> ret;
    	if( SuiteConstants.SCOPE_MAJORS.equals(scope)) { 
    		// latest from majors versions only
    		ret = MatrixUtils.toMatrix(new Object[][]{SeamRuntimeUIConstants.LATEST_MAJORS_FREE_DOWNLOADS, free});
    	} else if( SuiteConstants.SCOPE_FREE.equals(scope)) {
    		// ALL free
    		ret = MatrixUtils.toMatrix(new Object[][]{SeamRuntimeUIConstants.ALL_DOWNLOADS, free});
    	} else if( SuiteConstants.SCOPE_ALL.equals(scope)) {
    		// All
			ret = MatrixUtils.toMatrix(new Object[][] { SeamRuntimeUIConstants.ALL_DOWNLOADS, free});
    	} else {
        	// If smoke test
    		ret = MatrixUtils.toMatrix(new Object[][]{SeamRuntimeUIConstants.SMOKETEST_DOWNLOADS, free});
    	}
    	return ret;
    }
	
}
