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

	@Test
	public void executeCompileTimeMojo() {

		File pom = new File("src/test/resources/validFiles/pom_1to1.xml");
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		try {
			
			CompileTimeSassyMojo ctMojo = new CompileTimeSassyMojo();
			ctMojo = (CompileTimeSassyMojo) rule.configureMojo(ctMojo,
					rule.extractPluginConfiguration("sassy-maven-plugin", pom));

			ctMojo.execute();
		}
		catch (MojoExecutionException e) {
			;
		} catch (MojoFailureException e) {
			;
		} catch (Exception e) {
			;
		}

	}
}
