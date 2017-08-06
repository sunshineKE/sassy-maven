package org.jago.sassymaven.mojo.util;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.apache.maven.plugin.testing.MojoRule;
import org.jago.sassymaven.mojos.WatchSassyMojo;

public class WatchMojoRunner extends Thread {

	WatchSassyMojo watchMojo;
	File pom;
	MojoRule rule;
	Exception ex;
	CountDownLatch mojoStarted;

	public WatchMojoRunner(File pom, MojoRule rule, CountDownLatch mojoStarted) {
		this.pom = pom;
		this.rule = rule;
		this.mojoStarted = mojoStarted;
	}

	@Override
	public void run() {
		try {
			watchMojo = new WatchSassyMojo();
			watchMojo = (WatchSassyMojo) rule.configureMojo(watchMojo,
					rule.extractPluginConfiguration("sassy-maven-plugin", pom));
			mojoStarted.countDown();
			watchMojo.execute();

		} catch (Exception e) {
			ex = e;
		}
	}

	public void exit() throws InterruptedException {
		watchMojo.stop();
		this.join();
	}

	public void throwExceptionIfAvailable() throws Exception {
		if (ex != null) {
			throw ex;
		}
	}
}