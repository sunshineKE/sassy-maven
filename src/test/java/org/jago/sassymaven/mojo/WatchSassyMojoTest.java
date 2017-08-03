package org.jago.sassymaven.mojo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.testing.MojoRule;
import org.jago.sassymaven.mojos.WatchSassyMojo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.io.Files;

enum FileChangeType {
	Created, Updated, Deleted
}

class WatchSassyMojoTestParameter {

	public String pom, srcBasePath, destFilePath;
	FileChangeType fileAction;

	public WatchSassyMojoTestParameter(String pom, String srcBasePath, String destFilePath, FileChangeType fileAction) {
		this.pom = pom;
		this.srcBasePath = srcBasePath;
		this.destFilePath = destFilePath;
		this.fileAction = fileAction;
	}

	@Override
	public String toString() {
		return fileAction.toString();
	}
}

// public class WatchServiceThread extends Thread {
// ;
// }

@RunWith(Parameterized.class)
public class WatchSassyMojoTest {

	Path destPath;
	ExecutorService execService = Executors.newCachedThreadPool();
	WatchMojoRunner mojoRunner;

	@Parameter
	public WatchSassyMojoTestParameter params;

	@Rule
	public MojoRule rule = new MojoRule();

	@Before
	public void before() {
		new File(params.destFilePath).delete();

		String tmp = (new File(params.destFilePath)).getParent();
		destPath = Paths.get(tmp);

		mojoRunner = new WatchMojoRunner(new File(params.pom), rule);
	}

	@After
	public void after() {
		destPath = null;
		try {
			mojoRunner.exit();
		} catch (InterruptedException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Parameters(name = "File {0}")
	public static Collection<Object> data() {
		return Arrays.asList(new Object[] {
				new WatchSassyMojoTestParameter("src/test/resources/integrationtest/watch/pom-watch.xml",
						"src/test/resources/integrationtest/watch/input",
						"src/test/resources/integrationtest/watch/output/sassTestFile.css", FileChangeType.Created),
				new WatchSassyMojoTestParameter("src/test/resources/integrationtest/watch/pom-watch.xml",
						"src/test/resources/integrationtest/watch/input",
						"src/test/resources/integrationtest/watch/output/sassTestFile.css", FileChangeType.Updated),
				new WatchSassyMojoTestParameter("src/test/resources/integrationtest/watch/pom-watch.xml",
						"src/test/resources/integrationtest/watch/input",
						"src/test/resources/integrationtest/watch/output/sassTestFile.css", FileChangeType.Deleted) });
	}

	@Test
	@Ignore("Not running, there's a threading problem.")
	public void testWatchMojo() {

		File pom = new File(params.pom);
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		try {
			switch (params.fileAction) {
			case Created: {

				execService.execute(mojoRunner);
				createSourceFileFromDestFile(params.destFilePath, params.srcBasePath);
				Thread.sleep(15000);
				mojoRunner.throwExceptionIfAvailable();

				File destFile = new File(params.destFilePath);
				Assert.assertTrue(destFile.exists());

				break;
			}
			case Updated: {

				createSourceFileFromDestFile(params.destFilePath, params.srcBasePath);
				execService.execute(mojoRunner);
				String sourceFilePath = getScssFileFromDestFile(params.destFilePath);
				(new File(sourceFilePath)).setLastModified(System.currentTimeMillis());
				Thread.sleep(15000);
				mojoRunner.throwExceptionIfAvailable();

				File destFile = new File(params.destFilePath);
				Assert.assertTrue(destFile.exists());

				break;
			}
			case Deleted: {

				createSourceFileFromDestFile(params.destFilePath, params.srcBasePath);
				execService.execute(mojoRunner);
				String sourceFilePath = getScssFileFromDestFile(params.destFilePath);
				(new File(sourceFilePath)).delete();
				Thread.sleep(15000);
				mojoRunner.throwExceptionIfAvailable();

				File destFile = new File(params.destFilePath);
				Assert.assertTrue(destFile.exists());

				break;
			}
			}
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	private String getScssFileFromDestFile(String destFilePath) {
		String cssFileName = (new File(destFilePath)).getName();
		return cssFileName.replace(".css", ".scss");
	}

	private void createSourceFileFromDestFile(String destFilePath, String srcBasePath) throws IOException {

		String scssFileName = getScssFileFromDestFile(destFilePath);
		String scssTemplateFile = params.srcBasePath + "Template/" + scssFileName;
		String scssFile = params.srcBasePath + "/" + scssFileName;

		Files.copy(new File(scssTemplateFile), new File(scssFile));
	}
}

class WatchMojoRunner extends Thread {

	WatchSassyMojo watchMojo;
	File pom;
	MojoRule rule;
	Exception ex;

	public WatchMojoRunner(File pom, MojoRule rule) {
		this.pom = pom;
		this.rule = rule;
	}

	@Override
	public void run() {
		try {
			watchMojo = new WatchSassyMojo();
			watchMojo = (WatchSassyMojo) rule.configureMojo(watchMojo,
					rule.extractPluginConfiguration("sassy-maven-plugin", pom));
			watchMojo.execute();
			
		} catch (Exception e) {
			ex = e;
		}
	}

	public void exit() throws InterruptedException {
		watchMojo.stop();
		Thread.currentThread().interrupt();
		this.join();
	}

	public void throwExceptionIfAvailable() throws Exception {
		if (ex != null) {
			throw ex;
		}
	}
}