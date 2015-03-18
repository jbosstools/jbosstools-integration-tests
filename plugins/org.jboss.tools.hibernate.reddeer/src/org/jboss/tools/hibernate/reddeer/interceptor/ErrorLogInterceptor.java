package org.jboss.tools.hibernate.reddeer.interceptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.interceptor.ISyncInterceptor;

/**
 * ErrorLogInterceptors servers for watching Error Log changes betwen 
 * syncExec operations. It checks .log file. 
 * Note: Beware it quite aggresive as it recreate log file whenever it has some content
 * @author Jiri Peterka
 *
 */
public class ErrorLogInterceptor implements ISyncInterceptor {

	private Logger log = Logger.getLogger(this.getClass());
	private File file;
	
	public ErrorLogInterceptor() {
		file = new File(Platform.getLocation().toString() + File.separator + ".metadata" + File.separator + ".log");
	}
	
	/**
	 * Logs changes from error log before syncExec()
	 */
	@Override
	public void beforeSyncOp() {
		logAndCut("beforeSync");	
	}

	/**
	 * Logs changes from error log after syncExec()
	 */
	@Override
	public void afterSyncOp() {
		logAndCut("afterSync");
	}


	private void logAndCut(String flag) {
		if (!file.exists()) return;
		boolean content = false;
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				log.error("New in Error Log " + flag + " : " + scanner.nextLine());
				content = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) scanner.close();
		}
		
		if (content) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
