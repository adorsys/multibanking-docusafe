package de.adorsys.multibanking.config.core;

import de.adorsys.multibanking.service.base.ExceptionHandlingDocumentSafeService;
import org.adorsys.cryptoutils.storeconnectionfactory.ExtendedStoreConnectionFactory;
import org.adorsys.docusafe.business.DocumentSafeService;
import org.adorsys.docusafe.business.impl.DocumentSafeServiceImpl;
import org.adorsys.docusafe.business.impl.WithCache;
import org.adorsys.docusafe.spring.annotation.UseDocusafeSpringConfiguration;
import org.adorsys.docusafe.spring.factory.SpringExtendedStoreConnectionFactory;
import org.adorsys.encobject.service.api.ExtendedStoreConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sample config for the docusafe. Beware of the wrapping for exception handling.
 * @author fpo
 *
 */
@Configuration
@UseDocusafeSpringConfiguration
public class DocuSafeConfig {

	@Bean
	public DocumentSafeService docusafe(SpringExtendedStoreConnectionFactory springExtendedStoreConnectionFactory){
		return new ExceptionHandlingDocumentSafeService(new DocumentSafeServiceImpl(WithCache.TRUE, springExtendedStoreConnectionFactory.getExtendedStoreConnectionWithSubDir(null)));
	}
}
