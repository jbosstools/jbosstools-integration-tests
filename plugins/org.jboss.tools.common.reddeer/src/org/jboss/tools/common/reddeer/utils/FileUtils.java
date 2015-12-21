package org.jboss.tools.common.reddeer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {
	
	public static void deleteDirectory(File dir) throws IOException{
		if (!dir.exists()){
			return;
		}
		Files.walkFileTree(Paths.get(dir.getAbsolutePath()), new SimpleFileVisitor<Path>(){
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path directory, IOException exc) throws IOException {
				Files.delete(directory);
				return FileVisitResult.CONTINUE;
			}
		});
	}

}
