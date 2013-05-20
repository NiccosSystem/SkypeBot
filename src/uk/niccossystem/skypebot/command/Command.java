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
	 * The command's name
	 * 
	 * @return
	 */
	public String name();
	
	/**
	 * The command's help text
	 * 
	 * @return
	 */
	public String help();
	
//	/**
//	 * The command's permission
//	 * 
//	 * @return
//	 */
//	public String permission();
	
}
