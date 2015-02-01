package com.maaryan.ml.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.maaryan.fhi.excp.FileHashIndexerException;
import com.maaryan.fhi.io.PathUtil;
import com.maaryan.fhi.vo.FileHashIndex;
import com.maaryan.ml.MediaLibraryTest;
import com.maaryan.ml.util.GsonUtil;

public class MediaStoreTest extends MediaLibraryTest{
	private static Path testMediaStoreFolder;
	@BeforeClass
	public static void beforeClass() {
		try {
			testMediaStoreFolder = Files.createTempDirectory("media-folder");
			Files.createDirectories(testMediaStoreFolder);
			Path a = testMediaStoreFolder.resolve("a");
			Files.createDirectory(a);
			Path b = testMediaStoreFolder.resolve("b");
			Files.createDirectory(b);
			Files.createFile(a.resolve("f1.avi"));
			Files.createFile(b.resolve("f2.jpg"));
			FileUtils.writeStringToFile(b.resolve("f2.jpg").toFile(),"rn");
		} catch (IOException e) {
			throw new FileHashIndexerException(e);
		}
	}

	@Test
	public void testInit() {
		logger.info("testInit started");
		MediaStore mediaStore = new MediaStore(testMediaStoreFolder,null);
		try {
			mediaStore.createIndex();
			FileHashIndex fhi = GsonUtil.fromJson(FileUtils.readFileToString(mediaStore.getIndexFile().toFile()), FileHashIndex.class);
			Assert.assertTrue(fhi.getFileIndexMap().size()==2);
			System.out.println(FileUtils.readFileToString(mediaStore.getIndexFile().toFile()));
		} catch (IOException e) {
			Assert.fail("Exception not expted.");
		}
		logger.info("testInit ended");
	}
	
	@AfterClass
	public static void afterClass() {
		PathUtil.deleteRecursively(testMediaStoreFolder);
	}

}
