package org.jboss.tools.usercase.ticketmonster.ui.bot.test.browsersim;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;
import org.jboss.tools.vpe.browsersim.BrowserSimRunner;

public class BrowsersimHandler {
	
	
	private BrowserSim browserSim;
	private Shell browserSimShell;
	private boolean urlLoadingCompleted = false;
	private boolean urlLoadingChanged = false;
	
	private static boolean isJavaFxAvailable;
	private static boolean isWebKitAvailable;
	
	static {
		String platform = PlatformUtil.getOs();
	    isJavaFxAvailable = false;
	    
	    
	    boolean isLinux = PlatformUtil.OS_LINUX.equals(platform);

	    // Trying to load javaFx libs except Linux GTK3 case
	    if (!(isLinux && !BrowserSimUtil.isRunningAgainstGTK2())) {
	      isJavaFxAvailable = BrowserSimUtil.loadJavaFX();
	    }
	    
	    isWebKitAvailable = BrowserSimUtil.isWebkitAvailable();
	}
	 /**
     * Gets list of running java processes via calling command jps
     * @return
     */
    public static List<String> getRunningJavaProcesesNames(){
      List<String> result = new LinkedList<String>();
      String javaHome = System.getProperty("java.home", "");
      // search for sdk location instead of jre location
      if (javaHome.endsWith(File.separator + "jre")){
        javaHome = javaHome.substring(0,javaHome.length() -4);
      }
      String jpsCommand = "jps";
      if (javaHome.length() > 0) {
        File javaLocation = new File(javaHome);
        if (javaLocation.exists() && javaLocation.isDirectory()) {
          File javaBinLocation = new File(javaLocation, "bin" + File.separator
              + "jps");
          if (javaBinLocation.exists()) {
            jpsCommand = javaBinLocation.getAbsolutePath();
          }
        }
      }
      String line;
      Process p;
      try {
        p = Runtime.getRuntime().exec(jpsCommand);
        BufferedReader input = new BufferedReader(new InputStreamReader(
            p.getInputStream()));
        while ((line = input.readLine()) != null) {
          if(line.length() > 0){
            String[] lineSplit = line.split(" ");
            if (lineSplit.length > 1){
              result.add(lineSplit[1]);  
            }
            else {
              result.add("[PID]:" + lineSplit[0]);
            }
          }
        }
        input.close();

      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }
      return result;
    }
    
    /**
     * Counts running java processes with name processName
     * @param processName
     * @return
     */
    public static int countJavaProcess(String processName){
      List<String> runningJavaProcesses = getRunningJavaProcesesNames();
      List<String> processNameList = new LinkedList<String>();
      processNameList.add(processName);
      runningJavaProcesses.retainAll(processNameList);
      return runningJavaProcesses.size();  
    }
    
    public void openBrowsersim(final String url){
    	
    	browserSim = Display.syncExec(new ResultRunnable<BrowserSim>() {
    		
    		@Override
    	    public BrowserSim run() {
    			BrowserSim newBrowserSim = new BrowserSim(url, org.eclipse.swt.widgets.Display.getCurrent().getActiveShell());
    	        newBrowserSim.open(isJavaFxAvailable,isWebKitAvailable);
    	        return newBrowserSim;
    	    }
    		
    	});
    	
    	browserSimShell = new DefaultShell();
    	//ticketmonster javascript info
    	if (!url.equalsIgnoreCase(BrowserSimRunner.ABOUT_BLANK) && url != null){
    		waitWhileUrlIsLoading();
    	}
    }
    
    /**
     * Waits while url is loaded in browserSim
     * @param browserSim
     * @param wait
     * @param sleepTime
     */
    public void waitWhileUrlIsLoading(){
      setUrlLoadingChanged(false);
      setUrlLoadingCompleted(false);
      ProgressListener pr = new ProgressListener() {
        @Override
        public void completed(ProgressEvent arg0) {
          setUrlLoadingCompleted(true);
        }
        @Override
        public void changed(ProgressEvent arg0) {
          setUrlLoadingChanged(true);
        }
      };
      long startTime = System.currentTimeMillis();
      addProgressListener(pr);
      boolean isCompleted = isUrlLoadingCompleted();
      while (!isCompleted && (System.currentTimeMillis() < startTime + 5000)){
        AbstractWait.sleep(TimePeriod.SHORT);
        isCompleted = isUrlLoadingCompleted();
      }
      removeProgressListener(pr);
      if (!isCompleted && isUrlLoadingChanged()){
        String url = Display.syncExec(new ResultRunnable<String>() {
          @Override
          public String run() {
            return browserSim.getBrowser().getUrl();
          }
        });
        throw new WaitTimeoutExpiredException("BrowserSim Url: " + url + " was not loaded completelly");
      }
    }
    
    private void setUrlLoadingChanged (boolean urlLoadingChanged) {
        synchronized (this) {
        	this.urlLoadingChanged = urlLoadingChanged;
        }    
    }
    
    private boolean isUrlLoadingChanged () {
        synchronized(this){
          return this.urlLoadingChanged;
        }
    }
    
    private void setUrlLoadingCompleted (boolean urlLoadingCompleted) {
    	synchronized(this){
    		this.urlLoadingCompleted = urlLoadingCompleted;  
        }
    }
    
    /**
     * Adds ProgressListener to BrowserSim underlying browser
     * @param browserSim
     * @param progressListener
     */
    public void addProgressListener (final ProgressListener progressListener){
      Display.getDisplay().syncExec(new Runnable() {
        @Override
        public void run() {
          browserSim.getBrowser().addProgressListener(progressListener);
        }
      });
    }
    
    private boolean isUrlLoadingCompleted () {
        synchronized(this){
          return this.urlLoadingCompleted;
        }  
    }
    
    /**
     * Removes ProgressListener to BrowserSim underlying browser
     * @param browserSim
     * @param progressListener
     */
    public void removeProgressListener (final ProgressListener progressListener){
    	Display.getDisplay().syncExec(new Runnable() {
    		@Override
    		public void run() {
    			browserSim.getBrowser().removeProgressListener(progressListener);
    		}
    	});
    }
    
    /**
     * Returns browserSim shell
     * @param browserSim
     * @return
     */
    public Shell getBrowserSimShell(final BrowserSim browserSim){
      return this.browserSimShell;
    }
    
    
    public String getBrowserSimBrowser(){
    	return Display.syncExec(new ResultRunnable<String>() {

			@Override
			public String run() {
				return browserSim.getBrowser().getText();
			}
		});
    }
    
    public void close(){
    	browserSimShell.close();
    }

}
