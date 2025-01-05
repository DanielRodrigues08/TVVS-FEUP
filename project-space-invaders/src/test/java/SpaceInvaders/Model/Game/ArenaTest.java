package SpaceInvaders.Model.Game;

import SpaceInvaders.Model.Game.Collectables.Collectable;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArenaTest {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;
    private Arena arena;

    private static Stream<Arguments> valuesFreeArenaPosition() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(2, 2);

        var a1 = mock(Alien.class);
        var a2 = mock(Alien.class);
        when(a1.getPosition()).thenReturn(p1);
        when(a2.getPosition()).thenReturn(p2);

        var cw1 = mock(CoverWall.class);
        var cw2 = mock(CoverWall.class);
        when(cw1.getPosition()).thenReturn(p1);
        when(cw2.getPosition()).thenReturn(p2);

        var as1 = mock(AlienShip.class);
        var as2 = mock(AlienShip.class);
        when(as1.getPosition()).thenReturn(p1);
        when(as2.getPosition()).thenReturn(p2);

        return Stream.of(
                Arguments.of(p1, List.of(a1, a2), List.of(cw1), null, false),
                Arguments.of(p1, List.of(a2), List.of(cw1, cw2), null, false),
                Arguments.of(p1, List.of(a2), List.of(cw2), null, true),
                Arguments.of(p1, List.of(a2), List.of(cw2), as1, false),
                Arguments.of(p1, List.of(a2), List.of(cw2), as2, true)
        );
    }

    @BeforeEach
    public void setUp() {
        arena = new Arena(WIDTH, HEIGHT);
    }

    @Test
    public void testGetWidth() {
        assertEquals(WIDTH, arena.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(HEIGHT, arena.getHeight());
    }

    @Test
    public void testGetRound() {
        int numExpected = 2;
        arena.setRound(numExpected);
        assertEquals(numExpected, arena.getRound());
    }

    @Test
    public void testGetScore() {
        int scoreExpected = 0;
        assertEquals(scoreExpected, arena.getScore());
    }

    @Test
    public void testIncreaseScore() {
        int scoreExpected = 10;
        arena.increaseScore(scoreExpected);
        assertEquals(scoreExpected, arena.getScore());
    }

    @Test
    public void testGetShip() {
        var ship = mock(Ship.class);
        arena.setShip(ship);
        assertSame(ship, arena.getShip());
    }

    @Test
    public void testGetAliens() {
        var alien = mock(Alien.class);
        List<Alien> aliens = List.of(alien);
        arena.setAliens(aliens);
        assertSame(aliens, arena.getAliens());
    }

    @Test
    public void testGetWalls() {
        var wall = mock(Wall.class);
        List<Wall> walls = List.of(wall);
        arena.setWalls(walls);
        assertSame(walls, arena.getWalls());
    }

    @Test
    public void testGetCoverWalls() {
        var coverWall = mock(CoverWall.class);
        List<CoverWall> coverWalls = List.of(coverWall);
        arena.setCoverWalls(coverWalls);
        assertSame(coverWalls, arena.getCoverWalls());
    }

    @Test
    public void testGetProjectiles() {
        var projectile = mock(Projectile.class);
        List<Projectile> projectiles = List.of(projectile);
        arena.setProjectiles(projectiles);
        assertSame(projectiles, arena.getProjectiles());
    }

    @Test
    public void testGetAlienShip() {
        var alienShip = mock(AlienShip.class);
        arena.setAlienShip(alienShip);
        assertSame(alienShip, arena.getAlienShip());
    }

    @Test
    public void testGetActiveCollectable() {
        var collectable = mock(Collectable.class);
        arena.setActiveCollectable(collectable);
        assertSame(collectable, arena.getActiveCollectable());
    }

    @Test
    public void testGetAttackingAliens() {
        var alien1 = mock(Alien.class);
        var alien2 = mock(Alien.class);
        when(alien1.getAlienState()).thenReturn(AlienState.ATTACKING);
        when(alien2.getAlienState()).thenReturn(AlienState.PASSIVE);

        List<Alien> aliens = List.of(alien1, alien2);
        arena.setAliens(aliens);

        List<Alien> attackingAliens = arena.getAttackingAliens();

        assertEquals(List.of(alien1), attackingAliens);
    }

    @ParameterizedTest
    @MethodSource("valuesFreeArenaPosition")
    public void testFreeArenaPosition(Position position, List<Alien> aliens, List<CoverWall> coverWalls, AlienShip alienShip, boolean expected) {
        arena.setAliens(aliens);
        arena.setCoverWalls(coverWalls);
        arena.setAlienShip(alienShip);
        assertEquals(expected, arena.freeArenaPosition(position));
    }

    @Test
    public void testGetFreeArenaColumns(){
        List<Integer> expected = List.of(0, 3, 4);
        var alien1 = mock(Alien.class);
        var alien2 = mock(Alien.class);
        when(alien1.getPosition()).thenReturn(new Position(1, 1));
        when(alien2.getPosition()).thenReturn(new Position(2, 2));
        List<Alien> aliens = List.of(alien1, alien2);
        arena.setAliens(aliens);
        arena.setCoverWalls(List.of());

        List<Integer> freeColumns = arena.getFreeArenaColumns();

        assertEquals(expected, freeColumns);
    }
}