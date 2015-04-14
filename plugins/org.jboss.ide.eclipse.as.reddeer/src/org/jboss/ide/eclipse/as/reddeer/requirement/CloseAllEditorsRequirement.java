package org.jboss.ide.eclipse.as.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.as.reddeer.requirement.CloseAllEditorsRequirement.CloseAllEditors;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.core.util.Display;

public class CloseAllEditorsRequirement implements Requirement<CloseAllEditors> {

	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
	public @interface CloseAllEditors {
		
	}
	
	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().closeAllEditors(true);
			}
		});
	}

	@Override
	public void setDeclaration(CloseAllEditors declaration) {
		
	}
}
