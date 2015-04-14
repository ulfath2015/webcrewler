package com.imaginea.crawler.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.crawler.service.CrawlerService;
import com.imaginea.crawler.serviceimpl.CrawlerServiceImpl;

public class Crawler {

	public static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

	public static void main(String args[]) {
		
		String fileName="crawler.properties";

		try {

			CrawlerConfig crawlerConfig = new CrawlerConfig(fileName);

			CrawlerService crawlerService = new CrawlerServiceImpl(
					crawlerConfig.getDownloadLocation());

			Set<String> mailURLs = crawlerService.getAllURLs(
					crawlerConfig.getSourceURL(), "");

			crawlerService.download(mailURLs);

		} catch (FileNotFoundException e) {

			LOG.error("Property file " + fileName + " not found, exception : "
					+ e);

		} catch (IOException e) {

			LOG.error("IO exception occurred during " + fileName
					+ " exception : " + e);

		}
	}

}
