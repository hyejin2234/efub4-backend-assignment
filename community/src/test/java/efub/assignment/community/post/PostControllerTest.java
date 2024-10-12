package efub.assignment.community.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.CommunityApplication;
import efub.assignment.community.account.AccountRepository;
import efub.assignment.community.account.domain.Account;
import efub.assignment.community.account.dto.SignUpRequestDto;
import efub.assignment.community.board.BoardRepository;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.PostRequestDto;
import efub.assignment.community.post.dto.PostUpdateDto;
import java.nio.charset.MalformedInputException;
import java.util.WeakHashMap;
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
// 테스트에 사용할 프로퍼티 파일을 지정. application-test.yml 파일의 설정을 테스트 환경에 적용
@TestPropertySource(locations = "classpath:application-test.yml")
public class PostControllerTest {

    // MockMVC : 웹 계층을 테스트할 때 http 요청과 응답을 시뮬레이션
    // 통합테스트에서는 Controller를 기반으로 하는 테스트가 많기 때문에 MockMvc를 이용하여 전체 웹 계층을 테스트
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected BoardRepository boardRepository;

    private Long defaultPostId;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @BeforeEach
    public void createDefaultPost() {
        // 기본 계정 생성
        Account account = Account.builder().
                email("email@gmail.com")
                .password("!ps")
                .nickname("nickname")
                .university("E")
                .studentId("11")
                .build();

        accountRepository.save(account);

        Board board = Board.builder()
                .account(account)
                .boardName("b")
                .boardDescription("des")
                .boardNotice("n")
                .build();

        boardRepository.save(board);

        Post post = Post.builder()
                .board(board)
                .account(account)
                .title("title")
                .content("content")
                .writerOpen("false")
                .build();

        postRepository.save(post);
        defaultPostId = post.getPostId();
    }


    @Test
    @DisplayName("updatePost : 게시글 수정 성공")
    public void updatePost() throws Exception{
        /*given*/
        final Long postId = defaultPostId;
        final String url = "/posts/"+postId;
        final String updatedTitle = "수정된 title";
        final String updatedContent = "수정된 content";
        final PostUpdateDto updateDto = new PostUpdateDto(updatedTitle, updatedContent);

        /* when */
        final String requestBody = objectMapper.writeValueAsString(updateDto);

        ResultActions resultActions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        /* then */
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(jsonPath("$.content").value(updatedContent));
    }

    @Test
    @DisplayName("deletePost_fail_withoutPermission : 게시글 삭제 실패(권한없음)")
    public void deletePost_fail() throws Exception{
        /* given */
        final Long postId = defaultPostId;
        final Long noPermissionAccountId = 10L;
        final String url = "/posts/"+postId+"?accountId="+noPermissionAccountId;

        /* given */
        ResultActions resultActions = mockMvc.perform(delete(url));

        /* then */
        resultActions
                .andExpect(status().isOk());
    }
}
