package org.jago.sassymaven.compiler;

/**
 * 
 * @author Claudia Hirt
 *
 *         Interface for calling java-sass compiler
 */
public interface ISassCompiler {
	/**
	 * compile a file from source directory to destination directory destination
	 * file name is the same as source file name but with ending *.css instead
	 * of *.scss
	 * 
	 * @param sourceDirectory
	 * @param destinationDirectory
	 */
	public void compile(String sourceDirectory, String destinationDirectory);
}
