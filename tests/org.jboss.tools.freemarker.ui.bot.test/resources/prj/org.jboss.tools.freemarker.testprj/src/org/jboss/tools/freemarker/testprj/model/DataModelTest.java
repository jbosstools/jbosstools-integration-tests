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
package org.jboss.tools.freemarker.testprj.model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import freemarker.template.Configuration;
import freemarker.template.Template;

public class DataModelTest {
	
	public static void main(String[] args) throws IOException {
		DataModelTest test = new DataModelTest();
		test.perform();
	}

	public void perform() {
		// Add the values in the datamodel
		Map<String, List<User>> datamodel = new HashMap<String,List<User>>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1989);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		List<User> userList = new ArrayList<User>();
		userList.add(new User("John", "Smith", 27, true, cal.getTime()));
		cal.set(Calendar.YEAR, 1990);
		userList.add(new User("Alan", "Baker", 28, true, cal.getTime()));
		cal.set(Calendar.YEAR, 1991);
		userList.add(new User("Amy", "Tailor", 29, false, cal.getTime()));
		//datamodel.put("user", new User("Jarda", "Novak", 27, true, cal.getTime()));
		datamodel.put("users", userList);
		try {
			freemarkerDo(datamodel, "data-model.ftl");
		}

		catch (Exception e) {
			System.out.println("Exception");
			System.out.println(e.getLocalizedMessage());
		}
	}

	public void freemarkerDo(Map<String, List<User>> datamodel, String template) throws Exception {
		Configuration cfg = new Configuration(Configuration.getVersion());
		cfg.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + "/resource"));
		Template tpl = cfg.getTemplate(template);
		OutputStreamWriter output = new OutputStreamWriter(System.out);
		tpl.process(datamodel, output);
	}

}
