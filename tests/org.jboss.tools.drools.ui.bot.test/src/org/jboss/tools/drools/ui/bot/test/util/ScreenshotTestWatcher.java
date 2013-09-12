package org.jboss.tools.drools.ui.bot.test.util;

import java.io.File;

import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ScreenshotTestWatcher extends TestWatcher {
    private static final File SCREENSHOT_DIR = new File("screenshots");

    public ScreenshotTestWatcher() {
        if (!SCREENSHOT_DIR.exists()) {
            SCREENSHOT_DIR.mkdirs();
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        String testName = description.getTestClass() != null ? description.getTestClass().getSimpleName() : "unknownClass";
        String methodName = description.getMethodName();
        int index = SCREENSHOT_DIR.list().length;
        File screenshotFile = new File(SCREENSHOT_DIR, String.format("%02d-%s-%s.png", index, methodName, testName ));

        SWTUtils.captureScreenshot(screenshotFile.getAbsolutePath());
    }

}
