package com.maaryan.ml.conf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maaryan.fhi.commons.SleepUtil;
import com.maaryan.fhi.io.PathUtil;
import com.maaryan.fhi.task.FileMetaTaskRejectionHandler;
import com.maaryan.ml.excp.MediaLibraryException;
import com.maaryan.ml.task.MediaFileRejectionHandler;
import com.maaryan.ml.task.MediaScannerTask;

@Component
public class MediaUploader implements Runnable{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MediaLibraryConfig mediaLibraryConfig;
	private Set<Path> foldersToScan;
	
	protected ThreadPoolExecutor mediaScannerExecutor;
	protected ThreadPoolExecutor mediaFileExecutor;
	
	
	@PostConstruct
	public void init(){
		
		this.mediaScannerExecutor = new ThreadPoolExecutor(
				mediaLibraryConfig.getMediaScannerCorePoolSize(),
				mediaLibraryConfig.getMediaScannerMaxPoolSize(),
				mediaLibraryConfig.getMediaScannerKeepAliveTimeMsecs(),
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(
						mediaLibraryConfig.getMediaScannerQueueSize()));
		mediaScannerExecutor
				.setRejectedExecutionHandler(new FileMetaTaskRejectionHandler());
		mediaFileExecutor = new ThreadPoolExecutor(
				mediaLibraryConfig.getMediaFileCorePoolSize(),
				mediaLibraryConfig.getMediaFileMaxPoolSize(),
				mediaLibraryConfig.getMediaFileKeepAliveTimeMsecs(),
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(
						mediaLibraryConfig.getMediaFileQueueSize()));
		mediaFileExecutor
				.setRejectedExecutionHandler(new MediaFileRejectionHandler());
		Path mediaLibraryPath = Paths.get(mediaLibraryConfig.getMediaLibraryPath());
		if(!Files.exists(mediaLibraryPath)){
			try {
				Files.createDirectories(mediaLibraryPath);
			} catch (IOException e) {
				throw new MediaLibraryException(e);
			}
		}
		
	}
	
	public void run() {
		logger.info("MediaManager start");
		uploadMedia();
		logger.info("MediaManager end");
	}

	public void uploadMedia() {
		logger.info("UploadingMedia started..");
		if(foldersToScan==null || foldersToScan.size()==0){
			logger.info("No folders to scan.");
		}
		foldersToScan = PathUtil.filter(foldersToScan);
		for (Path f : foldersToScan) {
			mediaScannerExecutor.execute(new MediaScannerTask(f, mediaLibraryConfig,mediaFileExecutor));
		}
		while (mediaScannerExecutor.getActiveCount() != 0
				|| mediaFileExecutor.getActiveCount() != 0
				) {
			logger.debug("mediaScannerExecutor.getActiveCount():"
					+ mediaScannerExecutor.getActiveCount()
					+ " , fileMetaFeeder.getFileMetaTaskExecutor().getActiveCount():"
					+ mediaFileExecutor.getActiveCount()
					);
			SleepUtil.sleep(1000);
		}
		mediaScannerExecutor.shutdown();
		try {
			logger.info("Awaiting shutdown of mediaScannerExecutor");
			mediaScannerExecutor.awaitTermination(60, TimeUnit.MINUTES);
			logger.info("mediaScannerExecutor is shutted down");
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		mediaFileExecutor.shutdown();
		try {
			logger.info("Awaiting shutdown of fileMetaFeeder");
			mediaFileExecutor.awaitTermination(60,
					TimeUnit.MINUTES);
			logger.info("fileMetaFeeder is shutted down");
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("UploadingMedia done..");
		return;
	}

	public MediaLibraryConfig getMediaLibraryConfig() {
		return mediaLibraryConfig;
	}

	public void setFoldersToScan(Set<Path> foldersToScan) {
		this.foldersToScan = foldersToScan;
	}
	
}
