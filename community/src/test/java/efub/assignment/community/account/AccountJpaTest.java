package efub.assignment.community.account;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import efub.assignment.community.account.domain.Account;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountJpaTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AccountRepository accountRepository;

    // 성공
    @Test
    public void Account_saveTest(){
        Account account = Account.builder()
                .email("abcd@gmail.com")
                .password("123akd!")
                .nickname("닉네임")
                .university("이화여대")
                .studentId("123456")
                .build();

        testEntityManager.persist(account);

        assertTrue(accountRepository.findById(account.getAccountId()).isPresent(), "Account should be present");
        assertThat(accountRepository.findById(account.getAccountId()).get(),is(account));
    }

    // 성공
    @Test
    public void Account_save_findByEmailTest() {
        Account account1 = Account.builder()
                .email("abcd@gmail.com")
                .password("123akd!")
                .nickname("닉네임")
                .university("이화여대")
                .studentId("123456")
                .build();

        testEntityManager.persist(account1);

        assertTrue(accountRepository.findByEmail("abcd@gmail.com").isPresent(), "Account should be present");
        assertThat(accountRepository.findByEmail("abcd@gmail.com").get(),is(account1));
    }

    // 실패
    @Test
    public void Account_save_existsByNicknameTest() {
        Account account1 = Account.builder()
                .email("abcd@gmail.com")
                .password("123akd!")
                .nickname("닉네임")
                .university("이화여대")
                .studentId("123456")
                .build();

        testEntityManager.persist(account1);

        assertTrue(accountRepository.existsByNickname("틀린닉네임"));
    }
}