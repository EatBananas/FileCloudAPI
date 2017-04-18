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

/** Führt Tests im Bezug auf Fileoperationen wie File erstellen und löschen aus
 * @author anonym
 *
 */
public class TestFileOperations {

	private static Filecloud _fileCloudInstance;
	
	private static String _validPath;
	private static String _invalidPath;
	private static String _subFolderName;
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
		_subFolderName = properties.getProperty("validSubfolderName");
		_invalidPath = properties.getProperty("invalidPath");
		_validPath = properties.getProperty("validPath");
		
		_fileCloudInstance = new Filecloud();
		
		_fileCloudInstance.setUrl(properties.getProperty("validURL"));
		
		_fileCloudInstance.login(_validUsername, _validPassword);
		
		_fileCloudInstance.createFolder(_validPath, _subFolderName);
	}
	
	@AfterClass
	public static void tearDownStatic(){
		System.out.println("after class call");
		_fileCloudInstance.deleteFolder(_validPath, _subFolderName);
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
	public void testCreateValidPath(){
		Assert.assertTrue(_fileCloudInstance.createFolder(_validPath, _subFolderName));
	}
	
	@Test
	public void testCreateInvalidPath(){
		Assert.assertFalse(_fileCloudInstance.createFolder(_invalidPath, _subFolderName));
	}
	
	@Test
	public void testDeleteValidPath(){
		Assert.assertTrue(_fileCloudInstance.deleteFolder(_validPath, _subFolderName));
	}
	
	@Test
	public void testDeleteInvalidPath(){
		Assert.assertFalse(_fileCloudInstance.deleteFolder(_invalidPath, _subFolderName));
	}
}

