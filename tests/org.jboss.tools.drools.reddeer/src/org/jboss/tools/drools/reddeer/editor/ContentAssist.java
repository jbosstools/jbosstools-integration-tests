package org.jboss.tools.drools.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.reddeer.swt.lookup.ShellLookup;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

public class ContentAssist {
    private final ITextEditor editor;
    private Shell assistShell;
    private Table assistTable;

    ContentAssist(ITextEditor editor) {
        this.editor = editor;
        open();
    }

    private void open() {
        Display.syncExec(new Runnable() {
            public void run() {
                Shell[] orig = new ShellLookup().getShells();

                IAction action = editor.getAction("ContentAssistProposal");
                action.run();

                Shell[] now = new ShellLookup().getShells();
                Shell result = null;
                nowLoop : for (Shell sh : now) {
                    for (Shell sh2 : orig) {
                        if (sh.equals(sh2)) {
                            continue nowLoop;
                        }
                    }

                    result = sh;
                }

                assistShell = result;
            }
        });

        if (assistShell == null) {
            throw new RuntimeException("Unable to open content assist");
        }

        Display.syncExec(new Runnable() {
            public void run() {
                Control c = assistShell.getChildren()[0];
                assistTable = (Table)c;
            }
        });
    }

    public void close() {
        if (assistShell != null) {
            Display.syncExec(new Runnable() {
                public void run() {
                    assistShell.close();
                }
            });
        }
        assistShell = null;
    }

    public List<String> getItems() {
        return Display.syncExec(new ResultRunnable<List<String>>() {
            public List<String> run() {
                List<String> result = new ArrayList<String>();

                for (TableItem item : assistTable.getItems()) {
                    result.add(item.getText());
                }

                return result;
            }
        });
    }

    public void selectItem(final String text) {
        List<String> items = getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(text)) {
                selectItem(i);
            }
        }
    }

    public void selectItem(final int index) {
        Display.syncExec(new Runnable() {
            public void run() {
                assistTable.select(index);

                Event event = new Event();
                event.type = SWT.Selection;
                event.widget = assistTable;
                event.item = assistTable.getItem(index);

                assistTable.notifyListeners(SWT.Selection, event);
                assistTable.notifyListeners(SWT.DefaultSelection, event);
            }
        });
    }
}
