package org.jboss.tools.switchyard.reddeer.component;

/**
 * An interface which ensures creating a component.
 * 
 * @author apodhrad
 * 
 * @param <T>
 */
public interface ComponentBuilder<T> {

	T create();
}
