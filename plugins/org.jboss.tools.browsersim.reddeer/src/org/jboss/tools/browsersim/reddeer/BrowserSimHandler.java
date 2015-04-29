/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.reddeer;

import java.lang.reflect.Field;
import org.apache.log4j.Logger;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.MenuHandler;
import org.jboss.reddeer.core.lookup.MenuLookup;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatchers;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.browsersim.ui.launch.BrowserSimArgs;
import org.jboss.tools.browsersim.ui.launch.BrowserSimRunner;
import org.jboss.tools.browsersim.browser.PlatformUtil;
import org.jboss.tools.browsersim.eclipse.launcher.BrowserSimLauncher;
import org.jboss.tools.browsersim.eclipse.preferences.PreferencesUtil;
import org.jboss.tools.browsersim.ui.BrowserSim;
import org.jboss.tools.browsersim.ui.util.BrowserSimUtil;
import org.jboss.tools.browsersim.ui.util.JavaFXUtil;

/**
 * Handles testing functionality for BrowserSim
 * 
 * @author Vladimir Pakan
 * @author Pavol Srna
 * 
 */
public class BrowserSimHandler {
	private static final String NOT_STANDALONE = BrowserSimLauncher.NOT_STANDALONE;
	private static final String CONFIGURATION = "-configuration";

	private static final Logger log = Logger.getLogger(BrowserSimHandler.class);
	private boolean urlLoadingCompleted = false;
	private boolean urlLoadingChanged = false;
	private BrowserSim browserSim;
	private Shell browserSimShell;

	private static boolean isJavaFxAvailable;
	private static boolean isWebKitAvailable;

	static {
		String platform = PlatformUtil.getOs();
		isJavaFxAvailable = false;

		boolean isLinux = PlatformUtil.OS_LINUX.equals(platform);

		// Trying to load javaFx libs except Linux GTK3 case
		if (!(isLinux && !BrowserSimUtil.isRunningAgainstGTK2())) {
			isJavaFxAvailable = JavaFXUtil.loadJavaFX();
		}

		isWebKitAvailable = BrowserSimUtil.isWebkitAvailable();
	}

	/**
	 * Opens BrowserSim with url and waits tiemOut for page to be fully loaded
	 * 
	 * @param url
	 *            - url of page to open in BrowserSim
	 * @param timeOut
	 * @return
	 */
	public BrowserSimHandler(final String url, TimePeriod timeOut) {

		BrowserSim browserSim = Display.syncExec(new ResultRunnable<BrowserSim>() {

			@SuppressWarnings("static-access")
			@Override
			public BrowserSim run() {

				String[] parameters = null;
				try {
					parameters = new String[] { NOT_STANDALONE, CONFIGURATION,
							PreferencesUtil.getBrowserSimConfigFolderPath() };
				} catch (Exception e) {
					throw new CoreLayerException(e.getMessage());
				}
				BrowserSimArgs.parseArgs(parameters);

				BrowserSim newBrowserSim = new BrowserSim(url, Display.getDisplay().getCurrent().getActiveShell());
				newBrowserSim.open(isJavaFxAvailable, isWebKitAvailable);
				return newBrowserSim;
			}

		});

		this.browserSim = browserSim;
		this.browserSimShell = new DefaultShell();

		if (!url.equalsIgnoreCase(BrowserSimRunner.ABOUT_BLANK) && url != null) {
			waitWhileUrlIsLoading(TimePeriod.SHORT, timeOut);
		}

	}

	/**
	 * Opens BrowserSim with url
	 * 
	 * @param url
	 *            - url of page to open in BrowserSim
	 * @return
	 */
	public BrowserSimHandler(final String url) {
		this(BrowserSimRunner.ABOUT_BLANK, TimePeriod.NORMAL);
	}

	/**
	 * Opens BrowserSim with empty page
	 * 
	 * @return
	 */
	public BrowserSimHandler() {
		this(BrowserSimRunner.ABOUT_BLANK);
	}

	/**
	 * Waits while url is loaded in browserSim
	 * 
	 * @param browserSim
	 * @param wait
	 * @param sleepTime
	 * @throws TimeoutException
	 */
	public void waitWhileUrlIsLoading(final TimePeriod sleepTime, final TimePeriod timeout) {
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
		while (!isCompleted && (System.currentTimeMillis() < startTime + (timeout.getSeconds() * 1000))) {
			log.info("Waiting while BrowserSim is loading URL");
			AbstractWait.sleep(sleepTime);
			isCompleted = isUrlLoadingCompleted();
		}
		removeProgressListener(pr);
		if (!isCompleted && isUrlLoadingChanged()) {

			String url = Display.syncExec(new ResultRunnable<String>() {
				@Override
				public String run() {
					return browserSim.getBrowser().getUrl();
				}
			});
			log.error("BrowserSim Url: " + url + " was not loaded completelly");
			throw new WaitTimeoutExpiredException("BrowserSim Url: " + url + " was not loaded completelly");
		}
	}

