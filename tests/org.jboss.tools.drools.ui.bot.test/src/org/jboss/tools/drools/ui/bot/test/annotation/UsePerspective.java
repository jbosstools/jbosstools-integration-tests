package org.jboss.tools.drools.ui.bot.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UsePerspective {
    Class<? extends AbstractPerspective> value();
}
