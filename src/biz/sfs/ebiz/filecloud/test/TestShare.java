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

/** Führt tests im Bezug auf Shares aus
 * @author anonym
 *
 */
public class TestShare {

	private static Filecloud _fileCloudInstance;

	private static String _validPassword;
	private static String _validUsername;
	private static String _validSharePath;
	private static String _validShareName;
	private static String _validShareDelName;
	private static String _invalidSharePath;
	private static String _validEMail;
	private static String _validRemEMail;
	private static String _invalidEMail;
	
	
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
		_validEMail = properties.getProperty("validEMail");
		_validRemEMail = properties.getProperty("validRemEMail");
		_invalidEMail = properties.getProperty("invalidEMail");
		_validSharePath = properties.getProperty("validPath");
		_invalidSharePath = properties.getProperty("invalidPath");
		_validShareName = properties.getProperty("validShareName");
		_validShareDelName = properties.getProperty("validShareDelName");
		
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
		_fileCloudInstance.createFolder(_validSharePath, _validShareName);
		_fileCloudInstance.createFolder(_validSharePath, _validShareDelName);
		_fileCloudInstance.setQShare(_validSharePath + "/" + _validShareDelName);
		_fileCloudInstance.addUsertoShare(_validRemEMail, _validSharePath + "/" + _validShareDelName);
	}
	
	@After
	public void tearDown(){
		System.out.println("after test call");
		_fileCloudInstance.deleteFolder(_validSharePath, _validShareDelName);
		_fileCloudInstance.deleteFolder(_validSharePath, _validShareName);
	}
	
	@Test
	public void testCreateShareValidPath(){
		Assert.assertTrue(_fileCloudInstance.setQShare(_validSharePath + "/" + _validShareName));
	}
	
	@Test
	public void testCreateShareInvalidPath(){
		Assert.assertFalse(_fileCloudInstance.setQShare(_invalidSharePath + "/" + _validShareName));
	}
	
	@Test
	public void testDelShareValidPath(){
		Assert.assertTrue(_fileCloudInstance.delShare(_validSharePath + "/" + _validShareDelName));
	}
	
	@Test
	public void testDelShareInvalidPath(){
		Assert.assertFalse(_fileCloudInstance.delShare(_invalidSharePath));
	}
	
	@Test
	public void testAddValidUserToValidShare(){
		Assert.assertTrue(_fileCloudInstance.addUsertoShare(_validEMail, _validSharePath + "/" + _validShareDelName));
	}
	
	@Test
	public void testAddValidUserToInvalidShare(){
		Assert.assertFalse(_fileCloudInstance.addUsertoShare(_validEMail, _invalidSharePath));
	}
	
	@Test
	public void testAddInvalidUserToValidShare(){
		Assert.assertFalse(_fileCloudInstance.addUsertoShare(_invalidEMail, _validSharePath + "/" + _validShareDelName));
	}
	
	@Test
	public void testAddInvalidUserToInvalidShare(){
		Assert.assertFalse(_fileCloudInstance.addUsertoShare(_invalidEMail, _invalidSharePath));
	}
	
	@Test
	public void testDelValidUserFromValidShare(){
		Assert.assertTrue(_fileCloudInstance.deleteUserFromShare(_validRemEMail, _validSharePath + "/" + _validShareDelName));
	}
	
	@Test
	public void testDelValidUserFromInvalidShare(){
		Assert.assertFalse(_fileCloudInstance.addUsertoShare(_validRemEMail, _invalidSharePath));
	}
	
	@Test
	public void testDelInvalidUserFromValidShare(){
		Assert.assertFalse(_fileCloudInstance.addUsertoShare(_invalidEMail, _validSharePath + "/" + _validShareDelName));
	}
	
	@Test
	public void testDelInvalidUserFromInvalidShare(){
		Assert.assertFalse(_fileCloudInstance.addUsertoShare(_invalidEMail, _invalidSharePath));
	}
}