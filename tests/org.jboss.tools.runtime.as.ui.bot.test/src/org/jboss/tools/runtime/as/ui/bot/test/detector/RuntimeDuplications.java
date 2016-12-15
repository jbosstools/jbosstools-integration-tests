package org.jboss.tools.runtime.as.ui.bot.test.detector;

import org.jboss.tools.runtime.as.ui.bot.test.template.RuntimeDetectionUtility;

/**
 * Tests the case when the runtime path contains two identical server runtimes with Seam.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class RuntimeDuplications extends RuntimeDetectionUtility {
//
//	private File tmpServerPath;
//	
//	private File tmpServerAPath;
//	
//	private File tmpServerBPath;
//	
//	private SearchingForRuntimesDialog searchingForRuntimesDialog;
//	
//	@Before
//	public void prepareServers() throws IOException{
//		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
//		tmpServerPath = new File(tmpDir, "tmpServerCopy_" + System.currentTimeMillis());
//		tmpServerAPath = new File(tmpServerPath, "serverA/jboss-eap-5.2");
//		tmpServerBPath = new File(tmpServerPath, "serverB/jboss-eap-5.2");
//		
//		File server = new File(RuntimeProperties.getInstance().getRuntimePath(DetectEAP52.SERVER_ID));
//		FileUtil.copyDir(server, tmpServerAPath, true, true, true);
//		FileUtil.copyDir(server, tmpServerBPath, true, true, true);
//	}
//	
//	@Test
//	public void duplicateRuntimes(){
//		searchingForRuntimesDialog = addPath(tmpServerPath.getAbsolutePath());
//		assertFoundRuntimesNumber(4);
//		
//		searchingForRuntimesDialog = searchFirstPath();
//		searchingForRuntimesDialog.ok();
//		
//		new WaitWhile(new JobIsRunning());
//		assertSeamRuntimesNumber(2);
//		assertServerRuntimesNumber(2);
//		
//		searchingForRuntimesDialog = searchFirstPath();
//		assertFoundRuntimesNumber(4);
//		
//		searchingForRuntimesDialog = searchFirstPath();
//		searchingForRuntimesDialog.hideAlreadyCreatedRuntimes();
//		assertFoundRuntimesNumber(0);
//	}
//
//	@After
//	public void deleteServers() throws IOException{
//		FileUtil.remove(tmpServerPath);
//		CleanEnvironmentUtils.cleanAll();
//	}
//	
//	private void assertFoundRuntimesNumber(int expected) {
//		List<Runtime> runtimes = searchingForRuntimesDialog.getRuntimes();
//		searchingForRuntimesDialog.cancel();
//		assertThat(runtimes.size(), is(expected));
//	}
}
