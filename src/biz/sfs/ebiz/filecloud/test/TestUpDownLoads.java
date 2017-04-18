package biz.sfs.ebiz.filecloud.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import biz.sfs.ebiz.filecloud.Filecloud;

/** Führt Tests im Bezug auf die Up- und Downloads aus
 * @author anonym
 *
 */
public class TestUpDownLoads {

	private static Filecloud _fileCloudInstance;
	
	private static String _validServerPathUp;
	private static String _validServerPathDown;
	private static String _validLocalPath;
	private static String _invalidLocalPath;
	private static String _invalidServerPath;
	private static String _validUsername;
	private static String _validPassword;
	
	
	@BeforeClass
	public static void setUpStatic(){
		System.out.println("before class call");
		Properties properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream("testparameter.properties"));
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_validUsername = properties.getProperty("validUser");
		_validPassword = properties.getProperty("validPass");
		_invalidServerPath = properties.getProperty("invalidPath");
		_invalidLocalPath = properties.getProperty("invalidPath");
		_validLocalPath = properties.getProperty("validLocalFilePath");
		_validServerPathDown = properties.getProperty("validServerPathDown");
		_validServerPathUp = properties.getProperty("validPath");
		
		_fileCloudInstance = new Filecloud();
		
		_fileCloudInstance.setUrl(properties.getProperty("validURL"));
		
		_fileCloudInstance.login(_validUsername, _validPassword);
	}
	
	@AfterClass
	public static void tearDownStatic(){
		System.out.println("after class call");
	}
	
	@Before
	public void setUp(){
		System.out.println("before test call");
	}
	
	@After
	public void tearDown(){
		System.out.println("after test call");
	}
	
	@Test
	public void testDownloadValidPath(){
		Assert.assertTrue(_fileCloudInstance.downloadFiles(_validServerPathDown, _validLocalPath));
	}
	
	@Test
	public void testDownloadInvalidServerPath(){
		Assert.assertFalse(_fileCloudInstance.downloadFiles(_invalidServerPath, _validLocalPath));
	}
	
	@Test
	public void testDownloadInvalidLocalPath(){
		Assert.assertFalse(_fileCloudInstance.downloadFiles(_validServerPathDown, _invalidLocalPath));
	}
	
	@Test
	public void testDownloadInvalidPath(){
		Assert.assertFalse(_fileCloudInstance.downloadFiles(_invalidServerPath, _invalidLocalPath));
	}
	
	@Test
	public void testUploadValidPath(){
		Assert.assertTrue(_fileCloudInstance.uploadFile(_validServerPathUp, _validLocalPath));
	}
	
	@Test
	public void testUploadInvalidServerPath(){
		Assert.assertFalse(_fileCloudInstance.uploadFile(_invalidServerPath, _validLocalPath));
	}
	
	@Test
	public void testUploadInvalidLocalPath(){
		Assert.assertFalse(_fileCloudInstance.uploadFile(_validServerPathUp, _invalidLocalPath));
	}
	
	@Test
	public void testUploadInvalidPath(){
		Assert.assertFalse(_fileCloudInstance.uploadFile(_invalidServerPath, _invalidLocalPath));
	}
}