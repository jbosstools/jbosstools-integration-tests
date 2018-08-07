package org.jboss.tools.maven.ui.bot.test.utils;

import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;

public class EditorResourceHelper {
    
    public static void replaceClassContentByResource(InputStream resource, boolean save, boolean closeEdit) {
        Scanner s = new Scanner(resource);
        String code = s.useDelimiter("\\A").next();
        s.close();
        DefaultEditor e = new DefaultEditor();
        new DefaultStyledText().setText(code);
        if (save) e.save();
        if (closeEdit) e.close();
    }

}
