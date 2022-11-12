package io.github.rezeros.aop.framework;

/**
 * @Author: ReZero
 * @Date: 4/9/19 5:28 PM
 * @Version 1.0
 */

@SuppressWarnings("serial")
public class AopConfigException extends RuntimeException {

    /**
     * Constructor for AopConfigException.
     * @param msg the detail message
     */
    public AopConfigException(String msg) {
        super(msg);
    }

    /**
     * Constructor for AopConfigException.
     * @param msg the detail message
     * @param cause the root cause
     */
    public AopConfigException(String msg, Throwable cause) {
        super(msg, cause);
    }

}