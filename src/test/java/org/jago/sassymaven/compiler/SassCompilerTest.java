package org.jago.sassymaven.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.jago.sassymaven.compiler.util.SassFileSystemUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SassCompilerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	@AfterClass
	public static void cleanUpTestDirectory() {
		File[] files = SassFileSystemUtils.findFileByExtension("src/test/resources/unittest/validFiles", "*.css");

		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}

		files = SassFileSystemUtils.findFileByExtension("src/test/resources/unittest/invalidFiles", "*.css");

		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}
	

	@Test
	public void testSassCompiler() {
		SassCompiler compiler = new SassCompiler(new SassCompilerLogger(new SystemStreamLog()));
		compiler.compile("src/test/resources/unittest/validFiles", "src/test/resources/unittest/validFiles");

		File compiledFile = new File("src/test/resources/unittest/validFiles/sassTestfile.css");

		if (!compiledFile.exists()) {
			Assert.fail();
		}
	}

	@Test
	public void testSassCompilerSyntaxErrorInOneFile() {
		SassCompiler compiler = new SassCompiler(new SassCompilerLogger(new SystemStreamLog()));

		compiler.compile("src/test/resources/unittest/invalidFiles", "src/test/resources/unittest/invalidFiles");

		File compiledFile1 = new File("src/test/resources/unittest/invalidFiles/sassTestfileValid.css");

		if (!compiledFile1.exists()) {
			Assert.fail();
		}

		try {
			String fileContent = FileUtils.readFileToString(compiledFile1, Charset.defaultCharset());
			Assert.assertFalse(fileContent.contains("SassCompilationException"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File compiledFile2 = new File("src/test/resources/unittest/invalidFiles/sassTestfileInvalid.css");

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
		SassCompiler compiler = new SassCompiler(new SassCompilerLogger(new SystemStreamLog()));

		compiler.compile("xy/zz", "ab/cc");
	}
}
