package de.adorsys.multibanking.service.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.multibanking.auth.UserContext;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;

/**
 * Services that access the repository of the current user use this service.
 * 
 * @author fpo 2018-04-06 05:00
 *
 */
public class UserObjectService extends CacheBasedService {
	private UserContext userContext;

	public UserObjectService(ObjectMapper objectMapper, UserContext userContext, CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService) {
		super(objectMapper, cachedTransactionalDocumentSafeService);
		this.userContext = userContext;
	}

	@Override
	public UserContext user() {
		return userContext;
	}
}
