package org.jago.sassymaven.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jago.sassymaven.compiler.util.PathUtils;

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
		File[] files = PathUtils.scanForSassFiles(sourceDirectory);
		
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

		String outfilePath = destinationDirectory + "/" + sourceFile.getName().replace(".scss", ".css");

		File outfile = new File(outfilePath);

		try {
			SassCompilerLogger
					.logInfo("Compiling file " + sourceFile.getAbsolutePath() +
			 " ==> " + destinationDirectory);
			
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(outfile);

			ctx.compile(fos);
		} catch (IOException e) {
			SassCompilerLogger.logException(e);
		} catch (SassCompilationException e) {
			// TODO write stack trace to destination file
			SassCompilerLogger.logException(new SassCompilerException(e.getStatus(), e.getMessage(), e.getFileName(),
					e.getLine(), e.getColumn(), e.getJson()));
		}
	}

}
