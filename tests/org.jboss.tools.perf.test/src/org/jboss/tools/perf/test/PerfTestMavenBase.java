package org.jboss.tools.perf.test;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.junit.After;

public class PerfTestMavenBase extends PerfTestBase{
	
	@After
	public void delete(){
		PackageExplorerPart pe = new PackageExplorerPart();
		pe.open();
		pe.deleteAllProjects(false);
	}

}
