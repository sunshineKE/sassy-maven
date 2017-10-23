package org.jago.sassymaven.mojos;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "update")
public class CompileSassyMojo extends AbstractSassyMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		super.execute();

		List<DirectoryMapping> expandedDirs = expandDirectoryMappingSubdirs(directories);
		
		for (DirectoryMapping d : expandedDirs) {
			compiler.compile(d.getSource(), d.getDestination());
		}
	}
}
