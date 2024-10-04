package efub.assignment.community.account;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import efub.assignment.community.account.domain.Account;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountTest {

    @Autowired
    private AccountRepository accountRepository;

    final String password = "123akd!";
    final String studentId = "123456";
    final String nickname = "닉네임";
    final String university = "이화";

    // 성공
    @Test
    public void 사용자속성_반환_테스트(){

        final String email = "abcd@gmail.com";

        Account account = Account.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();

        assertThat(account.getEmail(), is(email));
        assertThat(account.getPassword(), is(password));
        assertThat(account.getNickname(), is(nickname));
        assertThat(account.getUniversity(), is(university));
        assertThat(account.getStudentId(), is(studentId));
    }

    // 실패
    @Test
    void 사용자_이메일_널_테스트() {

        final String nullEmail = null;

        Account account = Account.builder()
                .email(nullEmail)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();

        assertThat(accountRepository.save(account), is(account));
    }
}
