package org.jboss.tools.hb.ui.bot.test.generation;

import org.junit.Test;

/**
 * Hibernate code generation configuration ui bot test
 * - Code generation configuration can be created
 * - Source code can be generated via hibernate generation configuration
 * @author jpeterka
 * 
 */
public class CreateCodeGenerationConfiguration40 extends CreateCodeGenerationConfiguration {

	@Test
	public void hibernateCodeGeneration40() {
		setPrjName("hibernate40");
		hibernateCodeGeneration();
	}
}