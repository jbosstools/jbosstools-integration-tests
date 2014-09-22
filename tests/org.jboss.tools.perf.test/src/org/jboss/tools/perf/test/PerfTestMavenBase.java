package org.jboss.tools.perf.test;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.junit.After;

public class PerfTestMavenBase extends PerfTestBase{
	
	@After
	public void delete(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.deleteAllProjects(false);
	}

}
