package org.jago.sassymaven.mojo.util;

import org.jago.sassymaven.mojo.util.Const.FileChangeType;

public class WatchSassyMojoTestParameter {

	private String pom;
	private String srcBasePath;
	private String destFilePath;
	private FileChangeType fileAction;

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

	public String getPom() {
		return pom;
	}

	public void setPom(String pom) {
		this.pom = pom;
	}

	public String getSrcBasePath() {
		return srcBasePath;
	}

	public void setSrcBasePath(String srcBasePath) {
		this.srcBasePath = srcBasePath;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	public FileChangeType getFileAction() {
		return fileAction;
	}

	public void setFileAction(FileChangeType fileAction) {
		this.fileAction = fileAction;
	}

}