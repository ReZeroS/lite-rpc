package lite.summer.aop.aspectj;

import lite.summer.aop.MethodMatcher;
import lite.summer.aop.Pointcut;
import lite.summer.util.ClassUtils;
import lite.summer.util.StringUtils;
import org.aspectj.weaver.reflect.ReflectionWorld;
import org.aspectj.weaver.tools.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ReZero
 * @Date: 4/8/19 10:11 PM
 * @Version 1.0
 */
public class AspectJExpressionPointcut implements Pointcut, MethodMatcher {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    /**
     * Enumeration list of the different kinds of pointcut primitives
     * supported by AspectJ.
     */
    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    private String expression;

    private PointcutExpression pointcutExpression;

    private ClassLoader pointcutClassLoader;

    public AspectJExpressionPointcut() {

    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    @Override
    public boolean matches(Method method/*, Class<?> targetClass*/) {

        checkReadyToMatch();

        ShadowMatch shadowMatch = getShadowMatch(method);

//      Special handling for this, target, @this, @target, @annotation
//      in Summer - we can optimize since we know we have exactly this class,
//      and there will never be matching subclass at runtime.
        if (shadowMatch.alwaysMatches()) {
            return true;
        }

        return false;
    }


    private ShadowMatch getShadowMatch(Method method) {

        ShadowMatch shadowMatch;
        try {
            shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        } catch (ReflectionWorld.ReflectionWorldException ex) {

            throw new RuntimeException("not implemented yet");
			/*try {
				fallbackExpression = getFallbackPointcutExpression(methodToMatch.getDeclaringClass());
				if (fallbackExpression != null) {
					shadowMatch = fallbackExpression.matchesMethodExecution(methodToMatch);
				}
			}
			catch (ReflectionWorldException ex2) {
				fallbackExpression = null;
			}*/
        }
        return shadowMatch;
    }

    /**
     * Check the expression and classloader has been created
     * or throw IllegalStateException
     */
    private void checkReadyToMatch() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (this.pointcutExpression == null) {
            this.pointcutClassLoader = ClassUtils.getDefaultClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
        }
    }

    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {

        PointcutParser parser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, classLoader);

        // resolve pointcut parameters
		/*PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
		for (int i = 0; i < pointcutParameters.length; i++) {
			pointcutParameters[i] = parser.createPointcutParameter(
					this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
		}*/
        return parser.parsePointcutExpression(replaceBooleanOperators(getExpression()),
                null, new PointcutParameter[0]);
    }


    private String replaceBooleanOperators(String pcExpr) {
        String result = StringUtils.replace(pcExpr, " and ", " && ");
        result = StringUtils.replace(result, " or ", " || ");
        result = StringUtils.replace(result, " not ", " ! ");
        return result;
    }


}
