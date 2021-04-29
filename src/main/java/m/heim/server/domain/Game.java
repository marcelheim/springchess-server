package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Game {@link Entity} Modell in der Java Persistence API
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
public class Game {
    /**
     * Konstruktor eines Games
     * @param boardId Id des verwendeten Boards in der GameListe im {@link m.heim.server.service.GameService}
     * @param name Name des Spiels
     */
    public Game(Integer boardId, String name) {
        this.boardId = boardId;
        this.name = name;
    }

    /**
     * Id des Spiels
     * {@link Id} - Primary Key
     * {@link Column} - Spalte, nicht null und einzigartig
     */
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private Integer id;
    /**
     * Id des zugehörigen Board
     * {@link Column} - Spalte
     */
    @Column
    private Integer boardId;
    /**
     * Name des Spiels
     * {@link Column} - Spalte, nicht null und einzigartig
     */
    @Column(nullable = false, unique = true)
    @Setter private String name;
    /**
     * Status des Spiels
     * {@link Column} - Spalte
     */
    @Column
    @Setter private String gameStatus;
    /**
     * Weißer Spieler des Spiels
     * {@link OneToOne} - 1:1 Join mit der Tabelle {@link User}
     * {@link Column} - Spalte
     */
    @OneToOne
    @Setter private User white;
    /**
     * Schwarzer Spieler des Spiels
     * {@link OneToOne} - 1:1 Join mit der Tabelle {@link User}
     * {@link Column} - Spalte
     */
    @OneToOne
    @Setter private User black;
}
