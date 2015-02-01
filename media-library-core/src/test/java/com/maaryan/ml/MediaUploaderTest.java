package com.maaryan.ml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.maaryan.ml.conf.MediaUploader;

public class MediaUploaderTest extends MediaLibraryTest{
	@Autowired
	private MediaUploader mediaUploader;
	@Test
	public void test(){
		System.out.println(mediaUploader.getMediaLibraryConfig().getImageStore().getIndexFile());
		System.out.println(mediaUploader.getMediaLibraryConfig().getAudioStore().getIndexFile());
		System.out.println(mediaUploader.getMediaLibraryConfig().getVideoStore().getIndexFile());
	}
	@Test
	public void uploadMedia(){
		Set<Path> foldersToScan = new TreeSet<>();
		foldersToScan.add(Paths.get("/photos"));
		mediaUploader.setFoldersToScan(foldersToScan);
		mediaUploader.uploadMedia();
	}
}
