package lite.summer.test;

import lite.summer.test.v1.V1AllTests;
import lite.summer.test.v2.V2AllTests;
import lite.summer.test.v3.V3AllTests;
import lite.summer.test.v4.V4AllTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @Author: ReZero
 * @Date: 3/31/19 10:37 PM
 * @Version 1.0
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        V1AllTests.class,
        V2AllTests.class,
        V3AllTests.class,
        V4AllTests.class
})
public class AllTests {
}
