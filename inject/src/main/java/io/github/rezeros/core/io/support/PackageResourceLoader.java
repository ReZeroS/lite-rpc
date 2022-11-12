package io.github.rezeros.core.io.support;

import io.github.rezeros.core.io.FileSystemResource;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.util.Assert;
import io.github.rezeros.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author: ReZero
 * @Date: 4/2/19 10:24 PM
 * @Version 1.0
 */

public class PackageResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(PackageResourceLoader.class);

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader, "ResourceLoader must not be null");
        this.classLoader = classLoader;
    }


    public ClassLoader getClassLoader() {
        return this.classLoader;
    }


    /**
     * @param basePackage
     * @return All class resources from the basePackage
     * @throws IOException
     */
    public Resource[] getResources(String basePackage) throws IOException {
        Assert.notNull(basePackage, "basePackage  must not be null");
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);
        ClassLoader cl = getClassLoader();
        URL url = cl.getResource(location);
        File rootDirectory = new File(url.getFile());

        Set<File> matchingFiles = retrieveMatchingFiles(rootDirectory);
        Resource[] result = new Resource[matchingFiles.size()];
        int i = 0;
        for (File file : matchingFiles) {
            result[i++] = new FileSystemResource(file);
        }
        return result;

    }

    protected Set<File> retrieveMatchingFiles(File rootDirectory) throws IOException {
        if (!rootDirectory.exists()) {
            // Silently skip non-existing directories.
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping [" + rootDirectory.getAbsolutePath() + "] because it does not exist");
            }
            return Collections.emptySet();
        }
        if (!rootDirectory.isDirectory()) {
            // Complain louder if it exists but is no directory.
            if (logger.isWarnEnabled()) {
                logger.warn("Skipping [" + rootDirectory.getAbsolutePath() + "] because it does not denote a directory");
            }
            return Collections.emptySet();
        }
        if (!rootDirectory.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot search for matching files underneath directory [" + rootDirectory.getAbsolutePath() +
                        "] because the application is not allowed to read the directory");
            }
            return Collections.emptySet();
        }
		/*String fullPattern = StringUtils.replace(rootDirectory.getAbsolutePath(), File.separator, "/");
		if (!pattern.startsWith("/")) {
			fullPattern += "/";
		}
		fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
		*/
        Set<File> result = new LinkedHashSet<>(8);
        doRetrieveMatchingFiles(rootDirectory, result);
        return result;
    }


    protected void doRetrieveMatchingFiles(File dir, Set<File> result) {

        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
            }
            return;
        }
        for (File content : dirContents) {

            if (content.isDirectory()) {
                if (!content.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() +
                                "] because the application is not allowed to read the directory");
                    }
                } else {
                    doRetrieveMatchingFiles(content, result);
                }
            } else {
                result.add(content);
            }

        }
    }

}
