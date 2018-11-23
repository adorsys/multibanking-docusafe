package de.adorsys.multibanking.config.core;

import org.adorsys.docusafe.spring.annotation.UseDocusafeSpringConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Sample config for the docusafe. Beware of the wrapping for exception handling.
 * @author fpo
 *
 */
@Configuration
@UseDocusafeSpringConfiguration
public class DocuSafeConfig {
	private final static Logger LOGGER = LoggerFactory.getLogger(DocuSafeConfig.class);
	public DocuSafeConfig() {
		LOGGER.debug("docusafe spring config supplies ExtendedStoreConnection and CachedTransactionalDocumentSafeService as spring beans.");
	}
}
