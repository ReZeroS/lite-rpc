package io.github.rezeros.tx;

import io.github.rezeros.util.MessageTracker;

/**
 * @Author: ReZero
 * @Date: 4/8/19 8:26 PM
 * @Version 1.0
 */
public class TransactionManager {

    public void start(){
        System.out.println("start tx");
        MessageTracker.addMsg("start tx");
    }
    public void commit(){
        System.out.println("commit tx");
        MessageTracker.addMsg("commit tx");
    }
    public void rollback(){
        System.out.println("rollback tx");
        MessageTracker.addMsg("rollback tx");
    }

}
