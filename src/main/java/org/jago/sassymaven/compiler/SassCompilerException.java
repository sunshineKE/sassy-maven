package org.jago.sassymaven.compiler;

import com.cathive.sass.SassCompilationException;

/**
 * @author Claudia Hirt
 */
public class SassCompilerException extends RuntimeException{

	public SassCompilerException(final int status, final String message, final String fileName, final int line,
			final int column, final String json) {
		delegate = new SassCompilationException(status, message, fileName, line, column, json);
	}

	private SassCompilationException delegate;
	
	public int getStatus() {
		return delegate.getStatus();
	}

	public String getFileName() {
		return delegate.getFileName();
	}

	public int getLine() {
		return delegate.getLine();
	}

	public int getColumn() {
		return delegate.getColumn();
	}

	public String getJson() {
		return delegate.getJson();
	}

	@Override
	public String getMessage() {
		return delegate.getMessage();
	}

}
