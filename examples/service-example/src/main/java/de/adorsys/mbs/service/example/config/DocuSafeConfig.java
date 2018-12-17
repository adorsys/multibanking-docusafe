package de.adorsys.mbs.service.example.config;

import javax.annotation.PostConstruct;

import org.adorsys.cryptoutils.storeconnectionfactory.ExtendedStoreConnectionFactory;
import org.adorsys.docusafe.business.DocumentSafeService;
import org.adorsys.docusafe.business.impl.DocumentSafeServiceImpl;
import org.adorsys.docusafe.business.impl.WithCache;
import org.adorsys.docusafe.business.types.UserID;
import org.adorsys.docusafe.business.types.complex.UserIDAuth;
import org.adorsys.docusafe.spring.annotation.UseDocusafeSpringConfiguration;
import org.adorsys.docusafe.spring.factory.SpringExtendedStoreConnectionFactory;
import org.adorsys.encobject.domain.ReadKeyPassword;
import org.adorsys.encobject.service.api.ExtendedStoreConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.adorsys.multibanking.auth.SystemContext;
import de.adorsys.multibanking.auth.UserContext;
import de.adorsys.multibanking.service.base.ExceptionHandlingDocumentSafeService;

/**
 * Sample config for the docusafe. Beware of the wrapping for exception handling.
 * @author fpo
 *
 */
@Configuration
@UseDocusafeSpringConfiguration
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
	DocumentSafeService docusafe(SystemContext systemContext, SpringExtendedStoreConnectionFactory springExtendedStoreConnectionFactory){
		DocumentSafeService safe = new ExceptionHandlingDocumentSafeService(new DocumentSafeServiceImpl(WithCache.TRUE, springExtendedStoreConnectionFactory.getExtendedStoreConnectionWithSubDir("docusafe-config")));
		UserIDAuth systemId = systemContext.getUser().getAuth();
		if(!safe.userExists(systemContext.getUser().getAuth().getUserID())){
			safe.createUser(systemId);
		}
		return safe;
	}
}
