package lite.summer.aop.aspectj;

import lite.summer.aop.config.AspectInstanceFactory;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 3:02 PM
 * @Version 1.0
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //例如： 调用TransactionManager的start方法
        this.invokeAdviceMethod();
        return methodInvocation.proceed();
    }


}