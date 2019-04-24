package lite.summer.aop.aspectj;

import lite.summer.aop.config.AspectInstanceFactory;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 3:02 PM
 * @Version 1.0
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice{

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod,pointcut,adviceObjectFactory);
    }

    public Object invoke(MethodInvocation mi) throws Throwable {
        Object o = mi.proceed();
        //例如：调用TransactionManager的commit方法
        this.invokeAdviceMethod();
        return o;
    }

}
