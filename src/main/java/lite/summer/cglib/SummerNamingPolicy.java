package lite.summer.cglib;

import net.sf.cglib.core.DefaultNamingPolicy;

/**
 * @Author: ReZero
 * @Date: 4/9/19 5:32 PM
 * @Version 1.0
 */
public class SummerNamingPolicy extends DefaultNamingPolicy {
    public static final SummerNamingPolicy INSTANCE = new SummerNamingPolicy();

    public SummerNamingPolicy() {
    }

    @Override
    protected String getTag() {
        return "BySummerCGLIB";
    }

}
