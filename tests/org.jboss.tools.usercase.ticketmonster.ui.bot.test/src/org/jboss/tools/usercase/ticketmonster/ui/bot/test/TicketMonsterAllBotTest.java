package org.jboss.tools.usercase.ticketmonster.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter10ForgeEntityTest;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter11ReviewPersistence;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter12AddNewEntity;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter13Deplyment;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter14REST;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter15JQuery;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter8CreateJavaEE6ProjectTest;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2.Chapter9ExploreApplicationTest;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part3.Chapter18FirstEntity;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.part3.Chapter19DatabaseDesign;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	
	Chapter8CreateJavaEE6ProjectTest.class,
	Chapter9ExploreApplicationTest.class,
	Chapter10ForgeEntityTest.class,
	Chapter11ReviewPersistence.class,
	Chapter12AddNewEntity.class,
	Chapter13Deplyment.class,
	Chapter14REST.class,
	Chapter15JQuery.class,

	Chapter18FirstEntity.class,
	Chapter19DatabaseDesign.class
	
})
public class TicketMonsterAllBotTest {

}
