package com.maaryan.ml.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.maaryan.ml.store.MediaExtensionFilter;
import com.maaryan.ml.store.MediaStore;

@Component
public class MediaLibraryConfig {
	@Value("${mediaLibraryPath:/media-library}")
	private String mediaLibraryPath;
	@Value("${deleteRejectedFile:false}")
	private boolean deleteRejectedFile=false;
	@Value("${deleteDuplicateFile:false}")
	private boolean deleteDuplicateFile=false;
	@Value("${mediaFileCorePoolSize:100}")
	private int mediaFileCorePoolSize = 100;
	@Value("${mediaFileMaxPoolSize:500}")
	private int mediaFileMaxPoolSize = 500;
	@Value("${mediaFileQueueSize:10000}")
	private int mediaFileQueueSize = 10000;
	@Value("${mediaFileKeepAliveTimeMsecs:60000}")
	private long mediaFileKeepAliveTimeMsecs = 60 * 1000;
	@Value("${mediaScannerCorePoolSize:10}")
	private int mediaScannerCorePoolSize = 10;
	@Value("${mediaScannerMaxPoolSize:20}")
	private int mediaScannerMaxPoolSize = 20;
	@Value("${mediaScannerQueueSize:20}")
	private int mediaScannerQueueSize = 20;
	@Value("${mediaScannerKeepAliveTimeMsecs:600000}")
	private long mediaScannerKeepAliveTimeMsecs = 10 * 60 * 1000;
	@Autowired
	private MediaExtensionFilter imageFilter;
	@Autowired
	private MediaExtensionFilter audioFilter;
	@Autowired
	private MediaExtensionFilter videoFilter;
	
	@Autowired
	private MediaStore imageStore;
	@Autowired
	private MediaStore audioStore;
	@Autowired
	private MediaStore videoStore;
	
	public String getMediaLibraryPath() {
		return mediaLibraryPath;
	}
	public boolean isDeleteRejectedFile() {
		return deleteRejectedFile;
	}
	public boolean isDeleteDuplicateFile() {
		return deleteDuplicateFile;
	}
	public int getMediaFileCorePoolSize() {
		return mediaFileCorePoolSize;
	}
	public int getMediaFileMaxPoolSize() {
		return mediaFileMaxPoolSize;
	}
	public int getMediaFileQueueSize() {
		return mediaFileQueueSize;
	}
	public long getMediaFileKeepAliveTimeMsecs() {
		return mediaFileKeepAliveTimeMsecs;
	}
	public int getMediaScannerCorePoolSize() {
		return mediaScannerCorePoolSize;
	}
	public int getMediaScannerMaxPoolSize() {
		return mediaScannerMaxPoolSize;
	}
	public int getMediaScannerQueueSize() {
		return mediaScannerQueueSize;
	}
	public long getMediaScannerKeepAliveTimeMsecs() {
		return mediaScannerKeepAliveTimeMsecs;
	}
	public MediaExtensionFilter getImageFilter() {
		return imageFilter;
	}
	public MediaExtensionFilter getAudioFilter() {
		return audioFilter;
	}
	public MediaExtensionFilter getVideoFilter() {
		return videoFilter;
	}
	public MediaStore getImageStore() {
		return imageStore;
	}
	public MediaStore getAudioStore() {
		return audioStore;
	}
	public MediaStore getVideoStore() {
		return videoStore;
	}
}
