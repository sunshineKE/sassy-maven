package org.jago.sassymaven.mojo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.util.FileUtils;
import org.jago.sassymaven.mojo.util.Const;
import org.jago.sassymaven.mojo.util.Const.FileChangeType;
import org.jago.sassymaven.mojo.util.WatchMojoRunner;
import org.jago.sassymaven.mojo.util.WatchSassyMojoTestParameter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.io.Files;

@RunWith(Parameterized.class)
public class WatchSassyMojoTest {
	private ExecutorService execService = Executors.newCachedThreadPool();
	private WatchMojoRunner mojoRunner;
	private CountDownLatch mojoStarted;

	@Parameter
	public WatchSassyMojoTestParameter params;

	@Rule
	public MojoRule rule = new MojoRule();

	@Before
	public void before() {
		new File(params.getDestFilePath()).delete();
		try {
			FileUtils.cleanDirectory(params.getSrcBasePath());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		mojoStarted = new CountDownLatch(1);
		
		mojoRunner = new WatchMojoRunner(new File(params.getPom()), rule, mojoStarted);		
	}

	@After
	public void after() {
		try {
			mojoRunner.exit();
		} catch (InterruptedException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Parameters(name = "File {0}")
	public static Collection<Object> data() {
		return Arrays.asList(new Object[] {
				new WatchSassyMojoTestParameter(Const.testDirBasePath + "pom-watch.xml",
						Const.testDirBasePath + "input", Const.testDirBasePath + "output/sassTestFile.css",
						FileChangeType.Created),
				new WatchSassyMojoTestParameter(Const.testDirBasePath + "pom-watch.xml",
						Const.testDirBasePath + "input", Const.testDirBasePath + "output/sassTestFile.css",
						FileChangeType.Updated),
				new WatchSassyMojoTestParameter(Const.testDirBasePath + "pom-watch.xml",
						Const.testDirBasePath + "input", Const.testDirBasePath + "output/sassTestFile.css",
						FileChangeType.Deleted) });
	}

	@Test
	public void testWatchMojo() {

		File pom = new File(params.getPom());
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		try {
			File destFile = new File(params.getDestFilePath());

			switch (params.getFileAction()) {
			case Created:
				execService.execute(mojoRunner);
				mojoStarted.await();
				createSourceFileFromDestFile(params.getDestFilePath(), params.getSrcBasePath());
				Thread.sleep(1000);
				mojoRunner.throwExceptionIfAvailable();

				Assert.assertTrue(destFile.exists());

				break;
			case Updated:
				createSourceFileFromDestFile(params.getDestFilePath(), params.getSrcBasePath());
				execService.execute(mojoRunner);
				mojoStarted.await();
				String sourceFilePath = getScssFileFromDestFile(params.getDestFilePath());
				(new File(sourceFilePath)).setLastModified(System.currentTimeMillis());
				Thread.sleep(1000);
				mojoRunner.throwExceptionIfAvailable();

				Assert.assertTrue(destFile.exists());

				break;
			case Deleted:
				createSourceFileFromDestFile(params.getDestFilePath(), params.getSrcBasePath());
				execService.execute(mojoRunner);
				mojoStarted.await();
				sourceFilePath = getScssFileFromDestFile(params.getDestFilePath());
				(new File(sourceFilePath)).delete();
				Thread.sleep(1000);
				mojoRunner.throwExceptionIfAvailable();

				Assert.assertTrue(destFile.exists());

				break;
			default:
				Assert.fail();
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
		String scssTemplateFile = params.getSrcBasePath() + "Template/" + scssFileName;
		String scssFile = params.getSrcBasePath() + "/" + scssFileName;

		Files.copy(new File(scssTemplateFile), new File(scssFile));
	}
}