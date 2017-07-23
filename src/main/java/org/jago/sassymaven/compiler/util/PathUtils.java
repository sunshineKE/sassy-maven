package org.jago.sassymaven.compiler.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public class PathUtils {
	private PathUtils() {
	}

	public static File[] scanForSassFiles(String path) {
		File[] files;

		File dir = new File(path);
		FileFilter fileFilter = new WildcardFileFilter("*.scss");
		files = dir.listFiles(fileFilter);

		return files;
	}
}