	/**
	 * Adds ProgressListener to BrowserSim underlying browser
	 * 
	 * @param browserSim
	 * @param progressListener
	 */
	public void addProgressListener(final ProgressListener progressListener) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				browserSim.getBrowser().addProgressListener(progressListener);
			}
		});
	}

	/**
	 * Removes ProgressListener to BrowserSim underlying browser
	 * 
	 * @param browserSim
	 * @param progressListener
	 */
	public void removeProgressListener(final ProgressListener progressListener) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				browserSim.getBrowser().removeProgressListener(progressListener);
			}
		});
	}

	private boolean isUrlLoadingCompleted() {
		synchronized (this) {
			return this.urlLoadingCompleted;
		}
	}

	private boolean isUrlLoadingChanged() {
		synchronized (this) {
			return this.urlLoadingChanged;
		}
	}

	private void setUrlLoadingCompleted(boolean urlLoadingCompleted) {
		synchronized (this) {
			this.urlLoadingCompleted = urlLoadingCompleted;
		}
	}

	private void setUrlLoadingChanged(boolean urlLoadingChanged) {
		synchronized (this) {
			this.urlLoadingChanged = urlLoadingChanged;
		}
	}

	/**
	 * Returns SWTBotBrowserExt instance of BrowserSim browser
	 * 
	 * @param browserSim
	 * @return
	 */
	public Browser getBrowserSimBrowser() {
		browserSimShell.setFocus();
		return new InternalBrowser();
	}

	public Menu openBrowserSimContextMenu() {
		browserSimShell.setFocus();
		new WaitUntil(new ShellIsActive(browserSimShell), TimePeriod.NORMAL);
		try {

			Menu[] menus = getPrivateFieldValue(RunningPlatform.isOSX() ? Display.class : Decorations.class, "menus",
					browserSimShell.getSWTWidget(), Menu[].class);
			Menu browserSimContextMenu = menus[0];

			showMenu(browserSimContextMenu);
			return browserSimContextMenu;
		} catch (SecurityException se) {
			throw new SWTLayerException("Unable to find MenuItem with label ", se);
		} catch (NoSuchFieldException nsfe) {
			throw new SWTLayerException("Unable to find MenuItem with label ", nsfe);
		} catch (IllegalArgumentException iae) {
			throw new SWTLayerException("Unable to find MenuItem with label ", iae);
		} catch (IllegalAccessException iace) {
			throw new SWTLayerException("Unable to find MenuItem with label ", iace);
		}
	}

	/**
	 * Returns browserSim shell
	 * 
	 * @param browserSim
	 * @return
	 */
	public Shell getBrowserSimShell(final BrowserSim browserSim) {
		return this.browserSimShell;
	}

	/**
	 * Loads Url to BrowserSim browser and waits until it's fully loaded
	 * 
	 * @param url
	 */
	public void loadUrlToBrowser(final String url, TimePeriod timeOut) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				browserSim.getBrowser().setUrl(url);
			}
		});
		waitWhileUrlIsLoading(TimePeriod.SHORT, timeOut);
	}

	/**
	 * Sets URL url in address bar and waits until it's fully loaded
	 * 
	 * @param url
	 */
	public void loadUrlFromAddressBar(final String url, TimePeriod timeOut) {
		browserSimShell.setFocus();
		Text addressBarText = getAddressText();
		addressBarText.setText(url);
		addressBarText.setFocus();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR, SWT.LF);
		waitWhileUrlIsLoading(TimePeriod.SHORT, timeOut);
	}

	public Text getAddressText() {
		return new DefaultText();
	}

	public StyledText getTitleStyledText() {
		return new DefaultStyledText();
	}

	/**
	 * Closes BrowserSim
	 */
	public void close() {
		browserSimShell.close();

	}

	/**
	 * Clicks on BrowserSim context menu specified by menuLabels
	 * 
	 * @param menuLabels
	 */
	public void clickContextMenu(String... menuLabels) {

		browserSimShell.setFocus();
		final Menu menu = openBrowserSimContextMenu();
		MenuItem[] mItems = null;
		mItems = Display.syncExec(new ResultRunnable<MenuItem[]>() {
			@Override
			public MenuItem[] run() {
				return menu.getItems();
			}
		});
		MenuLookup ml = MenuLookup.getInstance();
		MenuItem item = ml.lookFor(mItems, new WithMnemonicTextMatchers(menuLabels).getMatchers());
		MenuHandler.getInstance().select(item);
	}

	/**
	 * Sets checked status of BrowserSim context menu item with label menuLabel
	 * to checked parameter value Fires events only if checked status of menu
	 * item was changed
	 * 
	 * @param checked
	 * @param menuLabel
	 */
	public void checkContextMenu(boolean checked, String menuLabel) {
		browserSimShell.setFocus();
		final Menu menu = openBrowserSimContextMenu();
		MenuItem[] mItems = null;
		mItems = Display.syncExec(new ResultRunnable<MenuItem[]>() {
			@Override
			public MenuItem[] run() {
				return menu.getItems();
			}
		});
		MenuLookup ml = MenuLookup.getInstance();
		MenuItem item = ml.lookFor(mItems, new WithMnemonicTextMatchers(menuLabel).getMatchers());
		if (MenuHandler.getInstance().isSelected(item) != checked) {
			MenuHandler.getInstance().select(item);
		}
	}

	/**
	 * Closes all running instances of BRowserSim
	 */
	public static void closeAllRunningInstances() {
		for (ILaunch launch : DebugPlugin.getDefault().getLaunchManager().getLaunches()) {
			if (launch.getLaunchConfiguration() != null
					&& (launch.getLaunchConfiguration().getName().equals("BrowserSim"))
					|| launch.getLaunchConfiguration().getName().equals("CordovaSim")) {
				try {
					launch.terminate();
				} catch (DebugException de) {
					throw new RuntimeException(de);
				}
			}
		}
	}

	public static void showMenu(final Menu menu) {
		menu.getDisplay().syncExec(new Runnable() {
			public void run() {
				menu.notifyListeners(SWT.Show, new Event());
			}
		});
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T getPrivateFieldValue(Class<?> clazz, String fieldName, Object instance, Class<T> resultClazz)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Object value = field.get(instance);
		if (value != null) {
			return (T) value;
		} else {
			return null;
		}
	}

}
