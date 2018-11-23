package de.adorsys.multibanking.service.base;

import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.adorsys.docusafe.spring.factory.SpringCachedTransactionalDocusafeServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by peter on 21.11.18 15:13.
 */
@Configuration
public class ExceptionHandlingCachedTransactionalDocumentSafeServiceBean {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingCachedTransactionalDocumentSafeServiceBean.class);
    @Bean
    public CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService(SpringCachedTransactionalDocusafeServiceFactory factory) {
        LOGGER.info("CACHED SERVICE WRAPPED FOR EXCEPTIONS");
        return new ExceptionHandlingCachedTransactionalDocumentSafeService(factory.getCachedTransactionalDocumentSafeServiceWithSubdir(null));
    }
}
