package lite.summer.beans.factory.annotation;

import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.config.AutowireCapableBeanFactory;
import lite.summer.beans.factory.config.DependencyDescriptor;
import lite.summer.core.MethodParameter;
import lite.summer.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 5/12/19 9:27 AM
 * @Version 1.0
 */
public class AutowiredMethodElement extends InjectionElement {
    boolean required;

    public AutowiredMethodElement(Method method, boolean required, AutowireCapableBeanFactory beanFactory) {
        super(method, beanFactory);
        this.required = required;
    }

    public Method getMethod() {
        return (Method) this.member;
    }


    @Override
    public void inject(Object target) {
        Method method = (Method) this.member;

        try {
            Object[] arguments;
            Class<?>[] paramTypes = method.getParameterTypes();
            arguments = new Object[paramTypes.length];
            DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];

            for (int i = 0; i < arguments.length; i++) {
                MethodParameter methodParam = new MethodParameter(method, i);
                descriptors[i] = new DependencyDescriptor(required, methodParam);
                Object value = factory.resolveDependency(descriptors[i]);
                arguments[i] = value;
                if (value != null) {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(target, arguments);
                }
            }
        } catch (InvocationTargetException ex) {


        } catch (Throwable ex) {
				throw new BeanCreationException("Could not autowire method: " + method, ex);
        }
    }
}
