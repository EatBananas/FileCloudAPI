package biz.sfs.ebiz.filecloud.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** Hauptklasse der Tests, zum ausführen aller Tests
 * @author anonym
 *
 */
@RunWith(Suite.class)
@SuiteClasses(value = { TestAll.class, TestFileOperations.class, TestUpDownLoads.class, TestShare.class })
public class TestSuite {

}
