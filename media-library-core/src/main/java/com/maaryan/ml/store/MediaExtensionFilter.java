package com.maaryan.ml.store;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;

import com.maaryan.fhi.io.PathFileFilter;

public class MediaExtensionFilter implements PathFileFilter {
	private Set<String> extensions= new HashSet<String>();
	public MediaExtensionFilter(String extensions) {
		StringTokenizer st = new StringTokenizer(extensions,",");
		while(st.hasMoreElements()){
			this.extensions.add(st.nextToken().trim().toLowerCase());
		}
	}

	@Override
	public boolean accept(Path path) {
		if(Files.isDirectory(path))
			return false;
		return this.extensions.contains(FilenameUtils.getExtension(path.getFileName().toString().toLowerCase()));
	}
}
