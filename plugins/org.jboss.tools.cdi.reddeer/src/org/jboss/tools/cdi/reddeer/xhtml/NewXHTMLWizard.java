/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.xhtml;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;


public class NewXHTMLWizard extends NewMenuWizard{
	
	public static final String CATEGORY="JBoss Tools Web";
	public static final String NAME="XHTML Page";
	public static final String SHELL_TEXT = "New XHTML Page";
	
	public NewXHTMLWizard(){
		super(SHELL_TEXT,CATEGORY,NAME);
	}

}
