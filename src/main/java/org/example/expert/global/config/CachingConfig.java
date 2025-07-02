//package org.example.expert.global.config;
//
//import org.ehcache.config.builders.CacheConfigurationBuilder;
//import org.ehcache.config.builders.ExpiryPolicyBuilder;
//import org.ehcache.config.builders.ResourcePoolsBuilder;
//import org.ehcache.config.units.EntryUnit;
//import org.ehcache.config.units.MemoryUnit;
//import org.ehcache.jsr107.Eh107Configuration;
//import org.hibernate.cache.jcache.ConfigSettings;
//import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.jcache.JCacheCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.cache.Caching;
//import javax.cache.spi.CachingProvider;
//import java.time.Duration;
//
//@EnableCaching
//@Configuration
//public class CachingConfig {
//    public static final String DB_CACHE = "db_cache";
//
//    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;
//
//    public CachingConfig() {
//        this.jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
//                        ResourcePoolsBuilder.newResourcePoolsBuilder()
//                                .heap(10000, EntryUnit.ENTRIES))
//                .withSizeOfMaxObjectSize(1000, MemoryUnit.B)
//                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(300)))
//                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(600))));
//    }
//
//    @Bean
//    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(CacheManager cacheManager) {
//        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
//    }
//
//    @Bean
//    public JCacheManagerCustomizer cacheManagerCustomizer() {
//        return cm -> {
//            cm.createCache(DB_CACHE, jcacheConfiguration);
//        };
//    }
//
//    @Bean
//    public CacheManager cacheManager() {
//        CachingProvider provider = Caching.getCachingProvider();
//        javax.cache.CacheManager jCacheManager = provider.getCacheManager();
//        return new JCacheCacheManager(jCacheManager); // spring CacheManager로 래핑
//    }
//}