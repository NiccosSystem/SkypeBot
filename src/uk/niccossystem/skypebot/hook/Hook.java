package uk.niccossystem.skypebot.hook;

public abstract class Hook {
	public String getName() {
		return this.getClass().getSimpleName();
	}
}