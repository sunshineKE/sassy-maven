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

	private SassCompilerLogger compilerLogger;

	public SassCompiler(SassCompilerLogger logger) {
		this.compilerLogger = logger;
	}

	@Override
	public void compile(String sourceDirectory, String destinationDirectory) {
		File[] files = SassFileSystemUtils.findFileByExtension(sourceDirectory, "*.scss");
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// do not compile files which start with "_"
				if (!files[i].getName().startsWith("_")) {
					compileSingleFile(files[i], destinationDirectory);
				}
			}
		} else {
			compilerLogger.logInfo("No files found in directory " + sourceDirectory);
		}
		
		compilerLogger.logInfo("------- Compilation finished -------");
	}


	private void compileSingleFile(File sourceFile, String destinationDirectory) {
		SassContext ctx = SassFileContext.create(sourceFile.toPath());

		File outfile = SassFileSystemUtils.prepareDestinationFile(destinationDirectory, sourceFile.getName());

		FileOutputStream fos;

		try {
			compilerLogger
					.logInfo("Compiling file " + sourceFile.getAbsolutePath() + " ==> " + destinationDirectory);

			if (!outfile.exists()) {
				outfile.createNewFile();
			}

			fos = new FileOutputStream(outfile);

			try {
				ctx.compile(fos);
			} catch (SassCompilationException e) {
				e.printStackTrace(new PrintStream(outfile));
				compilerLogger.logException(new SassCompilerException(e.getStatus(), e.getMessage(),
						e.getFileName(), e.getLine(), e.getColumn(), e.getJson()));
			}
		} catch (IOException e) {
			compilerLogger.logException(e);
		}
	}

}
