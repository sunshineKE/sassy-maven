package org.jago.sassymaven.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.jago.sassymaven.compiler.util.SassFileSystemUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SassCompilerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void cleanUpTestDirectory() {
		File[] files = SassFileSystemUtils.findFileByExtension("src/test/resources/validFiles", "*.css");

		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}

		files = SassFileSystemUtils.findFileByExtension("src/test/resources/invalidFiles", "*.css");

		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	@Test
	public void testSassCompiler() {
		SassCompiler compiler = new SassCompiler();
		compiler.compile("src/test/resources/validFiles", "src/test/resources/validFiles");

		File compiledFile = new File("src/test/resources/validFiles/sassTestfile.css");

		if (!compiledFile.exists()) {
			Assert.fail();
		}
	}

	@Test
	public void testSassCompilerSyntaxErrorInOneFile() {
		SassCompiler compiler = new SassCompiler();

		compiler.compile("src/test/resources/invalidFiles", "src/test/resources/invalidFiles");

		File compiledFile1 = new File("src/test/resources/invalidFiles/sassTestfileValid.css");

		if (!compiledFile1.exists()) {
			Assert.fail();
		}

		try {
			String fileContent = FileUtils.readFileToString(compiledFile1, Charset.defaultCharset());
			Assert.assertFalse(fileContent.contains("SassCompilationException"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File compiledFile2 = new File("src/test/resources/invalidFiles/sassTestfileInvalid.css");

		if (!compiledFile2.exists()) {
			Assert.fail();
		}

		try {
			String fileContent = FileUtils.readFileToString(compiledFile2, Charset.defaultCharset());
			Assert.assertTrue(fileContent.contains("SassCompilationException"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSassCompilerPathNotFound() {
		SassCompiler compiler = new SassCompiler();

		compiler.compile("xy/zz", "ab/cc");
	}
}
