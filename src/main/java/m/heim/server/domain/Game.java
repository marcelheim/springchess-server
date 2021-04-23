package m.heim.server.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import m.heim.server.enums.GameStatus;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
public class Game {
    public Game(Integer boardId, String name) {
        this.boardId = boardId;
        this.name = name;
    }

    public Game(String name) {
        this.name = name;
    }

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private Integer id;

    @Column
    private Integer boardId;

    @Column(nullable = false, unique = true)
    @Setter private String name;

    @Column
    @Setter private String gameStatus;

    @OneToOne
    @Setter private User white;

    @OneToOne
    @Setter private User black;
}
