package org.jago.sassymaven.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	public void compile(String sourceDirectory, String destinationDirectory, String sourcefileName)
			throws SassCompilerException {
		Path srcRoot = Paths.get(sourceDirectory);

		SassContext ctx = SassFileContext.create(srcRoot.resolve(sourcefileName));

		String outfilePath = destinationDirectory + "/" + sourcefileName.replace(".scss", ".css");

		File outfile = new File(outfilePath);

		try {
			if (!outfile.exists()) {
				System.out.println("Creating file " + outfile.getAbsolutePath());
				outfile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(outfile);

			ctx.compile(fos);
			
			System.out.println("Compiled to file " + outfile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (SassCompilationException e) {
			throw new SassCompilerException(e.getStatus(), e.getMessage(), e.getFileName(), e.getLine(), e.getColumn(),
					e.getJson());
		}
	}

}
