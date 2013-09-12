package org.jboss.tools.drools.ui.bot.test.util;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class ApplicationIsRunning implements WaitCondition {
    private ConsoleView console;
    
    public ApplicationIsRunning() {
        console = new ConsoleView();
    }

    public boolean test() {
        return console.getTitle().contains("<terminated>");
    }

    public String description() {
        return "Tests that currently running application is in terminated state";
    }

}
