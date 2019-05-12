package lite.summer.beans.factory.annotation;

import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.config.AutowireCapableBeanFactory;
import lite.summer.beans.factory.config.DependencyDescriptor;
import lite.summer.util.ReflectionUtils;

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

    public Method getMethod(){
        return (Method) this.member;
    }


    @Override
    public void inject(Object target) {
//        Method method = this.getMethod();
//        try {
//
//            DependencyDescriptor desc = new DependencyDescriptor(method, this.required);
//
//            Object value = factory.resolveDependency(desc);
//
//            if (value != null) {
//
//                ReflectionUtils.makeAccessible(field);
//                field.set(target, value);
//            }
//        }
//        catch (Throwable ex) {
//            throw new BeanCreationException("Could not autowire field: " + field, ex);
//        }
    }
}
