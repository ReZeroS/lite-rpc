//package io.github.rezeros.core.serialize.jdk;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JdkSerializeFactoryTest {
//
//
//    private static User buildUserDefault() {
//        User user = new User();
//        user.setAge(11);
//        user.setAddress("深圳市南山区");
//        user.setBankNo(12897873624813L);
//        user.setSex(1);
//        user.setId(10001);
//        user.setIdCardNo("440308781129381222");
//        user.setRemark("备注信息字段");
//        user.setUsername("ddd-user-name");
//        return user;
//    }
//
//    @Benchmark
//    public void jdkSerializeTest() {
//        SerializeFactory serializeFactory = new JdkSerializeFactory();
//        User user = buildUserDefault();
//        byte[] result = serializeFactory.serialize(user);
//        User deserializeUser = serializeFactory.deserialize(result, User.class);
//    }
//
//    @Benchmark
//    public void hessianSerializeTest() {
//        SerializeFactory serializeFactory = new HessianSerializeFactory();
//        User user = buildUserDefault();
//        byte[] result = serializeFactory.serialize(user);
//        User deserializeUser = serializeFactory.deserialize(result, User.class);
//    }
//
//    @Benchmark
//    public void fastJsonSerializeTest() {
//        SerializeFactory serializeFactory = new FastJsonSerializeFactory();
//        User user = buildUserDefault();
//        byte[] result = serializeFactory.serialize(user);
//        User deserializeUser = serializeFactory.deserialize(result, User.class);
//    }
//
//    @Benchmark
//    public void kryoSerializeTest() {
//        SerializeFactory serializeFactory = new KryoSerializeFactory();
//        User user = buildUserDefault();
//        byte[] result = serializeFactory.serialize(user);
//        User deserializeUser = serializeFactory.deserialize(result, User.class);
//    }
//
//    public static void main(String[] args) throws RunnerException {
//        //配置进行2轮热数 测试2轮 1个线程
//        //预热的原因 是JVM在代码执行多次会有优化
//        Options options = new OptionsBuilder().warmupIterations(2).measurementBatchSize(2)
//                .forks(1).build();
//        new Runner(options).run();
//    }
//
//
//}