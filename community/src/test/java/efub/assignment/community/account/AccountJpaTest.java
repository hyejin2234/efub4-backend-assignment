package efub.assignment.community.account;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import efub.assignment.community.account.domain.Account;
import org.junit.jupiter.api.BeforeAll;
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
    public void 사용자_생성_테스트(){

        final String email = "abcd@gmail.com";
        final String password = "123akd!";
        final String nickname = "닉네임";
        final String university = "이화";
        final String studentId = "123456";

        Account account = Account.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();

        testEntityManager.persist(account);

        assertTrue(accountRepository.findById(account.getAccountId()).isPresent(), "Account should be present");
        assertThat(accountRepository.findById(account.getAccountId()).get(),is(account));
    }

    // 성공
    @Test
    public void 사용자_이메일로조회_테스트() {
        final String email = "abcd@gmail.com";
        final String password = "123akd!";
        final String nickname = "닉네임";
        final String university = "이화";
        final String studentId = "123456";

        Account account = Account.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();

        testEntityManager.persist(account);

        assertTrue(accountRepository.findByEmail(email).isPresent(), "Account should be present");
        assertThat(accountRepository.findByEmail(email).get(),is(account));
    }

    // 실패
    @Test
    public void 틀린닉네임으로_사용자존재여부_테스트() {

        final String email = "abcd@gmail.com";
        final String password = "123akd!";
        final String nickname = "닉네임";
        final String university = "이화";
        final String studentId = "123456";

        Account account = Account.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();

        final String wrongNickname = "틀린닉네임";

        testEntityManager.persist(account);

        assertTrue(accountRepository.existsByNickname(wrongNickname));
    }
}