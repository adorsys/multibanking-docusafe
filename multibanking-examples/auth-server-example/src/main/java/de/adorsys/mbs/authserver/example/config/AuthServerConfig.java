package de.adorsys.mbs.authserver.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.lockpersistence.client.LockClient;
import de.adorsys.lockpersistence.client.NoopLockClient;
import de.adorsys.sts.keymanagement.persistence.KeyStoreRepository;
import de.adorsys.sts.keymanagement.service.KeyManagementProperties;
import de.adorsys.sts.persistence.FsKeyStoreRepository;
import de.adorsys.sts.persistence.FsResourceServerRepository;
import de.adorsys.sts.persistence.FsUserDataRepository;
import de.adorsys.sts.persistence.KeyEntryMapper;
import de.adorsys.sts.resourceserver.persistence.ResourceServerRepository;
import de.adorsys.sts.resourceserver.service.UserDataRepository;
import de.adorsys.sts.token.passwordgrant.EnablePasswordGrant;
import org.adorsys.docusafe.business.types.UserID;
import org.adorsys.docusafe.business.types.complex.UserIDAuth;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.adorsys.docusafe.spring.annotation.UseDocusafeSpringConfiguration;
import org.adorsys.docusafe.spring.factory.SpringCachedTransactionalDocusafeServiceFactory;
import org.adorsys.encobject.domain.ReadKeyPassword;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnablePasswordGrant
@EnableConfigurationProperties
@UseDocusafeSpringConfiguration
public class AuthServerConfig {

    @Value("${docusafe.system.user.name}")
    String docusafeSystemUserName;
    @Value("${docusafe.system.user.password}")
    String docusafeSystemUserPassword;
    
    UserIDAuth systemId;
    
    @PostConstruct
    void postConstruct(){
		systemId = new UserIDAuth(new UserID(docusafeSystemUserName), new ReadKeyPassword(docusafeSystemUserPassword));
    }

	@Bean
	public CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService(SpringCachedTransactionalDocusafeServiceFactory factory) {
		CachedTransactionalDocumentSafeService service = factory.getCachedTransactionalDocumentSafeServiceWithSubdir(RandomStringUtils.randomAlphanumeric(10).toUpperCase());
		// Create system user.
		if(!service.userExists(systemId.getUserID())){
			service.createUser(systemId);
		}
		return service;
	}
    
	@Bean
	ResourceServerRepository resourceServerRepository(ObjectMapper objectMapper, CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService) {
		return new FsResourceServerRepository(systemId, cachedTransactionalDocumentSafeService, objectMapper);
	}

	@Bean
	UserDataRepository userDataRepository(ObjectMapper objectMapper, CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService) {
		return new FsUserDataRepository(cachedTransactionalDocumentSafeService, objectMapper);
	}

	@Bean
	KeyStoreRepository keyStoreRepository(ObjectMapper objectMapper, CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService, KeyManagementProperties keyManagementProperties) {
		return new FsKeyStoreRepository(systemId, cachedTransactionalDocumentSafeService, keyManagementProperties, new KeyEntryMapper(objectMapper));
	}
	
	private LockClient lockClient = new NoopLockClient();
	@Bean
	public LockClient getLockClient(){
		return lockClient;
	}
}
