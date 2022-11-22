//package io.github.rezeros.rpc.springboot.starter.config;
//
//import io.github.rezeros.rpc.springboot.starter.common.IRpcService;
//import org.idea.irpc.framework.core.common.event.IRpcListenerLoader;
//import org.idea.irpc.framework.core.server.ApplicationShutdownHook;
//import org.idea.irpc.framework.core.server.Server;
//import org.idea.irpc.framework.core.server.ServiceWrapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import java.util.Map;
//
///**
// * @Author linhao
// * @Date created in 7:29 下午 2022/3/7
// */
//public class IRpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(IRpcServerAutoConfiguration.class);
//
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Server server = null;
//        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(IRpcService.class);
//        if (beanMap.size() == 0) {
//            //说明当前应用内部不需要对外暴露服务
//            return;
//        }
//        printBanner();
//        long begin = System.currentTimeMillis();
//        server = new Server();
//        server.initServerConfig();
//        IRpcListenerLoader iRpcListenerLoader = new IRpcListenerLoader();
//        iRpcListenerLoader.init();
//        for (String beanName : beanMap.keySet()) {
//            Object bean = beanMap.get(beanName);
//            IRpcService iRpcService = bean.getClass().getAnnotation(IRpcService.class);
//            ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(bean, iRpcService.group());
//            dataServiceServiceWrapper.setServiceToken(iRpcService.serviceToken());
//            dataServiceServiceWrapper.setLimit(iRpcService.limit());
//            server.exportService(dataServiceServiceWrapper);
//            LOGGER.info(">>>>>>>>>>>>>>> [irpc] {} export success! >>>>>>>>>>>>>>> ",beanName);
//        }
//        long end = System.currentTimeMillis();
//        ApplicationShutdownHook.registryShutdownHook();
//        server.startApplication();
//        LOGGER.info(" ================== [{}] started success in {}s ================== ",server.getServerConfig().getApplicationName(),((double)end-(double)begin)/1000);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
//    private void printBanner(){
//        System.out.println();
//        System.out.println("==============================================");
//        System.out.println("|||---------- IRpc Starting Now! ----------|||");
//        System.out.println("==============================================");
//        System.out.println("源代码地址: https://gitee.com/IdeaHome_admin/irpc-framework");
//        System.out.println("version: 1.0.0");
//        System.out.println();
//    }
//}
