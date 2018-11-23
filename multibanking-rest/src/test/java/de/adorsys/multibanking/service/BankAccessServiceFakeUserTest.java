package de.adorsys.multibanking.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.adorsys.multibanking.config.service.BaseServiceTest;
import de.adorsys.multibanking.exception.UserNotFoundException;
import de.adorsys.multibanking.service.old.TestConstants;
import de.adorsys.multibanking.service.producer.OnlineBankingServiceProducer;
import de.adorsys.onlinebanking.mock.MockBanking;
import figo.FigoBanking;

@RunWith(SpringRunner.class)
@ActiveProfiles("docusafe")
public class BankAccessServiceFakeUserTest extends BaseServiceTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(BankAccessServiceFakeUserTest.class);

    @MockBean
    private FigoBanking figoBanking;
    @MockBean
    private MockBanking mockBanking;
    @MockBean
    private OnlineBankingServiceProducer bankingServiceProducer;
    @Autowired
    private UserDataService uds;
    @Autowired
    private BankAccessService bankAccessService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
    	TestConstants.setup();
    }

    @Before
    public void beforeTest() throws IOException {
        LOGGER.info("BEFORE TEST START");
    	MockitoAnnotations.initMocks(this);
        when(bankingServiceProducer.getBankingService(anyString())).thenReturn(mockBanking);
        LOGGER.info("BEFORE TEST END");
    }

    @After
    public void after(){
        LOGGER.info("AFTER TEST START");
        if(userContext!=null)
    		LOGGER.debug(userContext.getRequestCounter().toString());
        LOGGER.info("AFTER TEST END");
    }

    @Test
    public void when_delete_bankAccesd_user_notExist_should_throw_exception() {
        LOGGER.info("TEST START");
    	// Inject a user, without creating that user in the storage.
    	auth("fakeUser", "fakePassword");

        // TODO Exception doesnt rise.
         thrown.expect(UserNotFoundException.class);
        bankAccessService.deleteBankAccess("badAccess");
        LOGGER.info("TEST END");
    }
}
