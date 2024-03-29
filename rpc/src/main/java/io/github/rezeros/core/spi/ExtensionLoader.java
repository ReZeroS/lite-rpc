package io.github.rezeros.core.spi;


import io.github.rezeros.core.filter.IClientFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    public static String EXTENSION_LOADER_DIR_PREFIX = "META-INF/irpc/";

    public static Map<String, LinkedHashMap<String, Class<?>>> EXTENSION_LOADER_CLASS_CACHE = new ConcurrentHashMap<>();

    public void loadExtension(Class<?> clazz) throws IOException, ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null!");
        }
        String spiFilePath = EXTENSION_LOADER_DIR_PREFIX + clazz.getName();
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> enumeration;
        enumeration = classLoader.getResources(spiFilePath);
        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            InputStreamReader inputStreamReader;
            inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<>();
            while ((line = bufferedReader.readLine()) != null) {
                //如果配置中加入了#开头则表示忽略该类无需进行加载
                if (line.startsWith("#")) {
                    continue;
                }
                String[] lineArr = line.split("=");
                String implClassName = lineArr[0];
                String interfaceName = lineArr[1];
                classMap.put(implClassName, Class.forName(interfaceName));
            }
            //只会触发class文件的加载，而不会触发对象的实例化
            if (EXTENSION_LOADER_CLASS_CACHE.containsKey(clazz.getName())) {
                //支持开发者自定义配置
                EXTENSION_LOADER_CLASS_CACHE.get(clazz.getName()).putAll(classMap);
            } else {
                EXTENSION_LOADER_CLASS_CACHE.put(clazz.getName(), classMap);
            }
        }
    }


    public <T> T getInstance(String implementName, Class<T> interfaceClass) throws InstantiationException, IllegalAccessException {
        LinkedHashMap<String, Class<?>> cacheMap = EXTENSION_LOADER_CLASS_CACHE.get(interfaceClass.getName());
        return interfaceClass.cast(cacheMap.get(implementName).newInstance());
    }

    public <T> List<T> loadInstanceList(Class<T> interfaceClass) throws InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>();
        LinkedHashMap<String, Class<?>> cacheMap = EXTENSION_LOADER_CLASS_CACHE.get(interfaceClass.getName());
        for (Class<?> readyClass : cacheMap.values()) {
            result.add(interfaceClass.cast(readyClass.newInstance()));
        }
        return result;
    }

}