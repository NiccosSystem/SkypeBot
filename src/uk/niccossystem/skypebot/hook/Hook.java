package uk.niccossystem.skypebot.hook;

/**
 * Base Hook class. Extend this to make new classes!
 * 
 * @author NiccosSystem
 *
 */
public abstract class Hook {
	public String getName() {
		return this.getClass().getSimpleName();
	}
}