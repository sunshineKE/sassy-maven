package org.jago.sassymaven.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.MojoRule;
import org.jago.sassymaven.mojos.CompileTimeSassyMojo;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;


public class CompileTimeSassyMojoTest /* extends AbstractMojoTestCase */ {

	File pom = null;

	@Rule
	public MojoRule rule = new MojoRule();

	private void runCompiler(File pom) throws MojoExecutionException, MojoFailureException, Exception {
		CompileTimeSassyMojo ctMojo = new CompileTimeSassyMojo();
		ctMojo = (CompileTimeSassyMojo) rule.configureMojo(ctMojo,
				rule.extractPluginConfiguration("sassy-maven-plugin", pom));

		ctMojo.execute();
	}

	@Test
	public void executeCompileTimeMojo1to1() {

		File pom = new File("src/test/resources/integrationtest/1-to-1/pom-1-to-1.xml");
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		try {
			runCompiler(pom);
		}
		catch (MojoExecutionException e) {
			Assert.assertNull(e);
		} catch (MojoFailureException e) {
			Assert.assertNull(e);
		} catch (Exception e) {
			Assert.assertNull(e);
		}

		File compiledFile = new File("src/test/resources/integrationtest/1-to-1/output/sassTestFile.css");

		Assert.assertTrue(compiledFile.exists());
	}

	@Test
	public void executeCompileTimeMojo_n_to_n() {

		File pom = new File("src/test/resources/integrationtest/n-to-n/pom-n-to-n.xml");
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		try {
			runCompiler(pom);
		} catch (MojoExecutionException e) {
			Assert.assertNull(e);
		} catch (MojoFailureException e) {
			Assert.assertNull(e);
		} catch (Exception e) {
			Assert.assertNull(e);
		}

		File compiledFile1 = new File("src/test/resources/integrationtest/n-to-n/output1/sassTestFile.css");
		File compiledFile2 = new File("src/test/resources/integrationtest/n-to-n/output2/sassTestFile.css");

		Assert.assertTrue(compiledFile1.exists());
		Assert.assertTrue(compiledFile2.exists());
	}

}
