package org.jboss.tools.bpel.reddeer.matcher;

import org.eclipse.gef.EditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 * @author apodhrad
 * 
 */
public class ActivityOfType<T extends EditPart> extends BaseMatcher<EditPart> {

	private String type;

	public ActivityOfType(String type) {
		this.type = type;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			EditPart editPart = (EditPart) item;
			return type.equals(getActivityType(editPart));
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" activity of type '" + type + "'");
	}

	public static String getActivityType(EditPart editPart) {
		Class<?> clazz = editPart.getModel().getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length > 0) {
			return interfaces[0].getSimpleName();
		}
		return null;

	}
}
