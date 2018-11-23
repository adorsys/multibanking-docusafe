package de.adorsys.multibanking.config.service;

import de.adorsys.multibanking.auth.SystemContext;
import de.adorsys.multibanking.auth.UserContext;
import de.adorsys.multibanking.service.old.TestConstants;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

/**
 * This is a sample configuration for the system user. The system user will be
 * set up by the containing application.
 *
 * @author fpo
 *
 */
@Configuration
public class SystemAuthConfig {
	@Autowired
	private CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService;

	@PostConstruct
	public void postConstruct(){
		SystemContext sys = TestConstants.getSystemUserIDAuth();
		UserContext user = sys.getUser();
		if(!cachedTransactionalDocumentSafeService.userExists(user.getAuth().getUserID())){
			cachedTransactionalDocumentSafeService.createUser(user.getAuth());
		}
	}

    @Bean
    @Primary
    public SystemContext systemIDAuth(){
    	return TestConstants.getSystemUserIDAuth();
    }
}
