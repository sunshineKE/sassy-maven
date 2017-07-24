package org.jago.sassymaven.compiler;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SassCompilerLogger {
	public static Logger logger = LogManager.getRootLogger();

	public static void logException(SassCompilerException e) {
		logger.error(SassCompilerException.class.getName() + " " + e.getMessage());
	}

	public static void logException(IOException e) {
		logger.error("" + " " + e);
	}

	public static void logInfo(String infoText) {
		logger.info(infoText);
	}
}
