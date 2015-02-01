package com.maaryan.ml.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maaryan.ml.util.MyDateUtil;

public class MediaStoreHelper {
	private static Logger logger = LoggerFactory.getLogger(MediaStoreHelper.class);
	public static Path getMediaPath(Path baseFolder, Path fileToCopy)
			throws IOException {
		Calendar modifiedTime = Calendar.getInstance();
		modifiedTime.setTimeInMillis(Files.getLastModifiedTime(fileToCopy)
				.toMillis());
		String pattern = "yyyyMMdd_HHmmss";
		String extension = FilenameUtils.getExtension(fileToCopy.getFileName()
				.toString()).toLowerCase();
		String fileName = MyDateUtil.toString(modifiedTime, pattern);
		Path toFolder = baseFolder.resolve(MyDateUtil.getYearStr(modifiedTime))
				.resolve(MyDateUtil.getMonthStr(modifiedTime));
		if (!Files.exists(toFolder))
			Files.createDirectories(toFolder);
		Path toFile = toFolder.resolve(fileName + "." + extension);
		if (!Files.exists(toFile))
			return toFile;
		int count = 1;
		do {
			toFile = toFolder.resolve(fileName + "_" + count + "." + extension);
			count++;
		} while (Files.exists(toFile));
		return toFile;
	}
	public static Set<String> getRejectedHashSet(Path rejectedMedia){
		Set<String> rejectedHashes = new LinkedHashSet<String>();
		try {
			if(!Files.exists(rejectedMedia))
				return rejectedHashes;
			for(Object hash:FileUtils.readLines(rejectedMedia.toFile())){
				String h = hash.toString().trim();
				if(h.length()>0)
					rejectedHashes.add(h);
			}
			return rejectedHashes;
		} catch (IOException e) {
			logger.error("Unable to load rejected hashes");
			return rejectedHashes;
		}
	}
} 
