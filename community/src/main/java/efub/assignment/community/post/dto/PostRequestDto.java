package efub.assignment.community.post.dto;


import efub.assignment.community.account.domain.Account;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {
    @NotBlank(message = "게시판 id는 필수입니다.")
    private String boardId;

    @NotBlank(message = "계정 닉네임은 필수입니다.")
    private String writerNickname;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Value("${writer.open:false}")
    private String writerOpen;

    @Builder
    public PostRequestDto(String boardId, String writerNickname, String title, String content, String writerOpen){
        this.boardId = boardId;
        this.writerNickname = writerNickname;
        this.title = title;
        this.content = content;
        this.writerOpen = writerOpen;
    }

    public Post toEntity(Board board, Account account){
        return Post.builder()
                .board(board)
                .account(account)  //글쓴이 accountId
                .title(this.title) //제목
                .content(this.content) //내용
                .writerOpen(this.writerOpen)
                .build();
    }
}
