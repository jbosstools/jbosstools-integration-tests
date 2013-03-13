package org.jboss.tools.bpel.ui.bot.ext.matcher;

import java.lang.reflect.Method;

import org.eclipse.gef.EditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 * @author apodhrad
 * 
 */
public class ActivityWithName<T extends EditPart> extends BaseMatcher<EditPart> {

	private String label;

	public ActivityWithName(String label) {
		this.label = label;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			EditPart editPart = (EditPart) item;
			return label.equals(getActivityName(editPart));
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" activity with label '" + label + "'");
	}

	public static String getActivityName(EditPart editPart) {
		Object model = editPart.getModel();
		Class<?> clazz = model.getClass();

		try {
			Method method = clazz.getMethod("getName");
			Object result = method.invoke(model);
			return (String) result;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}
}
