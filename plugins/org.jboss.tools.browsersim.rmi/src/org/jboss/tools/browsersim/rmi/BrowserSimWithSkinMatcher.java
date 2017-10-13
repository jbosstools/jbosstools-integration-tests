/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.rmi;

import java.util.List;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.core.resolver.WidgetResolver;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class BrowserSimWithSkinMatcher extends BaseMatcher<Shell> {

		@Override
		public boolean matches(Object item) {
			if(item instanceof Shell) {
				return Display.syncExec(new ResultRunnable<Boolean>() {

					@Override
					public Boolean run() {
						Shell shell = (Shell) item;
						List<Widget> children = WidgetResolver.getInstance().getChildren(shell);
						return children.size() > 0;
					}
				});
			}
			return false;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("matches shell which has skin composite child");
			
		}
		
	}