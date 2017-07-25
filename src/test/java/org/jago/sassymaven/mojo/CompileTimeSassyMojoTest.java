package org.jago.sassymaven.mojo;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.MojoRule;
import org.jago.sassymaven.mojos.CompileTimeSassyMojo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;



class TestParameter {
	
	public String pom;
	public List<String> compiledFiles;
	
	public TestParameter(String pom, List<String> compiledFiles) {
		this.pom = pom;
		this.compiledFiles = compiledFiles;
	}
}


@RunWith(Parameterized.class)
public class CompileTimeSassyMojoTest {

	@Parameter
	public TestParameter params;
	
	@Rule
	public MojoRule rule = new MojoRule();

	@Before
	public void before() {
		for(String f: params.compiledFiles) {
			new File(f).delete();
		}
	}

	@After
	public void after() {
		;
	}

	@Parameters
	public static Collection<Object> data() {
		return Arrays.asList( new Object[] {
			new TestParameter(
				"src/test/resources/integrationtest/1-to-1/pom-1-to-1.xml",
				Arrays.asList( 
					 "src/test/resources/integrationtest/1-to-1/output/sassTestFile.css" 
					)
				),
			new TestParameter(
				"src/test/resources/integrationtest/n-to-n/pom-n-to-n.xml",
				Arrays.asList( 
					"src/test/resources/integrationtest/n-to-n/output1/sassTestFile.css",
					"src/test/resources/integrationtest/n-to-n/output2/sassTestFile.css"
					)
				)
			}
		);
	}

	private void runCompiler(File pom) throws MojoExecutionException, MojoFailureException, Exception {
		CompileTimeSassyMojo ctMojo = new CompileTimeSassyMojo();
		ctMojo = (CompileTimeSassyMojo) rule.configureMojo(ctMojo,
				rule.extractPluginConfiguration("sassy-maven-plugin", pom));

		ctMojo.execute();
	}
			
	@Test
	public void executeCompileTimeMojoParameterized() {

		File pom = new File(params.pom);
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

		
		for(String f: params.compiledFiles) {
			Assert.assertTrue(new File(f).exists());
		}
	}

}
