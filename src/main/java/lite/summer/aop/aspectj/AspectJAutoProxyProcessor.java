package lite.summer.aop.aspectj;

import lite.summer.aop.Advice;
import lite.summer.aop.MethodMatcher;
import lite.summer.aop.Pointcut;
import lite.summer.aop.framework.AopConfigSupport;
import lite.summer.aop.framework.AopProxyFactory;
import lite.summer.aop.framework.CglibProxyFactory;
import lite.summer.aop.framework.JdkAopProxyFactory;
import lite.summer.beans.BeansException;
import lite.summer.beans.factory.config.BeanPostProcessor;
import lite.summer.beans.factory.config.ConfigurableBeanFactory;
import lite.summer.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: ReZero
 * @Date: 4/15/19 6:26 PM
 * @Version 1.0
 */
public class AspectJAutoProxyProcessor implements BeanPostProcessor {
    ConfigurableBeanFactory beanFactory;

    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException {

        //if this Bean is instance or subclass of Advice, then will not create proxy
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        List<Advice> advices = getCandidateAdvices(bean);
        if (advices.isEmpty()) {
            return bean;
        }

        return createProxy(advices, bean);
    }

    private List<Advice> getCandidateAdvices(Object bean) {

        List<Object> advices = this.beanFactory.getBeansByType(Advice.class);

        List<Advice> result = new ArrayList<>();
        for (Object o : advices) {
            Pointcut pointcut = ((Advice) o).getPointcut();
            if (canApply(pointcut, bean.getClass())) {
                result.add((Advice) o);
            }
        }
        return result;
    }

    protected Object createProxy(List<Advice> advices, Object bean) {

        AopConfigSupport config = new AopConfigSupport();

        config.setTargetObject(bean);

        for (Advice advice : advices) {
            config.addAdvice(advice);
        }

        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(bean.getClass());
        for (Class<?> targetInterface : targetInterfaces) {
            config.addInterface(targetInterface);
        }

        AopProxyFactory proxyFactory;
        if (config.getProxiedInterfaces().length == 0) {
            proxyFactory = new CglibProxyFactory(config);
        } else {
            proxyFactory = new JdkAopProxyFactory(config);
        }

        return proxyFactory.getProxy();
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass);
        return retVal;
    }

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;

    }

    public static boolean canApply(Pointcut pointcut, Class<?> targetClass) {

        MethodMatcher methodMatcher = pointcut.getMethodMatcher();

        Set<Class> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
        classes.add(targetClass);
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (methodMatcher.matches(method/*, targetClass*/)) {
                    return true;
                }
            }
        }

        return false;
    }

}
