package org.jago.sassymaven.compiler;

import org.junit.Test;

public class SassCompilerTest {

	@Test
	public void testSassCompiler() {
		SassCompiler compiler = new SassCompiler();
		compiler.compile("src/test/resources", "src/test/resources", "sassTestfile.scss");
	}
}
