package com.imaginea.crawler.serviceimpl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.imaginea.crawler.service.CrawlerService;

public class CrawlerServiceImplTest {
	
	File testDirectory;
	File file;
	CrawlerService crawlerService;

	@Before
	public void setUp() throws Exception {
		testDirectory = new File(System.getProperty("java.io.tmpdir") + "/" + "webcrawler-test-folder"+System.currentTimeMillis()+"/");
		crawlerService = new CrawlerServiceImpl(testDirectory.getAbsolutePath()+"/");
		
		if (!testDirectory.exists())
			testDirectory.mkdir();
	}

	@After
	public void tearDown() throws Exception {
		testDirectory.deleteOnExit();
	}

	@Test
	public void downloadMailTest() {
		Set<String> mailURLList = new HashSet<String>();
		mailURLList.add("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/%3C547C1A5F.7070709@uni-jena.de%3E");
		
		assertTrue(crawlerService.download(mailURLList));
	}
	
	@Test
	public void getAllURLs() throws IOException{
		
		Set<String> mailURLs = crawlerService.getAllURLs("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread","email");
		
		assertTrue( mailURLs.size() > 0 );
		
	}


}
