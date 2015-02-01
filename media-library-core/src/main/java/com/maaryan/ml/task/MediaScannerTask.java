package com.maaryan.ml.task;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maaryan.ml.conf.MediaLibraryConfig;

public class MediaScannerTask implements Runnable, FileVisitor<Path> {
	private Logger logger = LoggerFactory.getLogger(MediaScannerTask.class);
	private final Path folderToScan;
	private final MediaLibraryConfig mediaLibrarayConfig;
	private final ThreadPoolExecutor mediaFileExecutor;

	public MediaScannerTask(Path folderToScan,
			MediaLibraryConfig mediaLibrarayConfig,
			ThreadPoolExecutor mediaFileExecutor) {
		this.folderToScan = folderToScan;
		this.mediaLibrarayConfig = mediaLibrarayConfig;
		this.mediaFileExecutor = mediaFileExecutor;
	}

	@Override
	public void run() {
		logger.info("FolderScanner task Started");
		scanFolder();
		logger.info("FolderScanner task Done");
	}

	public void scanFolder() {
		logger.info("Scanning folder " + folderToScan);
		if (!Files.exists(folderToScan)) {
			logger.warn("Folder not found " + folderToScan);
		}
		try {
			Files.walkFileTree(folderToScan, this);
			logger.info("Scanning done for folder " + folderToScan);
		} catch (IOException e) {
			logger.error("Error while scanning " + folderToScan, e);
		}
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		logger.info("media previsit " + dir);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {

		if (mediaLibrarayConfig.getImageFilter().accept(file)) {
			mediaFileExecutor.execute(new MediaFileTask(mediaLibrarayConfig
					.getImageStore(), file, mediaLibrarayConfig));
			logger.debug("Submitted media to imageTask: " + file);
		} else if (mediaLibrarayConfig.getVideoFilter().accept(file)) {
			mediaFileExecutor.execute(new MediaFileTask(mediaLibrarayConfig
					.getVideoStore(), file, mediaLibrarayConfig));
			logger.debug("Submitted media to videoTask: " + file);
		} else if (mediaLibrarayConfig.getAudioFilter().accept(file)) {
			mediaFileExecutor.execute(new MediaFileTask(mediaLibrarayConfig
					.getAudioStore(), file, mediaLibrarayConfig));
			logger.debug("Submitted media to audioTask: " + file);
		} else {
			logger.debug("No media match: " + file);
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		logger.warn("media visitFileFailed: " + file, exc);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		logger.info("media postvisit " + dir);
		try {
			Files.deleteIfExists(dir);
		} catch (Exception e) {
			logger.info("Unable to delete folder. " + dir);
		}
		return FileVisitResult.CONTINUE;
	}

}
