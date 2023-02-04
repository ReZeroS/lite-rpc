package io.github.rezeros.test;

import io.github.rezeros.core.common.annotations.IRpcService;

import java.util.List;

@IRpcService
public interface DataService {

    /**
     * 发送数据
     *
     * @param body
     */
    String sendData(String body);

    /**
     * 获取数据
     *
     * @return
     */
    List<String> getList();


    /**
     * 异常测试方法
     */
    void testError();

    /**
     * 异常测试方法
     */
    String testErrorV2();
}
