package com.maaryan.ml.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.maaryan.fhi.excp.FileHashIndexerException;
import com.maaryan.fhi.io.PathUtil;
import com.maaryan.ml.MediaLibraryTest;

public class MediaStoreHelperTest extends MediaLibraryTest {
	private static Path testMediaStoreFolder;
	private static Path testFile;
	@BeforeClass
	public static void beforeClass() {
		try {
			testMediaStoreFolder = Files.createTempDirectory("media-folder");
			Files.createDirectories(testMediaStoreFolder);
			Path a = testMediaStoreFolder.resolve("a");
			Files.createDirectory(a);
			testFile = Files.createFile(a.resolve("f1.jpg"));
		} catch (IOException e) {
			throw new FileHashIndexerException(e);
		}
	}
	@Test
	public final void testGetMediaPath() {
		try {
			Path p = MediaStoreHelper.getMediaPath(testMediaStoreFolder, testFile);
			logger.info(p.toString());
			Files.createFile(p);
			p = MediaStoreHelper.getMediaPath(testMediaStoreFolder, testFile);
			logger.info(p.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			Assert.fail("Exception not expected");
		}
	}
	@AfterClass
	public static void afterClass() {
		PathUtil.deleteRecursively(testMediaStoreFolder);
	}

}
