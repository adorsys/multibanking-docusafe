package de.adorsys.multibanking.service.base;

import de.adorsys.multibanking.domain.UserEntity;
import de.adorsys.multibanking.exception.ResourceNotFoundException;
import org.adorsys.cryptoutils.exceptions.BaseException;
import org.adorsys.docusafe.business.types.UserID;
import org.adorsys.docusafe.business.types.complex.BucketContentFQN;
import org.adorsys.docusafe.business.types.complex.DSDocument;
import org.adorsys.docusafe.business.types.complex.DocumentDirectoryFQN;
import org.adorsys.docusafe.business.types.complex.DocumentFQN;
import org.adorsys.docusafe.business.types.complex.UserIDAuth;
import org.adorsys.docusafe.cached.transactional.CachedTransactionalDocumentSafeService;
import org.adorsys.encobject.types.ListRecursiveFlag;
import org.adorsys.encobject.types.PublicKeyJWK;
import org.apache.commons.lang3.StringUtils;

public class ExceptionHandlingCachedTransactionalDocumentSafeService implements CachedTransactionalDocumentSafeService {

    private CachedTransactionalDocumentSafeService delegate;

    public ExceptionHandlingCachedTransactionalDocumentSafeService(CachedTransactionalDocumentSafeService delegate) {
        this.delegate = delegate;
    }

    /**
     * ===========================================================================================
     */
    @Override
    public void createUser(UserIDAuth userIDAuth) {
        delegate.createUser(userIDAuth);
    }

    @Override
    public void destroyUser(UserIDAuth userIDAuth) {
        delegate.destroyUser(userIDAuth);
    }

    @Override
    public boolean userExists(UserID userID) {
        return delegate.userExists(userID);
    }

    @Override
    public void grantAccessToNonTxFolder(UserIDAuth userIDAuth, UserID userID, DocumentDirectoryFQN documentDirectoryFQN) {
        try {
            delegate.grantAccessToNonTxFolder(userIDAuth, userID, documentDirectoryFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public PublicKeyJWK findPublicEncryptionKey(UserID userID) {
        try {
            return delegate.findPublicEncryptionKey(userID);
        } catch (BaseException b) {
            throw checkContainer(b, userID);
        }
    }

    @Override
    public void nonTxStoreDocument(UserIDAuth userIDAuth, DSDocument dsDocument) {
        try {
            delegate.nonTxStoreDocument(userIDAuth, dsDocument);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public DSDocument nonTxReadDocument(UserIDAuth userIDAuth, DocumentFQN documentFQN) {
        try {
            return delegate.nonTxReadDocument(userIDAuth, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public boolean nonTxDocumentExists(UserIDAuth userIDAuth, DocumentFQN documentFQN) {
        try {
            return delegate.nonTxDocumentExists(userIDAuth, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public void nonTxDeleteDocument(UserIDAuth userIDAuth, DocumentFQN documentFQN) {
        try {
            delegate.nonTxDeleteDocument(userIDAuth, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public void nonTxDeleteFolder(UserIDAuth userIDAuth, DocumentDirectoryFQN documentDirectoryFQN) {
        try {
            delegate.nonTxDeleteFolder(userIDAuth, documentDirectoryFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public BucketContentFQN nonTxListDocuments(UserIDAuth userIDAuth, DocumentDirectoryFQN documentDirectoryFQN, ListRecursiveFlag listRecursiveFlag) {
        try {
            return delegate.nonTxListDocuments(userIDAuth, documentDirectoryFQN, listRecursiveFlag);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public void nonTxStoreDocument(UserIDAuth userIDAuth, UserID userID, DSDocument dsDocument) {
        try {
            delegate.nonTxStoreDocument(userIDAuth, userID, dsDocument);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public DSDocument nonTxReadDocument(UserIDAuth userIDAuth, UserID userID, DocumentFQN documentFQN) {
        try {
            return delegate.nonTxReadDocument(userIDAuth, userID, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public boolean nonTxDocumentExists(UserIDAuth userIDAuth, UserID userID, DocumentFQN documentFQN) {
        try {
            return delegate.nonTxDocumentExists(userIDAuth, userID, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    /**
     * ===========================================================================================
     */


    @Override
    public void beginTransaction(UserIDAuth userIDAuth) {
        try {
            delegate.beginTransaction(userIDAuth);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public void txStoreDocument(UserIDAuth userIDAuth, DSDocument dsDocument) {
        try {
            delegate.txStoreDocument(userIDAuth, dsDocument);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public DSDocument txReadDocument(UserIDAuth userIDAuth, DocumentFQN documentFQN) {
        try {
            return delegate.txReadDocument(userIDAuth, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public void txDeleteDocument(UserIDAuth userIDAuth, DocumentFQN documentFQN) {
        try {
            delegate.txDeleteDocument(userIDAuth, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public BucketContentFQN txListDocuments(UserIDAuth userIDAuth, DocumentDirectoryFQN documentDirectoryFQN, ListRecursiveFlag listRecursiveFlag) {
        try {
            return delegate.txListDocuments(userIDAuth, documentDirectoryFQN, listRecursiveFlag);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public boolean txDocumentExists(UserIDAuth userIDAuth, DocumentFQN documentFQN) {
        try {
            return delegate.txDocumentExists(userIDAuth, documentFQN);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }
    }

    @Override
    public void txDeleteFolder(UserIDAuth userIDAuth, DocumentDirectoryFQN documentDirectoryFQN) {
        try {
        } catch (BaseException b) {
            delegate.txDeleteFolder(userIDAuth, documentDirectoryFQN);
            throw checkContainer(b, userIDAuth);
        }

    }

    @Override
    public void endTransaction(UserIDAuth userIDAuth) {
        try {
            delegate.endTransaction(userIDAuth);
        } catch (BaseException b) {
            throw checkContainer(b, userIDAuth);
        }

    }


    private static boolean isContainerNotExist(BaseException b) {
        if (b instanceof org.adorsys.encobject.exceptions.ResourceNotFoundException) {
            return true;
        }
        String message = b.getMessage();
        return StringUtils.startsWith(message, "Container") && StringUtils.endsWith(message, "does not exist");
    }

    private static RuntimeException checkContainer(BaseException b, UserIDAuth userIDAuth) {
        return checkContainer(b, userIDAuth.getUserID());
    }

    private static RuntimeException checkContainer(BaseException b, UserID userID) {
        if (isContainerNotExist(b)) return new ResourceNotFoundException(UserEntity.class, userID.getValue());
        return b;
    }


}
