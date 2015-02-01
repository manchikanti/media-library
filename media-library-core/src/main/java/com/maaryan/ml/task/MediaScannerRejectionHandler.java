package com.maaryan.ml.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaScannerRejectionHandler implements RejectedExecutionHandler{
	private Logger logger = LoggerFactory.getLogger(MediaScannerRejectionHandler.class);
        @Override
        public void rejectedExecution(Runnable r,
                ThreadPoolExecutor executor) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage(),e);
            }
            executor.execute(r);
        }
}
