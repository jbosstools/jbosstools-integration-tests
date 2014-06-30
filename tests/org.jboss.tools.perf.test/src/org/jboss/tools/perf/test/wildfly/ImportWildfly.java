package org.jboss.tools.perf.test.wildfly;

import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

public class ImportWildfly extends PerfTestBase {

	public ImportWildfly() {
		projectFolder = "wildfly";
	}

	@Test
	public void testImportWildfly() {
		importProject();
	}

}
