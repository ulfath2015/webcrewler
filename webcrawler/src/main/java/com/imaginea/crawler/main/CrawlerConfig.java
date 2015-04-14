package com.imaginea.crawler.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CrawlerConfig {

	private String sourceURL;
	private String downloadLocation;
	private Properties props;

	public CrawlerConfig(String propertyFile) throws FileNotFoundException,
			IOException {

		props = new Properties();

		try (InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propertyFile)) {

			if (inputStream != null) {
				props.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propertyFile + "' not found in the classpath");
			}
		}

		sourceURL = props.getProperty("URL");
		downloadLocation = props.getProperty("DOWNLOAD_DIR");
	}

	public String getDownloadLocation() {
		return downloadLocation;
	}

	public String getSourceURL() {
		return sourceURL;
	}

}
