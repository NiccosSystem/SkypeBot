package uk.niccossystem.skypebot.hook;

/**
 * To make hooks, extend this class.
 * 
 * @author NiccosSystem
 *
 */
public abstract class Hook {
	public String getName() {
		return this.getClass().getSimpleName();
	}
}