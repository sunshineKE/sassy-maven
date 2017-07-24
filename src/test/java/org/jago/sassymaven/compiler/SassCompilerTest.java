package org.jago.sassymaven.compiler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SassCompilerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// TODO assertions for compiled files (check for existance)

	@Test
	public void testSassCompiler() {
		SassCompiler compiler = new SassCompiler();
		compiler.compile("src/test/resources/validFiles", "src/test/resources/validFiles");
	}

	@Test
	public void testSassCompilerSyntaxErrorInOneFile() {
		SassCompiler compiler = new SassCompiler();

		thrown.expect(SassCompilerException.class);
		
		compiler.compile("src/test/resources/invalidFiles", "src/test/resources/invalidFiles");
	}

	@Test
	public void testSassCompilerPathNotFound() {
		SassCompiler compiler = new SassCompiler();

		compiler.compile("xy/zz", "ab/cc");
	}
}
