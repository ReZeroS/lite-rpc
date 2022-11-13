package io.github.rezeros.cache;

import io.github.rezeros.core.registry.URL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommonServerCache {

    public static final Map<String,Object> PROVIDER_CACHE = new HashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();
}