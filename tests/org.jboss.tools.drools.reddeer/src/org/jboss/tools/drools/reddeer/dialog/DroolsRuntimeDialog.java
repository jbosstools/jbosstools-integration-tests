package org.jboss.tools.drools.reddeer.dialog;

import org.drools.eclipse.util.DroolsRuntimeManager;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class DroolsRuntimeDialog {

    public void setName(String name) {
        new LabeledText("Name:").setText(name);
    }

    public void setLocation(String location) {
        new LabeledText("Path:").setText(location);
    }

    public void createNewRuntime(String location) {
        // FIXME find a way to do this using SWTBot/RedDeer
        DroolsRuntimeManager.createDefaultRuntime(location);
        setLocation(location);
    }

    public void cancel() {
        new PushButton("Cancel").click();
    }

    public void ok() {
        new PushButton("OK").click();
    }

    public boolean isValid() {
        return new PushButton("OK").isEnabled();
    }
}
