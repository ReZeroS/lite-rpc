package lite.summer.aop.aspectj;

import lite.summer.aop.Advice;
import lite.summer.aop.Pointcut;
import lite.summer.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 3:53 PM
 * @Version 1.0
 */
public abstract class AbstractAspectJAdvice implements Advice {


    
    protected Method adviceMethod;
    protected AspectJExpressionPointcut pointcut;
    protected AspectInstanceFactory adviceObjectFactory;


    public AbstractAspectJAdvice(Method adviceMethod,
                                 AspectJExpressionPointcut pointcut,
                                 AspectInstanceFactory adviceObjectFactory) {

        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObjectFactory = adviceObjectFactory;
    }


    public void invokeAdviceMethod() throws Throwable {

        adviceMethod.invoke(adviceObjectFactory.getAspectInstance());
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Object getAdviceInstance() throws Exception {
        return adviceObjectFactory.getAspectInstance();
    }
}
