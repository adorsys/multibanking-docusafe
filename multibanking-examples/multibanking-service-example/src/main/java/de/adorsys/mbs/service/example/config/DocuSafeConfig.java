package de.adorsys.mbs.service.example.config;

import de.adorsys.multibanking.auth.SystemContext;
import de.adorsys.multibanking.auth.UserContext;
import org.adorsys.docusafe.business.types.UserID;
import org.adorsys.docusafe.business.types.complex.UserIDAuth;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.adorsys.docusafe.spring.annotation.UseDocusafeSpringConfiguration;
import org.adorsys.docusafe.spring.factory.SpringCachedTransactionalDocusafeServiceFactory;
import org.adorsys.encobject.domain.ReadKeyPassword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sample config for the docusafe. Beware of the wrapping for exception handling.
 * @author fpo
 *
 */
@UseDocusafeSpringConfiguration
@Configuration
public class DocuSafeConfig {

    @Value("${docusafe.system.user.name}")
    String docusafeSystemUserName;
    @Value("${docusafe.system.user.password}")
    String docusafeSystemUserPassword;

    @Bean
    SystemContext systemContext() {
        UserIDAuth systemId = new UserIDAuth(new UserID(docusafeSystemUserName), new ReadKeyPassword(docusafeSystemUserPassword));
        UserContext userContext = new UserContext();
        userContext.setAuth(systemId);
        return new SystemContext(userContext);
    }

	@Bean
    CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService(SystemContext systemContext, SpringCachedTransactionalDocusafeServiceFactory factory){
		CachedTransactionalDocumentSafeService safe = factory.getCachedTransactionalDocumentSafeServiceWithSubdir(null);
		UserIDAuth systemId = systemContext.getUser().getAuth();
		if(!safe.userExists(systemContext.getUser().getAuth().getUserID())){
			safe.createUser(systemId);
		}
		return safe;
	}
}
