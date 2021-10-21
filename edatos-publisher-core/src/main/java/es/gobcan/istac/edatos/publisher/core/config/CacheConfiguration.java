package es.gobcan.istac.edatos.publisher.core.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * @see <a href="https://stackoverflow.com/a/59735749/7611990">StackOverflow - Spring cache for a given request</a>
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean("requestScopedCacheManager")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
