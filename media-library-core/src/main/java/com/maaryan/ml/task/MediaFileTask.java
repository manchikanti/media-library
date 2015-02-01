package com.maaryan.ml.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maaryan.ml.conf.MediaLibraryConfig;
import com.maaryan.ml.excp.MediaDuplicateException;
import com.maaryan.ml.excp.MediaRejectedException;
import com.maaryan.ml.store.MediaStore;

public class MediaFileTask implements Runnable {
	private Logger logger;
	private MediaStore mediaStore;
	private Path file;
	private MediaLibraryConfig mediaLibraryConfig;
	private MediaFileTask(){
		logger = LoggerFactory.getLogger(this.getClass());
	}
	public MediaFileTask(MediaStore mediaStore, Path file, MediaLibraryConfig config) {
		this();
		this.mediaStore = mediaStore;
		this.mediaLibraryConfig = config;
		this.file = file;
	}

	@Override
	public void run() {
		logger.debug("Adding file to media store: " + mediaStore.getMediaStorePath());
		try {
			mediaStore.addFile(file);
			logger.debug("Added file to media store: " + mediaStore.getMediaStorePath());
			try {
				Files.deleteIfExists(file);
				logger.info("File added and deleted. "+file);
			} catch (IOException e1) {
				logger.info("File added but unable to delete. "+file);
			}
		} 
		catch(MediaRejectedException e){
			if(mediaLibraryConfig.isDeleteRejectedFile()){
				try {
					Files.deleteIfExists(file);
					logger.info("File rejected and deleted. "+file);
				} catch (IOException e1) {
					logger.info("File rejected but unable to delete. "+file);
				}
			}else{
				logger.debug("File rejected but not deleted. "+file);
			}
		}
		catch (MediaDuplicateException e) {
			logger.info(e.getMessage());
			if(mediaLibraryConfig.isDeleteDuplicateFile()){
				try {
					Files.deleteIfExists(file);
					logger.info("File duplicate and deleted. "+file);
				} catch (IOException e1) {
					logger.info("File duplicate but unable to delete. "+file);
				}
			}else{
				logger.debug("File duplicate but not deleted. "+file);
			}
		}
		catch (Exception e) {
			logger.error("Errof adding file to media store. " + file, e);
		}
	}
}
