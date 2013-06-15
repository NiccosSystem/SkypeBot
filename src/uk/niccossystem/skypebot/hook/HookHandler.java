package uk.niccossystem.skypebot.hook;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that methods which take a Hook subclass
 * as a parameter. If your function does not use this
 * annotation, it will be ignored upon the call of the hook. * 
 * 
 * @author NiccosSystem
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HookHandler {
	
}