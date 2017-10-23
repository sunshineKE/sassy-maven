package org.jago.sassymaven.mojos;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jago.sassymaven.compiler.ISassCompiler;
import org.jago.sassymaven.compiler.SassCompiler;
import org.jago.sassymaven.compiler.SassCompilerLogger;

public abstract class AbstractSassyMojo extends AbstractMojo {

	ISassCompiler compiler = new SassCompiler(new SassCompilerLogger(getLog()));

	private final String RECURSIVE_WILDCARD = "/*"; 
	
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
	
	@Parameter
	protected List<DirectoryMapping> directories;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog();
	}
	
	/*
	 * Searches for placeholder "/*" wildcards in source
	 * directory entries and expands it recursively to
	 * actually existing paths 
	 */
	protected List<DirectoryMapping> expandDirectoryMappingSubdirs(List<DirectoryMapping> dirs) {	
		
		List<DirectoryMapping> dirsIterationCopy = new ArrayList<DirectoryMapping>(dirs);
		
		for (DirectoryMapping d : dirsIterationCopy) {
			String srcFolder = d.getSource();
			String destFolder = d.getDestination();
			if (srcFolder.endsWith(RECURSIVE_WILDCARD)) {
				srcFolder = srcFolder.substring(0, srcFolder.length() - RECURSIVE_WILDCARD.length());
				d.setSource(srcFolder);
				Path srcFolderAbs = Paths.get(srcFolder).toAbsolutePath();
				int foundSubfolders = 0;
				for (File f : new File(srcFolder).listFiles()) {
					if (f.isDirectory()) {
						foundSubfolders++;
						Path srcSubFolderAbs = f.toPath().toAbsolutePath();
						String destSubFolderRelative = srcSubFolderAbs.toString().substring(srcFolderAbs.toString().length());
						Path destSubFolderAbs = Paths.get(destFolder, destSubFolderRelative);
						srcSubFolderAbs = Paths.get(srcSubFolderAbs.toString(), RECURSIVE_WILDCARD);
						
						DirectoryMapping expandedSubdirMapping = new DirectoryMapping();
						expandedSubdirMapping.setSource(srcSubFolderAbs.toString());
						expandedSubdirMapping.setDestination(destSubFolderAbs.toString());
						dirs.add(expandedSubdirMapping);
					}
				}
				
				if (foundSubfolders > 0) {
					dirs = expandDirectoryMappingSubdirs(dirs);
				}
			}
		}
		
		return dirs;
	}
}
