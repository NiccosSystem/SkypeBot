/**
 * 
 */
package uk.niccossystem.skypebot.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Command annotation. All command methods should use this.
 * 
 * @author NiccosSystem
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
	
	/**
	 * The name of the command, what phrase should call the command?
	 * 
	 * @return the command's name
	 */
	public String name();
	
	/**
	 * The help text for the command, what the "commands" command will show
	 * 
	 * @return the command's help text
	 */
	public String help();
	
}
