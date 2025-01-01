package SpaceInvaders.Model.Game;

import SpaceInvaders.Model.Game.Collectables.Collectable;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArenaModifierTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private Arena arena;
    private ArenaModifier arenaModifier;
    private Random random;

    private static Stream<Arguments> testCreateAlienShipValues() {
        return Stream.of(Arguments.of(0, new AlienShip(new Position(WIDTH - 4, 6), 50, 1000, -1)), Arguments.of(1, new AlienShip(new Position(4, 6), 50, 1000, 1)));
    }

    private static Stream<Arguments> testHasAlienInFrontValues() {
        Alien alien1 = new Alien(new Position(1, 1), 0, 0, 0, AlienState.PASSIVE, 0);
        Alien alien2 = new Alien(new Position(1, 2), 0, 0, 0, AlienState.PASSIVE, 0);
        Alien alien3 = new Alien(new Position(1, 3), 0, 0, 0, AlienState.PASSIVE, 0);
        Alien alien4 = new Alien(new Position(2, 1), 0, 0, 0, AlienState.PASSIVE, 0);

        return Stream.of(
                Arguments.of(List.of(alien1, alien2, alien3), alien1, alien2, true),
                Arguments.of(List.of(alien1, alien2, alien3), alien1, alien3, true),
                Arguments.of(List.of(alien1, alien2, alien3), alien2, alien1, true),
                Arguments.of(List.of(alien1, alien2, alien3), alien3, alien1, false),
                Arguments.of(List.of(alien1, alien4), alien1, alien4, false),
                Arguments.of(List.of(alien1, alien4), alien4, alien1, false)
        );
    }

    private static Stream<Arguments> provideAliensForRemoveAlien() {
        Alien alien1 = new Alien(new Position(1, 1), 0, 0, 0, AlienState.PASSIVE, 0);
        Alien alien2 = new Alien(new Position(1, 2), 0, 0, 0, AlienState.PASSIVE, 0);
        Alien alien3 = new Alien(new Position(1, 3), 0, 0, 0, AlienState.PASSIVE, 0);
        Alien alien4 = new Alien(new Position(2, 1), 0, 0, 0, AlienState.PASSIVE, 0);

        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(alien1, alien2, alien3)), alien1, List.of(alien2, alien3)),
                Arguments.of(new ArrayList<>(List.of(alien1, alien2, alien3)), alien2, List.of(alien1, alien3)),
                Arguments.of(new ArrayList<>(List.of(alien1, alien2, alien3)), alien3, List.of(alien1, alien2)),
                Arguments.of(new ArrayList<>(List.of(alien1, alien4)), alien1, List.of(alien4)),
                Arguments.of(new ArrayList<>(List.of(alien1, alien4)), alien4, List.of(alien1)),
                Arguments.of(new ArrayList<>(), alien1, new ArrayList<>())
        );
    }

    @BeforeEach
    void setUp() {
        arena = spy(new Arena(WIDTH, HEIGHT));
        arenaModifier = new ArenaModifier(arena);
        random = mock(Random.class);
        arenaModifier.setRandom(random);
    }

    @Test
    void testResetShipMode() {
        ShipMode expectedShipMode = ShipMode.NORMAL_MODE;
        Ship ship = new Ship(null, 0, 0);
        ship.setShipMode(ShipMode.MACHINE_GUN_MODE);
        arena.setShip(ship);

        arenaModifier.resetShipMode();

        assertEquals(expectedShipMode, ship.getShipMode());
    }

    @Test
    void testResetAliensMode() {
        AlienMode expectedShipMode = AlienMode.NORMAL_MODE;
        Alien alien1 = new Alien(null, 0, 0, 0, null, 0);
        alien1.setAlienMode(AlienMode.SCORE_2X);
        Alien alien2 = new Alien(null, 0, 0, 0, null, 0);
        alien2.setAlienMode(AlienMode.SCORE_3X);
        List<Alien> aliens = List.of(alien1, alien2);
        arena.setAliens(aliens);

        arenaModifier.resetAliensMode();

        assertEquals(expectedShipMode, alien1.getAlienMode());
        assertEquals(expectedShipMode, alien2.getAlienMode());
    }

    @Test
    void testCreateCollectableAffectingShip() {
        Position expectedPosition = new Position(1, 1);

        doReturn(List.of(1)).when(arena).getFreeArenaColumns();
        when(random.nextInt(2)).thenReturn(0);
        arena.setShip(mock(Ship.class));
        arena.setAliens(List.of(mock(Alien.class)));

        arenaModifier.createCollectable();

        assertEquals(expectedPosition, arena.getActiveCollectable().getPosition());
        assertInstanceOf(Collectable.class, arena.getActiveCollectable());
        assertInstanceOf(Ship.class, arena.getActiveCollectable().getAttackingElement());
    }

    @Test
    void testCreateCollectableAffectingAlien() {
        Position expectedPosition = new Position(1, 1);

        doReturn(List.of(1)).when(arena).getFreeArenaColumns();
        when(random.nextInt(2)).thenReturn(1);
        arena.setShip(mock(Ship.class));
        arena.setAliens(List.of(mock(Alien.class)));

        arenaModifier.createCollectable();

        assertEquals(expectedPosition, arena.getActiveCollectable().getPosition());
        assertInstanceOf(Collectable.class, arena.getActiveCollectable());
        List<Alien> aliens = (List<Alien>) arena.getActiveCollectable().getAttackingElement();
        assertInstanceOf(Alien.class, aliens.getFirst());
    }

    @ParameterizedTest
    @MethodSource("testCreateAlienShipValues")
    void testCreateAlienShip(int movement, AlienShip expectedAlienShip) {
        when(random.nextInt(anyInt())).thenReturn(movement);
        arenaModifier.createAlienShip();

        assertEquals(expectedAlienShip, arena.getAlienShip());
    }

    @ParameterizedTest
    @MethodSource("testHasAlienInFrontValues")
    void testHasAlienInFront(List<Alien> aliens, Alien alien, Alien excludedAlien, boolean expected) {
        when(arena.getAliens()).thenReturn(aliens);

        boolean result = arenaModifier.hasAlienInFront(alien, excludedAlien);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("provideAliensForRemoveAlien")
    void testRemoveAlien(List<Alien> initialAliens, Alien alienToRemove, List<Alien> expectedAliens) {
        when(arena.getAliens()).thenReturn(initialAliens);

        arenaModifier.removeAlien(alienToRemove);

        assertFalse(initialAliens.contains(alienToRemove));
        assertEquals(expectedAliens, initialAliens);
    }

    @Test
    void testRemoveCoverWall() {
        CoverWall coverWall = mock(CoverWall.class);
        List<CoverWall> coverWalls = new ArrayList<>();
        coverWalls.add(coverWall);
        when(arena.getCoverWalls()).thenReturn(coverWalls);

        arenaModifier.removeCoverWall(coverWall);

        assertFalse(coverWalls.contains(coverWall));
    }

    @Test
    void testRemoveActiveCollectable() {
        Collectable collectable = mock(Collectable.class);
        arena.setActiveCollectable(collectable);

        arenaModifier.removeActiveCollectable();

        assertNull(arena.getActiveCollectable());
    }

    @Test
    void testAddProjectile() {
        Projectile projectile = mock(Projectile.class);
        List<Projectile> projectiles = new ArrayList<>();
        when(arena.getProjectiles()).thenReturn(projectiles);

        arenaModifier.addProjectile(projectile);

        assertTrue(projectiles.contains(projectile));
    }

    @Test
    void testRemoveProjectile() {
        Projectile projectile = mock(Projectile.class);
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(projectile);
        when(arena.getProjectiles()).thenReturn(projectiles);

        arenaModifier.removeProjectile(projectile);

        assertFalse(projectiles.contains(projectile));
    }

    @Test
    void testRemoveAlienShip() {
        AlienShip alienShip = mock(AlienShip.class);
        arena.setAlienShip(alienShip);

        arenaModifier.removeAlienShip();

        assertNull(arena.getAlienShip());
    }
}