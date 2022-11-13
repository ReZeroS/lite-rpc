package io.github.rezeros.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalCache {


    public static final Map<String, Object> PROVIDER_CACHE = new ConcurrentHashMap<>();


}
