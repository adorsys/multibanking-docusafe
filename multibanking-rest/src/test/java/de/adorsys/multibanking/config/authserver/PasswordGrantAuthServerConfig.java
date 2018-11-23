package de.adorsys.multibanking.config.authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.multibanking.config.core.STSInMemoryConfig;
import de.adorsys.multibanking.service.base.ExceptionHandlingCachedTransactionalDocumentSafeService;
import de.adorsys.sts.persistence.FsUserDataRepository;
import de.adorsys.sts.resourceserver.persistence.InMemoryResourceServerRepository;
import de.adorsys.sts.resourceserver.persistence.ResourceServerRepository;
import de.adorsys.sts.resourceserver.service.UserDataRepository;
import de.adorsys.sts.token.passwordgrant.EnablePasswordGrant;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.adorsys.docusafe.spring.factory.SpringCachedTransactionalDocusafeServiceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@EnablePasswordGrant
@Import({ STSInMemoryConfig.class })
@EnableConfigurationProperties
@Profile("IntegrationTest")
public class PasswordGrantAuthServerConfig {

	@Bean
	ResourceServerRepository resourceServerRepository() {
		return new InMemoryResourceServerRepository();
	}

	@Bean
	UserDataRepository userDataRepository(ObjectMapper objectMapper, SpringCachedTransactionalDocusafeServiceFactory factory) {
		// Warning you can not use the client applications docusafe here.
		CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService = factory.getCachedTransactionalDocumentSafeServiceWithSubdir("Test-Auth-Server-Only");
		ExceptionHandlingCachedTransactionalDocumentSafeService exceptionWrappedService  = new ExceptionHandlingCachedTransactionalDocumentSafeService(cachedTransactionalDocumentSafeService);
		return new FsUserDataRepository(exceptionWrappedService, objectMapper);
	}
}
