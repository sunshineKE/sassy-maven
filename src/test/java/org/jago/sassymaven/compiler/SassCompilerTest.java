package org.jago.sassymaven.compiler;

import org.junit.Assert;
import org.junit.Test;

public class SassCompilerTest {

	@Test
	public void testSassCompiler() {
		SassCompiler compiler = new SassCompiler();
		compiler.compile("src/test/resources", "src/test/resources", "sassTestfile.scss");
	}

	@Test
	public void testSassCompilerSyntaxError() {
		SassCompiler compiler = new SassCompiler();
		try {
			compiler.compile("src/test/resources", "src/test/resources", "sassTestfileFailure.scss");
		} catch (SassCompilerException e) {
			Assert.assertTrue(true);
		}
	}
}
