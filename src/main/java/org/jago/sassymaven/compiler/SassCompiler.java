package org.jago.sassymaven.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.jago.sassymaven.compiler.util.SassFileSystemUtils;

import com.cathive.sass.SassCompilationException;
import com.cathive.sass.SassContext;
import com.cathive.sass.SassFileContext;

/**
 *
 * @author Claudia Hirt
 *
 */
public class SassCompiler implements ISassCompiler {

	@Override
	public void compile(String sourceDirectory, String destinationDirectory) {
		File[] files = SassFileSystemUtils.scanDirectoryForSassFiles(sourceDirectory);
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				compileSingleFile(files[i], destinationDirectory);
			}
		} else {
			SassCompilerLogger.logInfo("No files found in directory " + sourceDirectory);
		}
		
		SassCompilerLogger.logInfo("------- Compilation finished -------");
	}


	private void compileSingleFile(File sourceFile, String destinationDirectory) {
		SassContext ctx = SassFileContext.create(sourceFile.toPath());

		File outfile = SassFileSystemUtils.prepeareDestinationFile(destinationDirectory, sourceFile.getName());
		FileOutputStream fos;

		try {
			SassCompilerLogger
					.logInfo("Compiling file " + sourceFile.getAbsolutePath() + " ==> " + destinationDirectory);

			if (!outfile.exists()) {
				outfile.createNewFile();
			}

			fos = new FileOutputStream(outfile);

			try {
				ctx.compile(fos);
			} catch (SassCompilationException e) {
				e.printStackTrace(new PrintStream(outfile));
				SassCompilerLogger.logException(new SassCompilerException(e.getStatus(), e.getMessage(),
						e.getFileName(), e.getLine(), e.getColumn(), e.getJson()));
			}
		} catch (IOException e) {
			SassCompilerLogger.logException(e);
		}
	}

}
