package com.imaginea.crawler.service;

import java.io.IOException;
import java.util.Set;

public interface CrawlerService {

	Set<String> getAllURLs(String inputURL, String searchText)
			throws IOException;

	boolean download(Set<String> downloadURLs);

}
