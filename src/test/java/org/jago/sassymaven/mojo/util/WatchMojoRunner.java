package org.jago.sassymaven.mojo.util;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.jago.sassymaven.mojos.WatchSassyMojo;

public class WatchMojoRunner extends Thread {

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