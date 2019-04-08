package lite.summer.aop;

/**
 * @Author: ReZero
 * @Date: 4/8/19 10:15 PM
 * @Version 1.0
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();
    String getExpression();
}
