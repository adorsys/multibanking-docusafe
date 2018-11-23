package de.adorsys.multibanking.service.base;

import org.adorsys.docusafe.business.types.UserID;
import org.adorsys.docusafe.business.types.complex.UserIDAuth;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.JWK;

/**
 * Service used to manage the storage user.
 * 
 * @author fpo
 *
 */
@Service
public class StorageUserService {
	private final static Logger LOGGER = LoggerFactory.getLogger(StorageUserService.class);

	@Autowired
	private CachedTransactionalDocumentSafeService cachedTransactionalDocumentSafeService;

	/**
	 * Check existence of the user with the given user id.
	 * 
	 * @return
	 */
	public boolean userExists(UserID userID) {
		return cachedTransactionalDocumentSafeService.userExists(userID);
	}
		
	/**
	 * Create a user in the underlying storage. Given his 
	 * userId and password.
	 * 
	 * @param userIDAuth
	 */
	public void createUser(UserIDAuth userIDAuth){
		cachedTransactionalDocumentSafeService.createUser(userIDAuth);
	}
	
	/**
	 * Remove the user and all his files from the repository.
	 * 
	 * @param userIDAuth
	 */
	public void deleteUser(UserIDAuth userIDAuth) {
		cachedTransactionalDocumentSafeService.destroyUser(userIDAuth);
	}
	
	/**
	 * Retrieves an encryption public key for this user. Key will be user to send sensitive informations
	 * to this application. Like the banking PIN.
	 * 
	 * @return
	 */
	public JWK findPublicEncryptionKey(UserID userID){
		return cachedTransactionalDocumentSafeService.findPublicEncryptionKey(userID).getValue();
	}	
}
