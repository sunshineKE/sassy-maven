package org.jago.sassymaven.compiler;

import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

public class SassCompilerLogger {
	private Log logger;

	public SassCompilerLogger(Log logger) {
		this.logger = logger;
	}

	public void logException(SassCompilerException e) {
		logger.error(SassCompilerException.class.getName() + " " + e.getMessage());
	}

	public void logException(IOException e) {
		logger.error("" + " " + e);
	}

	public void logInfo(String infoText) {
		logger.info(infoText);
	}
}
