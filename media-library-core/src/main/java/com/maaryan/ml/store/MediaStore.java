package com.maaryan.ml.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maaryan.fhi.FileHashIndexer;
import com.maaryan.fhi.FileHashIndexerRelativePath;
import com.maaryan.fhi.conf.DefaultConfigFactory;
import com.maaryan.fhi.conf.FileHashIndexerConfig;
import com.maaryan.fhi.helper.FileMetaHelper;
import com.maaryan.fhi.io.PathFileFilter;
import com.maaryan.fhi.io.PathUtil;
import com.maaryan.fhi.vo.FileHashIndex;
import com.maaryan.fhi.vo.FileMeta;
import com.maaryan.ml.excp.MediaDuplicateException;
import com.maaryan.ml.excp.MediaLibraryException;
import com.maaryan.ml.excp.MediaRejectedException;
import com.maaryan.ml.util.GsonUtil;
public class MediaStore {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private Path mediaStorePath;
	private Path indexFile;
	private Path rejectedHashFile;
	private PathFileFilter pathFileFilter;
	private FileHashIndexerConfig fileHashIndexerConfig = DefaultConfigFactory.getFileHashIndexerRelativePathConfig();
	private FileHashIndex mediaIndex;
	private Set<String> rejectedHashes = new LinkedHashSet<String>();
	public MediaStore(Path mediaStorePath, MediaExtensionFilter filter){
		this.mediaStorePath = mediaStorePath;
		this.indexFile = mediaStorePath.resolve("mediaIndex.json");
		this.rejectedHashFile = mediaStorePath.resolve("mediaRejected.txt");
		rejectedHashes = MediaStoreHelper.getRejectedHashSet(rejectedHashFile);
		fileHashIndexerConfig.setPathFileFilter(filter);
	}
	public synchronized void initStore()  throws IOException{
		if(!Files.exists(mediaStorePath)){
			Files.createDirectories(mediaStorePath);
		}
		if(!Files.exists(indexFile)){
			logger.warn("Media index file not found "+indexFile);
			createIndex();
		}
		mediaIndex = GsonUtil.fromJson(FileUtils.readFileToString(indexFile.toFile()), FileHashIndex.class);
	}
	public synchronized void createIndex()  throws IOException{
		logger.info("Creating media index: "+mediaStorePath);
		Set<Path> foldersToScan = new HashSet<Path>();
		foldersToScan.add(mediaStorePath);
		fileHashIndexerConfig.setPathFileFilter(pathFileFilter);
		FileHashIndexer fileHashIndexer = new FileHashIndexerRelativePath(mediaStorePath, foldersToScan, fileHashIndexerConfig);
		FileHashIndex fileHashIndex = fileHashIndexer.indexFiles();
		persistIndex(fileHashIndex);
		logger.info("Creating media index done: "+mediaStorePath);
	}
	public synchronized void addFile(Path file) throws IOException{
		logger.info("Adding file "+file);
		if(PathUtil.isChild(mediaStorePath,file)){
			throw new MediaLibraryException("Cannot add child file of media store. "+file);
		}
		FileMeta fileMeta = FileMetaHelper.getFullPathMeta(file, fileHashIndexerConfig.getFileHashAlgorithm());
		if(rejectedHashes.contains(fileMeta.getFileHash())){
			throw new MediaRejectedException(file + " is in rejected list.");
		}
		FileMeta existingFileMeta = mediaIndex.getUniqueFileMeta(fileMeta.getFileHash());
		if(existingFileMeta!=null && Files.exists(mediaStorePath.resolve(existingFileMeta.getFilePath()))){
			throw new MediaDuplicateException(file + " is duplicated by "+fileMeta.getFilePath());
		}
		Path toPath = MediaStoreHelper.getMediaPath(mediaStorePath, file);
		Files.copy(file, toPath);
		FileMeta fileMetaNew = FileMetaHelper.getRelativePathMeta(mediaStorePath, toPath, fileHashIndexerConfig.getFileHashAlgorithm());
		mediaIndex.addUniqueFileMeta(fileMetaNew);
		if(!fileMeta.getFileHash().equals(fileMetaNew.getFileHash())){
			logger.warn("File meta mismatch after copy fromFile:"+fileMeta.getFilePath()+" toFile:"+fileMetaNew.getFilePath());
		}
		persistIndex(mediaIndex);
	}
	private void persistIndex(FileHashIndex fileHashIndex)  throws IOException{
		FileUtils.writeStringToFile(indexFile.toFile(), GsonUtil.toJson(fileHashIndex));
	}
	public void setPathFileFilter(PathFileFilter pathFileFilter) {
		this.pathFileFilter = pathFileFilter;
	}
	public Path getIndexFile() {
		return indexFile;
	}
	public Path getMediaStorePath() {
		return mediaStorePath;
	}
}
