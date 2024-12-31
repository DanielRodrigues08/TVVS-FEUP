package SpaceInvaders.Model.Game;

import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArenaBuilderByRoundTest {

    private static final List<String> LINES_ROUND5 = Arrays.asList(
            "   #",
            "W AS"
    );

    private static final List<String> LINES_ROUND3 = Arrays.asList(
            "# A",
            "W S"

    );

    private static final List<String> NULL_SHIP = List.of(
            "A #W",
            "A   ",
            "A   ",
            "A   ",
            "A   ",
            "    "
    );

    private ArenaBuilderByRound arenaBuilder;

    private static Stream<Arguments> testArenaBuilderByRoundValues() {
        return Stream.of(
                Arguments.of(5, 5, LINES_ROUND5),
                Arguments.of(6, 6, LINES_ROUND3)
        );
    }

    private static Stream<Arguments> testCreateShipValues() {
        return Stream.of(
                Arguments.of(LINES_ROUND5, new Ship(new Position(3, 1), 0, 0)),
                Arguments.of(NULL_SHIP, null)
        );
    }

    @ParameterizedTest
    @MethodSource("testArenaBuilderByRoundValues")
    public void testArenaBuilderByRound(int round, int expectedRound, List<String> expectedLines) {
        ArenaBuilderByRound arenaBuilderByRound = assertDoesNotThrow(() -> new ArenaBuilderByRound(round));
        assertEquals(expectedRound, arenaBuilderByRound.getRound());
        assertEquals(expectedLines, arenaBuilderByRound.getArenaLines());
    }

    @Test
    public void testInvalidArenaBuilderByRound() {
        assertThrows(Exception.class, () -> new ArenaBuilderByRound(-1));
    }

    @BeforeEach
    void setUp() {
        arenaBuilder = assertDoesNotThrow(() -> new ArenaBuilderByRound(5));
        arenaBuilder.setArenaLines(LINES_ROUND5);
    }

    @Test
    void testGetWidth() {
        assertEquals(LINES_ROUND5.getFirst().length(), arenaBuilder.getWidth());
    }

    @Test
    void testGetHeight() {
        assertEquals(LINES_ROUND5.size(), arenaBuilder.getHeight());
    }

    @Test
    void testGetRound() {
        int expectedRound = 5;
        assertEquals(expectedRound, arenaBuilder.getRound());
    }

    @Test
    void testSetArenaLines() {
        List<String> expectedLines = Arrays.asList("line1", "line2");
        arenaBuilder.setArenaLines(expectedLines);
        assertEquals(expectedLines, arenaBuilder.getArenaLines());
    }

    @Test
    void testGetArenaLines() {
        assertEquals(LINES_ROUND5, arenaBuilder.getArenaLines());
    }

    @ParameterizedTest
    @MethodSource("testCreateShipValues")
    void testCreateShip(List<String> lines, Ship expectedShip) {
        arenaBuilder.setArenaLines(lines);
        Ship ship = arenaBuilder.createShip();

        assertEquals(expectedShip, ship);
    }

    @Test
    void testCreateAliens() {
        List<Alien> expectedAliens = List.of(
                new Alien(new Position(0, 0), 0, 0, 0, AlienState.PASSIVE, 0),
                new Alien(new Position(0, 1), 0, 0, 0, AlienState.ATTACKING, 1),
                new Alien(new Position(0, 2), 0, 0, 0, AlienState.PASSIVE, 2),
                new Alien(new Position(0, 3), 0, 0, 0, AlienState.ATTACKING, 0),
                new Alien(new Position(0, 4), 0, 0, 0, AlienState.PASSIVE, 1)

        );
        arenaBuilder.setArenaLines(NULL_SHIP);

        List<Alien> aliens = arenaBuilder.createAliens();
        assertEquals(expectedAliens, aliens);
    }

    @Test
    void testCreateWalls() {
        List<Wall> expectedWalls = List.of(
                new Wall(new Position(2, 0))
        );
        arenaBuilder.setArenaLines(NULL_SHIP);

        List<Wall> walls = arenaBuilder.createWalls();
        assertEquals(expectedWalls, walls);
    }

    @Test
    void testCreateCoverWalls() {
        List<CoverWall> expectedCoverWalls = List.of(
                new CoverWall(new Position(3, 0), 0)
        );
        arenaBuilder.setArenaLines(NULL_SHIP);

        List<CoverWall> coverWalls = arenaBuilder.createCoverWalls();
        assertEquals(expectedCoverWalls, coverWalls);
    }

}