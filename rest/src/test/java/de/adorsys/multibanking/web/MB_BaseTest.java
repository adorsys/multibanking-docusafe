package de.adorsys.multibanking.web;

import de.adorsys.multibanking.domain.BankEntity;
import de.adorsys.multibanking.service.BankService;
import de.adorsys.multibanking.web.base.BaseControllerIT;
import de.adorsys.multibanking.web.base.entity.BankAccessStructure;
import de.adorsys.multibanking.web.base.entity.UserPasswordTuple;
import org.adorsys.cryptoutils.exceptions.BaseException;
import org.adorsys.encobject.service.api.ExtendedStoreConnection;
import org.junit.Assume;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.Optional;

/**
 * Created by peter on 07.05.18 at 17:34.
 */
public abstract class MB_BaseTest extends BaseControllerIT {
    private final static Logger LOGGER = LoggerFactory.getLogger(MB_BaseTest.class);
    @Autowired
    public BankService bankService;

    @Autowired
    private ExtendedStoreConnection extendedStoreConnection;

    public UserPasswordTuple userPasswordTuple;
    public BankAccessStructure theBeckerTuple = new BankAccessStructure("19999999", "m.becker", "12345");
    public String PIN = "12345";
    public String WRONG_PIN = "22344";

    @Before
    public void setupBank() throws Exception {
        LOGGER.info("SETUP BANK !");
        new BaseException("Stack");
        extendedStoreConnection.listAllBuckets().forEach(bucket -> {
            if (! bucket.getObjectHandle().getContainer().equals("bp-system")) {
                LOGGER.info("DELETE " + bucket.toString());
                extendedStoreConnection.deleteContainer(bucket);
            }
        });
        userPasswordTuple = new UserPasswordTuple("peter", "alwaysTheSamePassword");
        auth(userPasswordTuple);

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mock_bank.json");
        bankService.importBanks(inputStream);
        Optional<BankEntity> bankEntity = bankService.findByBankCode("19999999");
        Assume.assumeTrue(bankEntity.isPresent());
    }
}
