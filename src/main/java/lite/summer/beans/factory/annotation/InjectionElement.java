package lite.summer.beans.factory.annotation;

import lite.summer.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;

/**
 * @Author: ReZero
 * @Date: 4/8/19 4:04 PM
 * @Version 1.0
 */
public abstract class InjectionElement {
    protected Member member;
    protected AutowireCapableBeanFactory factory;
    InjectionElement(Member member,AutowireCapableBeanFactory factory){
        this.member = member;
        this.factory = factory;
    }

    public abstract void inject(Object target);

}
