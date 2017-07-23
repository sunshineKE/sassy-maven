package org.jago.sassymaven.compiler.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public class SassFileSystemUtils {
	private SassFileSystemUtils() {
	}

	public static File[] scanDirectoryForSassFiles(String path) {
		File[] files;

		File dir = new File(path);
		FileFilter fileFilter = new WildcardFileFilter("*.scss");
		files = dir.listFiles(fileFilter);

		return files;
	}

	public static File prepeareDestinationFile(String destinationDirectory, String sourceFileName) {
		String outfilePath = destinationDirectory + "/" + sourceFileName.replace(".scss", ".css");
		return new File(outfilePath);
	}
}
