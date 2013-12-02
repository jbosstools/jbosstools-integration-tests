/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ui.bot.ext;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withStyle;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.Twistie;
import org.hamcrest.Matcher;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotHyperlinkExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotScaleExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotTwistie;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotSection;

/**
 * Extended version of SWTWorkbenchBot, logging added
 * 
 * @author jpeterka
 * 
 */
public class SWTBotExt extends SWTWorkbenchBot {

	private Logger log = Logger.getLogger(SWTBotExt.class);

	public void logAndFail(String msg) {
		log.error(msg);
		fail(msg);
	}

	// ------------------------------------------------------------
	// SWTBot method wrapper ( for better logging mainly )
	// ------------------------------------------------------------

	@Override
	public SWTBotMenu menu(String text) {
		log.info("Menu \"" + text + "\" selected");
		return super.menu(text);
	}

	@Override
	public SWTBotButton button(String text) {
		log.info("Button \"" + text + "\" selected");
		return super.button(text);
	}
	
	@Override
	public SWTBotTree tree() {
		log.info("Tree selected");
		return super.tree();
	}

	@Override
	public SWTBotCCombo ccomboBox(String text) {
		log.info("Combobox \"" + text + "\" selected");
		return super.ccomboBox(text);
	}

	@Override
	public SWTBotTable table() {
		log.info("Table selected");
		return super.table();
	}
	
	public void sleep(long ms, String msg) {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if ((ste != null) && (ste[3] != null))
			log.info("Bot sleeps for " + ms + " ms  " + msg + " " + ste[3].toString());
		else
			log.info("Bot sleeps for " + ms + " ms  " + msg);
		super.sleep(ms);		
	}
	
	@Override
	public void sleep(long ms) {
		sleep(ms,"");
	}

	public SWTBotEditorExt swtBotEditorExtByTitle(String fileName) {
		SWTBotEditor editor = super.editorByTitle(fileName);
		return new SWTBotEditorExt(editor.toTextEditor().getReference(),
				(SWTWorkbenchBot) this);
	}

