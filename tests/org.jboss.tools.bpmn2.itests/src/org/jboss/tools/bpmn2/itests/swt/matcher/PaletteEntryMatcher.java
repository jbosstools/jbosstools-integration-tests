package org.jboss.tools.bpmn2.itests.swt.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gef.palette.PaletteEntry;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * match the label of a tool entry
 * 
 * @author David Green
 */
public class PaletteEntryMatcher extends BaseMatcher<PaletteEntry> {

	private final Pattern pattern;

	public PaletteEntryMatcher(String name) {
		this(Pattern.compile(Pattern.quote(name)));
	}
	
	public PaletteEntryMatcher(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * @see net.sf.swtbot.eclipse.gef.matchers.AbstractToolEntryMatcher#matches(org.eclipse.gef.palette.ToolEntry)
	 */
	protected boolean matches(final PaletteEntry paletteEntry) {
		final String label = paletteEntry.getLabel();
		if (label == null) {
			return false;
		}
		final Matcher matcher = pattern.matcher(label);
		return matcher.matches();
	}

	@Override
	public boolean matches(Object object) {
		if (object instanceof PaletteEntry) {
			return matches((PaletteEntry) object);
		}
		return false;
	}

	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
		
	}

}