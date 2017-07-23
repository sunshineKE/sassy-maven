package org.jago.sassymaven.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public abstract class AbstractSassyMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {

		getLog().info("I've got my mojo working.");
	}
}
