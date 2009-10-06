package org.jboss.tools.ui.runtime.bot.test.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.jboss.tools.ui.runtime.bot.test.Activator;

@SuppressWarnings("deprecation")
public class JBossRuntimePreferencesInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		Preferences preferences = Activator.getDefault().getPluginPreferences();
		preferences.setDefault(
				Activator.FIRST_START,
				true);
	}

}
