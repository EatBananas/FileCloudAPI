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

/** Führt mögliche Tests zum Login aus
 * @author anonym
 *
 */
public class TestAll {

	private static Filecloud _fileCloudInstance;
	
	private static String _validPassword;
	private static String _validUsername;
	private static String _invalidPassword;
	private static String _invalidUsername;
	
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
		_invalidUsername = properties.getProperty("invalidUser");
		_invalidPassword = properties.getProperty("invalidPass");
		
		_fileCloudInstance = new Filecloud();
		
		_fileCloudInstance.setUrl(properties.getProperty("validURL"));
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
	public void testLoginValidCredentials(){
		Assert.assertTrue(_fileCloudInstance.login(_validUsername, _validPassword));
	}
	
	@Test
	public void testLoginInvalidUsername(){
		Assert.assertFalse(_fileCloudInstance.login(_invalidUsername, _validPassword));
	}
	
	@Test
	public void testLoginInvalidPassword(){
		Assert.assertFalse(_fileCloudInstance.login(_validUsername, _invalidPassword));
	}
	
	@Test
	public void testLoginInvalidUsernameAndPassword(){
		Assert.assertFalse(_fileCloudInstance.login(_invalidUsername, _invalidPassword));
	}
}
