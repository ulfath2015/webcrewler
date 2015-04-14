package com.imaginea.crawler.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.crawler.service.CrawlerService;

public class CrawlerServiceImpl implements CrawlerService {

	public static final Logger LOG = LoggerFactory
			.getLogger(CrawlerServiceImpl.class);

	String downloadDirectory;

	String urlDomain;

	static Set<String> absMailURLs = new HashSet<String>();
	

	public CrawlerServiceImpl(String downloadDirectory) {

		this.downloadDirectory = downloadDirectory;
	} 

	@Override
	public Set<String> getAllURLs(String inputURL, String searchText)
			throws IOException {

		List<Element> mailLinks = new ArrayList<Element>();

		Document docHTML = Jsoup.connect(inputURL).get();

		Elements anchorElements = getAnchorElements(docHTML, "a");

		if (urlDomain == null && inputURL != null && inputURL.length() > 7) {

			urlDomain = inputURL.substring(0, inputURL.indexOf('/', 7));

		}

		for (Element element : anchorElements) {

			if (!element.attr("abs:href").startsWith(urlDomain))

				anchorElements.remove(element);
		}

		CollectionUtils.select(anchorElements,
				getAnchorFilterPredicate(getMailUrlPattern()), mailLinks);

		anchorElements.removeAll(mailLinks);

		for (Element element : mailLinks) {

			absMailURLs.add(element.attr("abs:href"));

		}
		
		

		return absMailURLs;

	}

	private Predicate getAnchorFilterPredicate(final String hrefPattern) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Pattern REGEX_PATTERN = Pattern.compile(hrefPattern);
				Element anchorElement = (Element) arg0;
				String absUrl = anchorElement.attr("abs:href");
				Matcher matcher = REGEX_PATTERN.matcher(absUrl.toUpperCase());
				if (matcher.find())
					return true;
				return false;
			}
		};
	}

	public String getMailUrlPattern() {
		return "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+";
	}

	private Elements getAnchorElements(Document docHTML, String cssSelector)
			throws IOException {

		return docHTML.select(cssSelector);
	}

	@Override
	public boolean download(Set<String> downloadURLs) {

		LOG.info("Dowloading web content to {}", downloadDirectory);
		try {

			int emailNumber = 1;
			for (String url : downloadURLs) {
				File directory = new File(downloadDirectory);
				if (!directory.exists())
					directory.mkdir();

				File file = new File(directory, "Email #" + (emailNumber++)
						+ ".html");
				saveToFile(url, file);
				if (emailNumber == 10)
					break;
			}
		} catch (Exception e) {

			LOG.error("Exception occurred while processing download : ", e);
			return Boolean.FALSE;

		}

		LOG.info("Download completed");
		return Boolean.TRUE;
	}

	private void saveToFile(String url, File file) throws IOException {

		URL emailUrl;
		URLConnection conn;
		OutputStream outputStream = new FileOutputStream(file);

		try {

			emailUrl = new URL(url);
			conn = emailUrl.openConnection();
			IOUtils.copy(conn.getInputStream(), outputStream);

		} catch (MalformedURLException e) {

			LOG.error("MalformedURLException occurred: {}", e);

		} finally {

			outputStream.close();

		}

	}

}
