package org.jago.sassymaven.mojos;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "watch")
public class WatchSassyMojo extends AbstractSassyMojo {

	Boolean stopped = false;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		HashMap<String, String> sourceToDestDirectory = new HashMap<String, String>();

		super.execute();
		// TODO Intitially run the CompileSassyMojo

		try {

			WatchService watcher = FileSystems.getDefault().newWatchService();

			for (DirectoryMapping d : directories) {
				Path sourcePath = Paths.get(d.getSource());
				sourcePath.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
				sourceToDestDirectory.put(d.getSource().toString(), d.getDestination());
				compiler.compile(d.getSource(), d.getDestination());		
			}

			stopped = false;
			
			while(!stopped) {
				WatchKey key = watcher.take();

				for (WatchEvent<?> event : key.pollEvents()) {
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path item = ev.context();
					if (item != null && item.toString().endsWith(".scss")) {
						Path sourceDir = (Path) key.watchable();
						String destDir = sourceToDestDirectory.get(sourceDir.toString().replace("\\", "/"));
						compiler.compile(sourceDir.toString(), destDir);
					}
				}

				key.reset();
			}
		} catch (IOException | InterruptedException e) {
			getLog().error("Failure while watching directories." , e);
		}
		catch (Exception e) {
			getLog().error("", e);
		}
		finally
		{
			Thread.currentThread().interrupt();			
		}
		
		return;
	}
	
	public void stop() {
		stopped = true;
	}
}
