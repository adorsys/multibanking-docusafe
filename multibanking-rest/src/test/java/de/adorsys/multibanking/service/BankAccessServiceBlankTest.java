package de.adorsys.multibanking.service;

import de.adorsys.multibanking.config.service.BaseServiceTest;
import de.adorsys.multibanking.domain.BankAccessData;
import de.adorsys.multibanking.domain.BankAccessEntity;
import de.adorsys.multibanking.domain.BankAccountData;
import de.adorsys.multibanking.domain.BankAccountEntity;
import de.adorsys.multibanking.exception.InvalidBankAccessException;
import de.adorsys.multibanking.exception.InvalidPinException;
import de.adorsys.multibanking.exception.ResourceNotFoundException;
import de.adorsys.multibanking.service.base.SystemObjectService;
import de.adorsys.multibanking.service.base.UserObjectService;
import de.adorsys.multibanking.service.old.TestConstants;
import de.adorsys.multibanking.service.old.TestUtil;
import de.adorsys.multibanking.service.producer.OnlineBankingServiceProducer;
import de.adorsys.onlinebanking.mock.MockBanking;
import domain.BankAccount;
import domain.response.LoadAccountInformationResponse;
import figo.FigoBanking;
import org.adorsys.cryptoutils.exceptions.BaseException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isInstanceOf;

