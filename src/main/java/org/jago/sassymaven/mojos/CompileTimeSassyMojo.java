package org.jago.sassymaven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "update:stylesheets")
public class CompileTimeSassyMojo extends AbstractSassyMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		super.execute();
	}
}
