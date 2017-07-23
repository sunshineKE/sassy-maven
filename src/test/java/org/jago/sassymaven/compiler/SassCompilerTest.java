package org.jago.sassymaven.compiler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SassCompilerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSassCompiler() {
		SassCompiler compiler = new SassCompiler();
		compiler.compile("src/test/resources", "src/test/resources", "sassTestfile.scss");
	}

	@Test
	public void testSassCompilerSyntaxError() {
		SassCompiler compiler = new SassCompiler();

		thrown.expect(SassCompilerException.class);
		
		compiler.compile("src/test/resources", "src/test/resources", "sassTestfileFailure.scss");
	}

	@Test
	public void testSassCompilerFileNotFound() {
		SassCompiler compiler = new SassCompiler();

		thrown.expect(SassCompilerException.class);

		compiler.compile("src/test/resources", "src/test/resources", "notexistingFile.scss");
	}
}
