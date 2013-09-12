package org.jboss.tools.drools.reddeer.editor;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.reddeer.workbench.exception.EditorNotFoundException;

public class DrlEditor extends DefaultEditor {
    private static final Logger LOGGER = Logger.getLogger(DrlEditor.class);

    public DrlEditor() {
        super();
        if (!(workbenchPart instanceof MultiPageEditorPart)){
            throw new EditorNotFoundException("Given editor is not instance of MultiPageEditorPart");
        }
    }

    public String getText() {
        return getDocument().get();
    }

    public void setText(final String text) {
        Display.syncExec(new Runnable() {
            public void run() {
                getDocument().set(text);
            }
        });
    }

    public void showReteTree() {
        Display.syncExec(new Runnable() {
            public void run() {
                for (IEditorPart editor : getEditorPart().findEditors(getEditorPart().getEditorInput())) {
                    if (!(editor instanceof ITextEditor)) {
                        getEditorPart().setActiveEditor(editor);
                    }
                }
            }
        });
    }

    public void showDrlText() {
        Display.syncExec(new Runnable() {
            public void run() {
                for (IEditorPart editor : getEditorPart().findEditors(getEditorPart().getEditorInput())) {
                    if (editor instanceof ITextEditor) {
                        getEditorPart().setActiveEditor(editor);
                    }
                }
            }
        });
    }

    public void setBreakpoint(int line) {
        try {
            IDocument doc = getDocument();
            int lineOffset = doc.getLineOffset(line);

            IToggleBreakpointsTarget breakpoint = (IToggleBreakpointsTarget)getEditorPart().getAdapter(IToggleBreakpointsTarget.class);
            breakpoint.toggleLineBreakpoints(getEditorPart(), new TextSelection(doc, lineOffset, 0));
        } catch (BadLocationException ex) {
            throw new IllegalArgumentException("Wrong line number", ex);
        } catch (CoreException ex) {
            throw new RuntimeException("Unable to set breakpoint", ex);
        }
    }

    public void setPosition(int line, int column) {
        setSelection(line, column, 0);
    }

    public void setSelection(int startLine, int startColumn, int endLine, int endColumn) {
        try {
            int offset = getDocument().getLineOffset(startLine) + startColumn;
            int length = getDocument().getLineOffset(endLine) + endColumn - offset;

            setSelection(offset, length);
        } catch (BadLocationException ex) {
            throw new IllegalArgumentException("Invalid coordinates", ex);
        }
    }

    public void setSelection(int line, int column, int length) {
        try {
            int offset = getDocument().getLineOffset(line) + column;
            setSelection(offset, length);
        } catch (BadLocationException ex) {
            throw new IllegalArgumentException("Invalid coordinates", ex);
        }
    }

    public void writeText(final String text) {
        final int offset = getOffset();
        Display.syncExec(new Runnable() {
            public void run() {
                try {
                    getDocument().replace(offset, 0, text);
                } catch (BadLocationException ex) {
                    LOGGER.error("Wrong location returned", ex);
                }
            }
        });
        setSelection(offset + text.length(), 0);
    }

    /**
     * Gets the text on line selected. The returned string does not contain line delimiter (even if it is present).
     * 
     * @return content of the line that cursor is currently on.
     */
    public String getTextOnCurrentLine() {
        return Display.syncExec(new ResultRunnable<String>() {
            public String run() {
                String result = null;
                try {
                    IDocument doc = getDocument();
                    int line = doc.getLineOfOffset(getOffset());
                    String delimiter = doc.getLineDelimiter(line);
                    int delimiterLength = delimiter != null ? delimiter.length() : 0;
                    result = doc.get(doc.getLineOffset(line), doc.getLineLength(line) - delimiterLength);
                } catch (BadLocationException ex) {
                    LOGGER.error("Wrong location returned", ex);
                }
                return result;
            }
        });
    }

    public ContentAssist createContentAssist() {
        return new ContentAssist(getTextEditorPart());
    }

    protected MultiPageEditorPart getEditorPart() {
        if (workbenchPart == null) {
            throw new RuntimeException("workbenchPart is null");
        }
        if (!(workbenchPart instanceof MultiPageEditorPart)) {
            throw new RuntimeException("workbenchPart isn't instance of MultiPageEditorPart");
        }
        return (MultiPageEditorPart) workbenchPart;
    }

    protected ITextEditor getTextEditorPart() {
        showDrlText();
        return Display.syncExec(new ResultRunnable<ITextEditor>() {
            public ITextEditor run() {
                for (IEditorPart editor : getEditorPart().findEditors(getEditorPart().getEditorInput())) {
                    if (editor instanceof ITextEditor) {
                        return (ITextEditor)editor;
                    }
                }
                return null;
            }
        });
    }

    protected IDocument getDocument() {
        return getTextEditorPart().getDocumentProvider().getDocument(getTextEditorPart().getEditorInput());
    }

    protected void setSelection(final int offset, final int length) {
        Display.syncExec(new Runnable() {
            public void run() {
                getTextEditorPart().getSelectionProvider().setSelection(new TextSelection(getDocument(), offset, length));
            }
        });
    }

    protected int getOffset() {
        int offset = Display.syncExec(new ResultRunnable<Integer>() {
            public Integer run() {
                ISelection sel = getTextEditorPart().getSelectionProvider().getSelection();
                if (sel instanceof ITextSelection) {
                    return ((ITextSelection) sel).getOffset();
                }
                return -1;
            }
        });
        

        if (offset < 0) {
            throw new RuntimeException("Unable to get current position");
        }

        return offset;
    }
}
