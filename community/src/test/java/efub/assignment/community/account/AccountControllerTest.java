package efub.assignment.community.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.CommunityApplication;
import efub.assignment.community.account.dto.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data.sql")
@ActiveProfiles("test")
@ContextConfiguration(classes = CommunityApplication.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class AccountControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AccountRepository accountRepository;

    @BeforeEach
    public void setMockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("createAccount : 회원가입 성공")
    public void createAccount() throws Exception{
        /*given*/
        final String url = "/accounts";
        final String email = "jin@ewhain.net";
        final String password = "jin1234!";
        final String nickname = "jin";
        final String university = "이화여자대학교";
        final String studentId = "2217040";
        final SignUpRequestDto requestDto = createDefaultSignUpRequestDto(email, password, nickname, university, studentId);

        /* when */
        final String requestBody = objectMapper.writeValueAsString(requestDto);

        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        /* then */
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").isNotEmpty())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.university").value(university))
                .andExpect(jsonPath("$.studentId").value(studentId));
    }

    @Test
    @DisplayName("createAccount_fail_invalidEmailFormat : 회원가입 실패(유효하지 않은 이메일)")
    public void createAccount_fail() throws Exception{
        /*given*/
        final String url = "/accounts";
        final String email = "failEmailFormat";
        final String password = "jin1234!";
        final String nickname = "jin";
        final String university = "이화여자대학교";
        final String studentId = "2217040";
        final SignUpRequestDto requestDto = createDefaultSignUpRequestDto(email, password, nickname, university, studentId);

        /* when */
        final String requestBody = objectMapper.writeValueAsString(requestDto);

        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        /* then */
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").isNotEmpty())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.university").value(university))
                .andExpect(jsonPath("$.studentId").value(studentId));
    }

    private SignUpRequestDto createDefaultSignUpRequestDto(String email,String password, String nickname, String university, String studentId){
        return SignUpRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();
    }
}
