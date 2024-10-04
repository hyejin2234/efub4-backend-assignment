package efub.assignment.community.post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import efub.assignment.community.account.domain.Account;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.PostUpdateDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostTest {

    @Autowired
    private PostRepository postRepository;

    static Account account;
    static Board board;


    @BeforeAll
    static void beforeAll() {
        account = Account.builder()
                .email("abcd@gmail.com")
                .password("123akd!")
                .nickname("닉네임")
                .university("이화여대")
                .studentId("123456")
                .build();

        board = Board.builder()
                .account(account)
                .boardName("boardName")
                .boardDescription("des")
                .boardNotice("notice")
                .build();
    }

    // 성공
    @Test
    public void 게시글_제목본문_수정_테스트(){

        final String title = "title";
        final String content = "content";
        final String writerOpen = "open";

        Post post1 = Post.builder()
                .account(account)
                .board(board)
                .title(title)
                .content(content)
                .writerOpen(writerOpen)
                .build();

        final String updatedTitle = "변경title";
        final String updatedContent = "변경content";

        post1.update(new PostUpdateDto(updatedTitle,updatedContent));

        assertThat(post1.getTitle(), is(updatedTitle));
        assertThat(post1.getContent(), is(updatedContent));
    }

    // 실패
    @Test
    public void 게시글_긴제목_저장_테스트() {

        final String tooLongTitle = "This title is way too long and exceeds the maximum length of 50 characters";
        final String content = "content";
        final String writerOpen = "open";

        Post post = Post.builder()
                .account(account)
                .board(board)
                .title(tooLongTitle)
                .content(content)
                .writerOpen(writerOpen)
                .build();

        assertThat(postRepository.save(post), is(post));
    }
}
