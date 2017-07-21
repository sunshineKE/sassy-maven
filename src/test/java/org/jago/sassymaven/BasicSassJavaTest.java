package org.jago.sassymaven;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.cathive.sass.SassCompilationException;
import com.cathive.sass.SassContext;
import com.cathive.sass.SassFileContext;

public class BasicSassJavaTest {
	
	@Test
	public void testSassCompiler(){
		System.out.println("Start test SASS compiler");
		Path srcRoot = Paths.get("./src/test/resources");
		SassContext ctx = SassFileContext.create(srcRoot.resolve("sassTestfile.scss"));
		try {
            ctx.compile(System.out);
        } catch (SassCompilationException e) {
            // Will print the error code, filename, line, column and the message provided
            // by libsass to the standard error output.
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(String.format("Compilation failed: %s", e.getMessage()));
        }
	}
}
