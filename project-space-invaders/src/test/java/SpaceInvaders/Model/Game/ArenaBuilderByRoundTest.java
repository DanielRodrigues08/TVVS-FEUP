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

    @BeforeEach
    public void setUp() {
        arenaBuilder = assertDoesNotThrow(() -> new ArenaBuilderByRound(5));
        arenaBuilder.setArenaLines(LINES_ROUND5);
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

    @Test
    public void testGetWidth() {
        assertEquals(LINES_ROUND5.getFirst().length(), arenaBuilder.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(LINES_ROUND5.size(), arenaBuilder.getHeight());
    }

    @Test
    public void testGetRound() {
        int expectedRound = 5;
        assertEquals(expectedRound, arenaBuilder.getRound());
    }

    @Test
    public void testSetArenaLines() {
        List<String> expectedLines = Arrays.asList("line1", "line2");
        arenaBuilder.setArenaLines(expectedLines);
        assertEquals(expectedLines, arenaBuilder.getArenaLines());
    }

    @Test
    public void testGetArenaLines() {
        assertEquals(LINES_ROUND5, arenaBuilder.getArenaLines());
    }

    @ParameterizedTest
    @MethodSource("testCreateShipValues")
    public void testCreateShip(List<String> lines, Ship expectedShip) {
        arenaBuilder.setArenaLines(lines);
        Ship ship = arenaBuilder.createShip();

        assertEquals(expectedShip, ship);
    }

    @Test
    public void testCreateAliens() {
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
    public void testCreateWalls() {
        List<Wall> expectedWalls = List.of(
                new Wall(new Position(2, 0))
        );
        arenaBuilder.setArenaLines(NULL_SHIP);

        List<Wall> walls = arenaBuilder.createWalls();
        assertEquals(expectedWalls, walls);
    }

    @Test
    public void testCreateCoverWalls() {
        List<CoverWall> expectedCoverWalls = List.of(
                new CoverWall(new Position(3, 0), 0)
        );
        arenaBuilder.setArenaLines(NULL_SHIP);

        List<CoverWall> coverWalls = arenaBuilder.createCoverWalls();
        assertEquals(expectedCoverWalls, coverWalls);
    }

    @Test
    public void testCreateArena() {
        arenaBuilder.setArenaLines(LINES_ROUND3);

        Ship ship = new Ship(new Position(2, 1), 100, 50);
        Alien alien = new Alien(new Position(2, 0), 160, 160, 100, AlienState.PASSIVE, 0);
        CoverWall coverWall = new CoverWall(new Position(0, 1), 100);
        Wall wall = new Wall(new Position(0, 0));
        int expectedRound = 5;
        int expectedWidth = 3;
        int expectedHeight = 2;

        Arena arena = arenaBuilder.buildArena();

        assertEquals(expectedRound, arena.getRound());
        assertEquals(expectedWidth, arena.getWidth());
        assertEquals(expectedHeight, arena.getHeight());
        assertEquals(ship, arena.getShip());
        assertEquals(List.of(alien), arena.getAliens());
        assertEquals(List.of(wall), arena.getWalls());
        assertEquals(List.of(coverWall), arena.getCoverWalls());
        assertTrue(arena.getProjectiles().isEmpty());

    }

}