/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge2.ui.bot.test.suite;


import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.forge2.ui.bot.wizard.test.CDISetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ConnectionProfileWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ConstraintSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.EJBSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.FacesSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPAEntitiesFromTablesTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPAEntityWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPAFieldWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPASetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ProjectNewWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.RESTSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ScaffoldSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ScaffoldWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ServletSetupWizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * 
 * @author psrna
 *
 */
@SuiteClasses({
	ProjectNewWizardTest.class,
	JPASetupWizardTest.class,
	ServletSetupWizardTest.class,
	ConstraintSetupWizardTest.class,
	FacesSetupWizardTest.class,
	RESTSetupWizardTest.class,
	EJBSetupWizardTest.class,
	JPAEntityWizardTest.class,
	JPAFieldWizardTest.class,
	ScaffoldSetupWizardTest.class,
	ScaffoldWizardTest.class,
	CDISetupWizardTest.class,
	ConnectionProfileWizardTest.class,
	JPAEntitiesFromTablesTest.class
})
@RunWith(RedDeerSuite.class)
public class Forge2AllTest {
}
