package org.jboss.tools.ui.bot.ext.parts;

import java.lang.reflect.Field;

import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.internal.forms.widgets.FormTextModel;
import org.hamcrest.SelfDescribing;

@SWTBotWidget(clasz = FormText.class, preferredName="formText", referenceBy = { ReferenceBy.MNEMONIC })
public class SWTBotFormTextExt extends AbstractSWTBotControl<FormText>{

	public SWTBotFormTextExt(FormText w) throws WidgetNotFoundException {
		super(w);
	}

	public SWTBotFormTextExt(FormText w, SelfDescribing description)
			throws WidgetNotFoundException {
		super(w, description);
	}
	
	public String selectionText() {
		return syncExec(new StringResult() {
			
			@Override
			public String run() {
				return widget.getSelectionText();
			}
		});
	}
	
	public AbstractSWTBotControl<FormText> click()  {
		/*syncExec(new VoidResult() {
			
			@Override
			public void run() {
				click(widget.toDisplay(25,5).x, widget.toDisplay(25,5).y, true); //+20 is there because of icon
			}
		});*/
		int timeout = 0;
		while (!hasFocus() && timeout!=5){
			//try to set focus
			setFocus();
			log.info("Trying to set focus");
			sleep(1000);
			timeout++;
		}
		if (!hasFocus()){
			throw new IllegalStateException("Unable to focus widget of type Hyperlink");
		}
		keyboard().typeCharacter('\r');
		return this;
	}
	
	/**
	 * Tests whether widget has focus or not. Needed for workaround of issue, where method setFocus() isn't working properly when ececuting test via maven.
	 * @return true if widget has focus. False otherwise.
	 */
	
	public boolean hasFocus(){
		return syncExec(new BoolResult() {
			
			@Override
			public Boolean run() {
				return widget.isFocusControl();
			}
		});
	}

	
	public String selectedLinkText() {
		return syncExec(new StringResult() {
			
			@Override
			public String run() {
				return widget.getSelectedLinkText();
			}
		});
	}
	
	public String toolTipText() {
		return syncExec(new StringResult() {
			
			@Override
			public String run() {
				return widget.getToolTipText();
			}
		});
	}

	@Override
	@SuppressWarnings("restriction")
	public String getText() {
		Field field;
		try {
			field = widget.getClass().getDeclaredField("model");
		} catch (SecurityException e1) {
			throw new SecurityException(e1);
		} catch (NoSuchFieldException e1) {
			throw new SecurityException(e1);
		}
		FormTextModel model;
		field.setAccessible(true);
		try {
			model = (FormTextModel)field.get(widget);
		} catch (IllegalArgumentException e1) {
			throw new SecurityException(e1);
		} catch (IllegalAccessException e1) {
			throw new SecurityException(e1);
		}
		return model.getAccessibleText().trim();
		
	}
	

}
