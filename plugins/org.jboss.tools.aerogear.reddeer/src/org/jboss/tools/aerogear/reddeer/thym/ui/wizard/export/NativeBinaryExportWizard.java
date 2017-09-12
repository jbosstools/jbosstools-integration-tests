/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.thym.ui.wizard.export;

import org.eclipse.reddeer.eclipse.selectionwizard.ExportMenuWizard;


/**
 * RedDeer implementation of Export Mobile Application Wizard
 * @author Vlado Pakan
 *
 */
public class NativeBinaryExportWizard extends ExportMenuWizard{
  
  public NativeBinaryExportWizard (){
	  super("Export Mobile Application", "Mobile", "Export Mobile Application");
  }

}
