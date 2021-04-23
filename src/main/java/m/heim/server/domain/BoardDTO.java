package m.heim.server.domain;

import com.github.bhlangonijr.chesslib.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardDTO {
    private String position;
    private Integer halfMoveCounter;
    private Integer boardId;

    public BoardDTO(Pair<Board, Integer> board) {
        position = board.getFirst().getFen();
        halfMoveCounter = board.getFirst().getHalfMoveCounter();
        boardId = board.getSecond();
    }
}
