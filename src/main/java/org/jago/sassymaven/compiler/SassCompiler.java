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
	public void compile(String sourceDirectory, String destinationDirectory)
			throws SassCompilerException {
		File[] files = PathUtils.scanForSassFiles(sourceDirectory);
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				compileSingleFile(files[i], destinationDirectory);
			}
		} else {
			System.out.println("No files detected for directory " + sourceDirectory);
		}
	}

	private void compileSingleFile(File sourceFile, String destinationDirectory) {
		SassContext ctx = SassFileContext.create(sourceFile.toPath());

		String outfilePath = destinationDirectory + "/" + sourceFile.getName().replace(".scss", ".css");

		File outfile = new File(outfilePath);

		try {
			System.out.println("Compiling file " + sourceFile.getAbsolutePath() +
			 " ==> " + destinationDirectory);
			
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(outfile);

			ctx.compile(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SassCompilationException e) {
			throw new SassCompilerException(e.getStatus(), e.getMessage(), e.getFileName(), e.getLine(), e.getColumn(),
					e.getJson());
		}
	}

}
