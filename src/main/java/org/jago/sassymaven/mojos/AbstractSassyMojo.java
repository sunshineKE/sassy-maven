package org.jago.sassymaven.mojos;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jago.sassymaven.compiler.ISassCompiler;
import org.jago.sassymaven.compiler.SassCompiler;

public abstract class AbstractSassyMojo extends AbstractMojo {

	ISassCompiler compiler = new SassCompiler();

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
	
	@Parameter
	protected List<DirectoryMapping> directories;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		;
	}
}
