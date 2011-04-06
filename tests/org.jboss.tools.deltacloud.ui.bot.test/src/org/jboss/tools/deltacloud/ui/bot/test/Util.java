package org.jboss.tools.deltacloud.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.jboss.tools.deltacloud.ui.bot.test.view.CloudConnection;
import org.jboss.tools.deltacloud.ui.bot.test.view.CloudViewer;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Preference;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class Util {
	static Logger log = Logger.getLogger(Util.class);
	public static final String CONNECTION_DEFAULT_NAME="default";
	public static final String MOCK_URL="http://localhost:3000";
	public static final String MOCK_USER="mockuser";
	public static final String MOCK_PASS="mockpassword";

	public static void setupDefaultConnection(CloudViewer viewer) {
		viewer.addConnection("default", MOCK_URL, MOCK_USER, MOCK_PASS);
	}
	
	public static void setupPreferences(boolean warnInstances, boolean autoConnectRSE) {
		SWTBot wiz = SWTTestExt.open.preferenceOpen(Preference.create("Deltacloud"));
		SWTBotCheckBox wi = wiz.checkBox("Do not warn me when launching an instance");
		SWTBotCheckBox acrse = wiz.checkBox("Automatically connect to RSE when creating an instance");
		if (warnInstances) wi.deselect();
		else wi.select();
		if (autoConnectRSE) acrse.select();
		else acrse.deselect();
		SWTTestExt.open.finish(wiz, IDELabel.Button.OK);
	}
	
	public static void setupInstances(CloudConnection conn, int instances) {
		conn.destroyAllInstances();
		final String instance="inst-";
		List<String> images = conn.getImages();
		assertTrue("No images in cloud, cannot continue", images.size()>0);
		for (int i = 0;i<instances;i++) {
			conn.launchDefault(instance, images.get(0), "default");
		}
	}

	/**
	 * gets image id from image label ( format is xxx [id])
	 * 
	 */
	public static String getImageId(String imageLabel) {
		int i = imageLabel.lastIndexOf("[");
		if (i>0) {
			return imageLabel.substring(i+1, imageLabel.length()-1);
		}
		return "";
	}
	public static boolean isDeltacloudRunning(String url) {
		HttpURLConnection connection = null;
		try {
			URL u = new URL(url);
			connection = (HttpURLConnection) u.openConnection();
			connection.setRequestProperty("accept", "*/*");
			if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
				return true;
			}
			log.error(connection.getResponseCode()+" "+connection.getResponseMessage());
			return false;
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e) {
			log.error(e);
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