	@SuppressWarnings("unchecked")
	public SWTBotBrowserExt browserExt() {
		try {
			List<Browser> bsrs = (List<Browser>) widgets(widgetOfType(Browser.class));
			return new SWTBotBrowserExt(bsrs.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Browser", ex);
		}
	}
	@SuppressWarnings("unchecked")
	public SWTBotBrowserExt browserByTitle(String title) {
		SWTBotEditor editor = editorByTitle(title);
		try {
			List<Browser> bsrs = (List<Browser>) editor.bot().widgets(
					widgetOfType(Browser.class));
			return new SWTBotBrowserExt(bsrs.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Browser", ex);
		}
	}

	public SWTBotScaleExt scaleExt() {
		return scaleExt(0);
	}
	@SuppressWarnings("unchecked")
	public SWTBotScaleExt scaleExt(int index) {
		try {
			List<Scale> bsrs = (List<Scale>) widgets(widgetOfType(Scale.class));
			return new SWTBotScaleExt(bsrs.get(index));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Browser", ex);
		}
	}
	@SuppressWarnings("unchecked")
	public SWTBotHyperlinkExt hyperlink(String text) {
		try {
			List<Hyperlink> bsrs = (List<Hyperlink>) widgets(allOf(widgetOfType(Hyperlink.class),withText(text)));
			return new SWTBotHyperlinkExt(bsrs.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Hyperlink", ex);
		}
	}
	@SuppressWarnings("unchecked")
	public SWTBotHyperlinkExt hyperlink(int index) {
		try {
			List<Hyperlink> bsrs = (List<Hyperlink>) widgets(widgetOfType(Hyperlink.class));
			return new SWTBotHyperlinkExt(bsrs.get(index));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Hyperlink", ex);
		}
	}
	public SWTBotHyperlinkExt hyperlink() {
		return hyperlink(0);
	}
	public SWTBotButton clickButton(String text) {
		return button(text).click();
	}
	@SuppressWarnings("unchecked")
	public SWTBotSection section(String label) {
		try {
		List<Section> sections = (List<Section>)widgets(allOf(withText(label),widgetOfType(Section.class)));
		return new SWTBotSection(sections.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Section", ex);
		}
	}
	@SuppressWarnings("unchecked")
	public SWTBotSection section(SWTBot bot, String label) {
		try {
		List<Section> sections = (List<Section>)bot.widgets(allOf(withText(label),widgetOfType(Section.class)));
		return new SWTBotSection(sections.get(0));
		} catch (WidgetNotFoundException ex) {
			throw new WidgetNotFoundException(
					"Could not find widget of type Section", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public SWTBotTwistie twistieByLabel(String label){
		List<Twistie> list = (List<Twistie>)widgets(widgetOfType(Twistie.class));
		SWTBotTwistie twistieBot = null;
		for (Twistie twistie : list) {
			twistieBot = new SWTBotTwistie(twistie);
			if (twistieBot.getLabelText().equals(label)) return twistieBot;
		}
		//SWTBotTwistie twistie = new SWTBotTwistie((Twistie)widget(allOf(widgetOfType(Twistie.class), WidgetMatcherFactory.withLabel(label))));
		//twistie.getId();
		throw new WidgetNotFoundException("Widget of type Twistie with label \""+label+"\" was not found");
	}

    /**
     * Waits for shell with given title.
     * Maximum waiting time is 30 seconds.
     *
     * @param shellTitle Title of shell which it should wait for.
     * @return Shell which it was waiting for or <code>null</code>
     *         if the shell was not displayed during waiting time.
     * @see #waitForShell(String, int)
     */
    public SWTBotShell waitForShell(final String shellTitle) {
        return waitForShell(shellTitle, -1);
    }

    /**
     * Waits for desired shell with given timeout
     * and return this shell when it is displayed.
     *
     * @param shellTitle Title of desired shell.
     * @param maxTimeout Maximum waiting time in seconds (actual timeout can be one second more).
     *                   Negative value means default timeout (30 seconds).
     * @return Shell which it was waiting for or <code>null</code>
     *         if the shell was not displayed during waiting time.
     */
	public SWTBotShell waitForShell(final String shellTitle, final int maxTimeout) {
        if (shellTitle == null) {
            throw new IllegalArgumentException("shellTitle cannot be null");
        }

        final int SLEEP_TIME = Timing.time2S();
        final int ATTEMPTS_TIMEOUT = getAttemptsTimeout((maxTimeout < 0 ? 30 : maxTimeout), SLEEP_TIME);

        for (int i = 0; i <= ATTEMPTS_TIMEOUT; i++) {
            for (SWTBotShell shell : shells()) {
                if (!shell.widget.isDisposed() && shellTitle.equals(shell.getText())) {
                    return shell;
                }
            }
            if (i < ATTEMPTS_TIMEOUT) {
                sleep(SLEEP_TIME);
            }
        }

        return null;
    }

    /**
     * Waits until there is desired number of opened shells.
     * It is useful sometimes when you cannot address exactly
     * what you are waiting for but you know that the right situation
     * is when there is <code>desiredNumberOfSheels</code> of shells.
     * For example you are waiting for closing of some dialog so there are
     * two shells and after closing this dialog there is one desired shell.
     * 
     * @param desiredNumberOfShells Number of desired shells.
     * @param maxTimeout Maximum time to wait in seconds. If this value is negative,
     *        default timeout is used (30 seconds).
     * @return Shells which exists after timeout or after achieving desired
     *         number of shells.
     */
    public List<SWTBotShell> waitForNumberOfShells(final int desiredNumberOfShells, final int maxTimeout) {
        final int SLEEP_TIME = Timing.time2S();
        final int ATTEMPTS_TIMEOUT = getAttemptsTimeout((maxTimeout < 0 ? 30 : maxTimeout), SLEEP_TIME);

        List<SWTBotShell> shells = null;
        for (int i = 0; i < ATTEMPTS_TIMEOUT; i++) {
            SWTBotShell[] shellsArray = shells();
            shells = new ArrayList<SWTBotShell>(shellsArray.length);
            for (int j = 0; j < shellsArray.length; j++) {
                if (shellsArray[j].isOpen()) {
                    shells.add(shellsArray[j]);
                }
            }
            if (shells.size() != desiredNumberOfShells) {
                sleep(SLEEP_TIME);
            } else {
                break;
            }
        }

        return shells;
    }

    /**
     * Waits until there is desired number of shells which exist
     * with default timeout (30 seconds).
     * 
     * @param desiredNumberOfShells
     * @return Shells which exists after timeout or after achieving desired
     *         number of shells.
     */
    public List<SWTBotShell> waitForNumberOfShells(final int desiredNumberOfShells) {
        return waitForNumberOfShells(desiredNumberOfShells, -1);
    }

    /**
     * If you need a loop in which some condition is tested and you want to know
     * how many loops should be done before timeout, give to this method <code>maxTimeout</code>
     * in seconds and desired delay between loops and this method returns number of loops
     * that should be done.
     * 
     * @param maxTimeout Maximum waiting time in seconds.
     * @param sleepTime Waiting time in milliseconds between attempts.
     * @return Number of attempts which is approximately equals to <code>maxTimeout</code>
     *         with <code>sleepTime</code> in each loop. 
     */
    private int getAttemptsTimeout(final int maxTimeout, final int sleepTime) {
        return ((int)Math.ceil((double)(maxTimeout * 1000) / (double)sleepTime));
    }

    /**
     * Returns current displayed checkboxes.
     * There are included disabled checkboxes as well
     * therefore it is necessary to check their states before use of them.
     * 
     * @return List of current displayed checkboxes.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SWTBotCheckBox> checkBoxes() {
        List<SWTBotCheckBox> checkBoxes = new ArrayList<SWTBotCheckBox>();
        Matcher matcher = allOf(widgetOfType(Button.class), withStyle(SWT.CHECK, "SWT.CHECK"));
        for (Object widget : widgets(matcher)) {
            if (widget != null) {
                checkBoxes.add(new SWTBotCheckBox((Button) widget));
            }
        }
        return checkBoxes;
    }
}