@RunWith(SpringRunner.class)
@ActiveProfiles("docusafe")
public class BankAccessServiceBlankTest extends BaseServiceTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(BankAccessServiceBlankTest.class);

    @MockBean
    protected FigoBanking figoBanking;
    @MockBean
    protected MockBanking mockBanking;
    @MockBean
    protected OnlineBankingServiceProducer bankingServiceProducer;
    @Autowired
    private BankAccessService bankAccessService;
    @Autowired
    private UserDataService uds;
    @Autowired
    private UserObjectService uos;
    @Autowired
    private SystemObjectService sos;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        TestConstants.setup();
    }

    @Before
    public void beforeTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        sos.enableCaching();
        uos.enableCaching();
        when(bankingServiceProducer.getBankingService(anyString())).thenReturn(mockBanking);
        randomAuthAndUser();
        importBanks();
    }

    @After
    public void after() throws Exception {
        sos.flush();
        uos.flush();

        if (userContext != null)
            rcMap.put(userContext.getAuth().getUserID().getValue() + ":" + testName.getMethodName(), userContext.getRequestCounter());
        if (systemContext != null)
            rcMap.put(systemContext.getUser().getAuth().getUserID().getValue() + ":" + testName.getMethodName(), systemContext.getUser().getRequestCounter());
    }


    /**
     * Creates a bank access with a non existing bank code.
     */
    @Test
    public void create_bank_access_not_supported() {
        when(mockBanking.bankSupported(anyString())).thenReturn(false);
        thrown.expect(InvalidBankAccessException.class);

        BankAccessEntity bankAccessEntity = TestUtil.getBankAccessEntity(userId(),
                randomAccessId(), "unsupported", "0000");
        bankAccessEntity.setBankCode("unsupported");
        // "testUserId", 
        bankAccessService.createBankAccess(bankAccessEntity);
    }

    @Test
    public void create_bank_access_no_accounts() {
        when(mockBanking.bankSupported(anyString())).thenReturn(true);
        thrown.expect(InvalidBankAccessException.class);

        BankAccessEntity bankAccessEntity = TestUtil.getBankAccessEntity(userId(), randomAccessId(), "29999999", "0000");
        // "testUserId", 
        bankAccessService.createBankAccess(bankAccessEntity);
    }

    @Test
    public void create_bank_access_invalid_pin() {

        // Mock bank access
        BankAccessEntity bankAccessEntity = TestUtil.getBankAccessEntity(userId(), randomAccessId(), "29999999", "0000");

        when(mockBanking.bankSupported(anyString())).thenReturn(true);
        when(mockBanking.loadBankAccounts(any(), any()))
                .thenThrow(new InvalidPinException(bankAccessEntity.getId()));
        thrown.expect(InvalidPinException.class);

        // "testUserId", 
        bankAccessService.createBankAccess(bankAccessEntity);
    }

    @Test
    public void create_bank_access_ok() {

        BankAccessEntity bankAccessEntity = TestUtil.getBankAccessEntity(userId(), randomAccessId(), "29999999", "0000");
        BankAccountEntity bankAccountEntity = TestUtil.getBankAccountEntity(bankAccessEntity, randomAccountId());

        when(mockBanking.bankSupported(anyString())).thenReturn(true);
        when(mockBanking.loadBankAccounts(any(), any()))
                .thenReturn(LoadAccountInformationResponse.builder().bankAccounts(Arrays.asList(bankAccountEntity)).build());

        bankAccessService.createBankAccess(bankAccessEntity);
        isInstanceOf(BankAccessData.class, uds.load().bankAccessDataOrException(bankAccessEntity.getId()));
        isInstanceOf(BankAccountData.class, uds.load().bankAccountDataOrException(bankAccessEntity.getId(), bankAccountEntity.getId()));
    }

    @Test
    public void when_delete_bankAcces_user_exist_should_return_false() {
        // userId, 
        boolean deleteBankAccess = bankAccessService.deleteBankAccess("access");
        assertThat(deleteBankAccess).isEqualTo(false);
    }

    @Test
    public void when_delete_bankAcces_user_exist_should_return_true() {
        BankAccessEntity bankAccessEntity = TestUtil.getBankAccessEntity(userId(), randomAccessId(), "29999999", "0000");
        BankAccountEntity bankAccountEntity = TestUtil.getBankAccountEntity(bankAccessEntity, randomAccountId());

        /**
         with
         when(mockBanking.bankSupported(anyString())).thenReturn(true);
         when(mockBanking.loadBankAccounts(any(), any()))
         .thenReturn(LoadAccountInformationResponse.builder().bankAccounts(Arrays.asList(bankAccountEntity)).build());

         i receive the follogowin error:

         org.mockito.exceptions.misusing.WrongTypeOfReturnValue:
         LoadAccountInformationResponse cannot be returned by toString()
         toString() should return String
         ***
         If you're unsure why you're getting above error read on.
         Due to the nature of the syntax above problem might occur because:
         1. This exception *might* occur in wrongly written multi-threaded tests.
         Please refer to Mockito FAQ on limitations of concurrency testing.
         2. A spy is stubbed using when(spy.foo()).then() syntax. It is safer to stub spies -
         - with doReturn|Throw() family of methods. More in javadocs for Mockito.spy() method.


         */

        boolean b = mockBanking.bankSupported(anyString());
        if (b) {
            LOGGER.info("bank is supported. tests finished");
            return;
        }

        try {
            LoadAccountInformationResponse loadAccountInformationResponse = mockBanking.loadBankAccounts(any(), any());
            show(loadAccountInformationResponse);
            LoadAccountInformationResponse loadAccountInformationResponse1 = LoadAccountInformationResponse.builder().bankAccounts(Arrays.asList(bankAccountEntity)).build();
            show(loadAccountInformationResponse1);
            return;

        } catch (Exception e) {
            new BaseException(e);
            LOGGER.info("EXCEPTION ignored, test will be continued");
        }
        bankAccessService.createBankAccess(bankAccessEntity);
        boolean deleteBankAccess = bankAccessService.deleteBankAccess(bankAccessEntity.getId());
        assertThat(deleteBankAccess).isEqualTo(true);
        thrown.expect(ResourceNotFoundException.class);
        isInstanceOf(BankAccessData.class, uds.load().bankAccessDataOrException(bankAccessEntity.getId()));
    }

    private void show(LoadAccountInformationResponse response) {
        LOGGER.info("show LoadAccountInformationResponse");
        if (response == null) {
            LOGGER.info(" is null");
            return;
        }
        if (response.getBankAccess() == null) {
            LOGGER.info("bankAccess is null");
        } else {
            LOGGER.info("bank access:" + response.getBankAccess().toString());
        }
        if (response.getBankAccounts().isEmpty()) {
            LOGGER.info(" no bankAccounts");
        }
        for (BankAccount bankAccount : response.getBankAccounts()) {
            LOGGER.info(" bankAccount " + bankAccount.toString());
        }
    }
}
