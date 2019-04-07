package lite.summer.beans.factory.config;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeReferencePropertyInfo;

/**
 * @Author: ReZero
 * @Date: 3/19/19 10:32 PM
 * @Version 1.0
 */
public class RuntimeBeanReference {
    private final String beanName;

    public RuntimeBeanReference(String beanName){
        this.beanName = beanName;
    }

    public String getBeanName(){
        return this.beanName;
    }

}