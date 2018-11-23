package de.adorsys.multibanking.service.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.multibanking.auth.SystemContext;
import de.adorsys.multibanking.auth.UserContext;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;

/**
 * Service that access the system repository use this service.
 * 
 * @author fpo 2018-04-06 06:00
 *
 */
public class SystemObjectService extends CacheBasedService {
	private SystemContext systemContext;

	public SystemObjectService(ObjectMapper objectMapper, SystemContext systemContext, CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService) {
	    super(objectMapper, cachedTransactionalDocumentSafeService);
		this.systemContext = systemContext;
	}

	@Override
	public UserContext user() {
		return systemContext.getUser();
	}
}
