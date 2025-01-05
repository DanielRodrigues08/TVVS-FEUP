package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.ArenaModifier;
import SpaceInvaders.Model.Game.Collectables.Collectable;
import SpaceInvaders.Model.Game.Element;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Sound.Sound_Options;
import SpaceInvaders.State.GameStates;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArenaControllerTest {
    @Mock
    private Arena arena;
    @Mock
    private ShipController shipController;
    @Mock
    private AlienController alienController;
    @Mock
    private ProjectileController projectileController;
    @Mock
    private CollectableController collectableController;
    @Mock
    private AlienShipController alienShipController;
    @Mock
    private ArenaModifier arenaModifier;
    @InjectMocks
    private ArenaController controller;

    private static Stream<Arguments> valuesTestCollisionBetween() {
        return Stream.of(
                Arguments.of(new Position(10, 10), new Position(10, 10), true),
                Arguments.of(new Position(10, 10), new Position(20, 20), false)
        );
    }

    private static Stream<Arguments> shipCollisionTestCases() {
        Ship ship = mock(Ship.class);
        when(ship.getPosition()).thenReturn(new Position(5, 5));

        Alien a1 = mock(Alien.class);
        when(a1.getPosition()).thenReturn(new Position(5, 5));
        Alien a2 = mock(Alien.class);
        when(a2.getPosition()).thenReturn(new Position(10, 10));

        return Stream.of(
                Arguments.of(ship, new ArrayList<>(), false),
                Arguments.of(ship, List.of(a1), true),
                Arguments.of(ship, List.of(a1, a2), true),
                Arguments.of(ship, List.of(a2), false)
        );
    }

    private static Stream<Arguments> alienCollidesWithCoverWallTestCases() {
        Alien a1 = mock(Alien.class);
        when(a1.getPosition()).thenReturn(new Position(5, 5));

        CoverWall c1 = mock(CoverWall.class);
        when(c1.getPosition()).thenReturn(new Position(5, 5));
        CoverWall c2 = mock(CoverWall.class);
        when(c2.getPosition()).thenReturn(new Position(10, 10));

        return Stream.of(
                Arguments.of(new ArrayList<>(), new ArrayList<>(), false),
                Arguments.of(List.of(a1), new ArrayList<>(), false),
                Arguments.of(new ArrayList<>(), List.of(c1), false),
                Arguments.of(List.of(a1), List.of(c1), true),
                Arguments.of(List.of(a1), List.of(c1, c2), true),
                Arguments.of(List.of(a1), List.of(c2), false)
        );
    }

    public static Stream<Arguments> alienReachesBottomArenaWallTestCases() {
        Alien a1 = mock(Alien.class);
        when(a1.getPosition()).thenReturn(new Position(5, 99));

        Alien a2 = mock(Alien.class);
        when(a2.getPosition()).thenReturn(new Position(5, 98));

        return Stream.of(
                Arguments.of(new ArrayList<>(), false),
                Arguments.of(List.of(a2), false),
                Arguments.of(List.of(a1, a2), true)
        );
    }

    private static Stream<Arguments> projectileWallCollisionTestCases() {
        Wall wall1 = mock(Wall.class);
        when(wall1.getPosition()).thenReturn(new Position(5, 5));

        Wall wall2 = mock(Wall.class);
        when(wall2.getPosition()).thenReturn(new Position(10, 10));

        Projectile proj1 = mock(Projectile.class);
        when(proj1.getPosition()).thenReturn(new Position(5, 5));

        Projectile proj2 = mock(Projectile.class);
        when(proj2.getPosition()).thenReturn(new Position(10, 10));

        Projectile proj3 = mock(Projectile.class);
        when(proj3.getPosition()).thenReturn(new Position(15, 15));


        return Stream.of(
                Arguments.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                Arguments.of(new ArrayList<>(List.of(wall1)),
                        new ArrayList<>(List.of(proj1)),
                        new ArrayList<>(List.of(proj1))),
                Arguments.of(new ArrayList<>(List.of(wall1)),
                        new ArrayList<>(Arrays.asList(proj1, proj3)),
                        new ArrayList<>(List.of(proj1))),
                Arguments.of(new ArrayList<>(Arrays.asList(wall1, wall2)),
                        new ArrayList<>(List.of(proj1)),
                        new ArrayList<>(List.of(proj1))),
                Arguments.of(new ArrayList<>(Arrays.asList(wall1, wall2)),
                        new ArrayList<>(Arrays.asList(proj1, proj2)),
                        new ArrayList<>(Arrays.asList(proj1, proj2))),
                Arguments.of(new ArrayList<>(List.of(wall1)),
                        new ArrayList<>(List.of(proj3)),
                        new ArrayList<>())
        );
    }

    private static Stream<Arguments> projectileCollisionsWithShipTestCases() {
        Ship ship = mock(Ship.class);
        when(ship.getPosition()).thenReturn(new Position(5, 5));

        AttackingElement attackingElement = mock(AttackingElement.class);
        when(attackingElement.getDamagePerShot()).thenReturn(10);

        Projectile proj1 = mock(Projectile.class);
        when(proj1.getPosition()).thenReturn(new Position(5, 5));
        when(proj1.getElement()).thenReturn(attackingElement);

        Projectile proj2 = mock(Projectile.class);
        when(proj2.getPosition()).thenReturn(new Position(10, 10));
        when(proj2.getElement()).thenReturn(attackingElement);

        Projectile proj3 = mock(Projectile.class);
        when(proj3.getPosition()).thenReturn(new Position(5, 5));
        when(proj3.getElement()).thenReturn(attackingElement);

        return Stream.of(
                // Empty projectiles list
                Arguments.of(ship, new ArrayList<>(), List.of()),
                // No collision
                Arguments.of(ship, List.of(proj2), List.of()),
                // Single projectile hits ship
                Arguments.of(ship, List.of(proj1), List.of(proj1)),
                // Multiple projectiles, one collision
                Arguments.of(ship, Arrays.asList(proj1, proj2), List.of(proj1)),
                // Multiple projectiles, multiple collisions
                Arguments.of(ship, Arrays.asList(proj1, proj2, proj3), Arrays.asList(proj1, proj3))
        );
    }

    private static Stream<Arguments> projectileAlienCollisionTestCases() {
        // Create mocks with specific positions
        Alien alien1 = mock(Alien.class);
        when(alien1.getPosition()).thenReturn(new Position(5, 5));

        Alien alien2 = mock(Alien.class);
        when(alien2.getPosition()).thenReturn(new Position(10, 10));

        Projectile proj1 = mock(Projectile.class);
        when(proj1.getPosition()).thenReturn(new Position(5, 5));  // Will collide with alien1

        Projectile proj2 = mock(Projectile.class);
        when(proj2.getPosition()).thenReturn(new Position(10, 10));  // Will collide with alien2

        Projectile proj3 = mock(Projectile.class);
        when(proj3.getPosition()).thenReturn(new Position(15, 15));  // Won't collide

        // Set up test cases
        return Stream.of(
                // Empty case
                Arguments.of(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyMap()
                ),

                // Single collision
                Arguments.of(
                        List.of(alien1),
                        List.of(proj1),
                        Map.of(alien1, List.of(proj1))
                ),

                // Multiple collisions
                Arguments.of(
                        Arrays.asList(alien1, alien2),
                        Arrays.asList(proj1, proj2),
                        new HashMap<Alien, List<Projectile>>() {{
                            put(alien1, List.of(proj1));
                            put(alien2, List.of(proj2));
                        }}
                ),

                // No collision
                Arguments.of(
                        List.of(alien1),
                        List.of(proj3),
                        Collections.emptyMap()
                )
        );
    }

    private static Stream<Arguments> projectileCoverWallCollisionTestCases() {
        // Mock CoverWalls
        CoverWall wall1 = mock(CoverWall.class);
        when(wall1.getPosition()).thenReturn(new Position(5, 5));

        CoverWall wall2 = mock(CoverWall.class);
        when(wall2.getPosition()).thenReturn(new Position(10, 10));

        // Mock Projectiles
        AttackingElement attackingElement = mock(AttackingElement.class);
        when(attackingElement.getDamagePerShot()).thenReturn(10);

        Projectile proj1 = mock(Projectile.class);
        when(proj1.getPosition()).thenReturn(new Position(5, 5));
        when(proj1.getElement()).thenReturn(attackingElement);

        Projectile proj2 = mock(Projectile.class);
        when(proj2.getPosition()).thenReturn(new Position(10, 10));
        when(proj2.getElement()).thenReturn(attackingElement);

        return Stream.of(
                // Empty lists
                Arguments.of(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyMap()
                ),
                // Single wall, single projectile collision
                Arguments.of(
                        List.of(wall1),
                        List.of(proj1),
                        Map.of(wall1, List.of(proj1))
                ),
                // Multiple walls, multiple projectiles
                Arguments.of(
                        Arrays.asList(wall1, wall2),
                        Arrays.asList(proj1, proj2),
                        Map.of(
                                wall1, List.of(proj1),
                                wall2, List.of(proj2)
                        )
                ),
                // No collisions
                Arguments.of(
                        List.of(wall1),
                        List.of(proj2),
                        Collections.emptyMap()
                )
        );
    }

    private static Stream<Arguments> projectileAlienShipCollisionTestCases() {
        AlienShip alienShip = mock(AlienShip.class);
        when(alienShip.getPosition()).thenReturn(new Position(5, 5));

        AttackingElement attackingElement = mock(AttackingElement.class);
        when(attackingElement.getDamagePerShot()).thenReturn(10);

        Projectile hitProjectile = mock(Projectile.class);
        when(hitProjectile.getPosition()).thenReturn(new Position(5, 5));
        when(hitProjectile.getElement()).thenReturn(attackingElement);

        Projectile missProjectile = mock(Projectile.class);
        when(missProjectile.getPosition()).thenReturn(new Position(10, 10));
        when(missProjectile.getElement()).thenReturn(attackingElement);

        return Stream.of(
                // Null alien ship
                Arguments.of(null, List.of(hitProjectile), List.of()),

                // Empty projectiles
                Arguments.of(alienShip, Collections.emptyList(), List.of()),

                // Single projectile hit
                Arguments.of(alienShip, List.of(hitProjectile), List.of(hitProjectile)),

                // Single projectile miss
                Arguments.of(alienShip, List.of(missProjectile), List.of()),

                // Multiple projectiles, one hit
                Arguments.of(alienShip, Arrays.asList(hitProjectile, missProjectile), List.of(hitProjectile))

                // Multiple projectiles, multiple hits
                //Arguments.of(alienShip, Arrays.asList(hitProjectile, hitProjectile), Arrays.asList(hitProjectile, hitProjectile))
        );
    }

    private static Stream<Arguments> shipCollectableCollisionTestCases() {
        Ship ship = mock(Ship.class);
        when(ship.getPosition()).thenReturn(new Position(5, 5));

        Collectable collidingCollectable = mock(Collectable.class);
        when(collidingCollectable.getPosition()).thenReturn(new Position(5, 5));

        Collectable nonCollidingCollectable = mock(Collectable.class);
        when(nonCollidingCollectable.getPosition()).thenReturn(new Position(10, 10));

        return Stream.of(
                // Null collectable
                Arguments.of(ship, null, 0),

                // No collision
                Arguments.of(ship, nonCollidingCollectable, 0),

                // Collision occurs
                Arguments.of(ship, collidingCollectable, 1)
        );
    }

    private static Stream<Arguments> collectableWallCollisionTestCases() {
        Wall wall = mock(Wall.class);
        when(wall.getPosition()).thenReturn(new Position(5, 5));
        Wall wall2 = mock(Wall.class);
        when(wall2.getPosition()).thenReturn(new Position(10, 10));
        Collectable collectable = mock(Collectable.class);
        when(collectable.getPosition()).thenReturn(new Position(5, 5));

        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(wall)), collectable, 1),
                Arguments.of(new ArrayList<>(List.of(wall2)), collectable, 0),
                Arguments.of(new ArrayList<>(List.of()), collectable, 0)
        );
    }

    private static Stream<Arguments> removeDestroyedCoverWalls() {
        CoverWall c1 = mock(CoverWall.class);
        CoverWall c2 = mock(CoverWall.class);
        CoverWall c3 = mock(CoverWall.class);

        when(c1.isDestroyed()).thenReturn(true);
        when(c2.isDestroyed()).thenReturn(false);
        when(c3.isDestroyed()).thenReturn(true);

        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(c1, c2, c3)), new ArrayList<>(List.of(c1, c3))),
                Arguments.of(new ArrayList<>(List.of(c2)), new ArrayList<>(List.of())),
                Arguments.of(new ArrayList<>(List.of()), new ArrayList<>(List.of()))
        );
    }

    private static Stream<Arguments> stepMethodTestCases() {
        KeyStroke escapeKey = mock(KeyStroke.class);
        when(escapeKey.getKeyType()).thenReturn(KeyType.Escape);

        KeyStroke normalKey = mock(KeyStroke.class);
        when(normalKey.getKeyType()).thenReturn(KeyType.Character);

        return Stream.of(
                // Format: time, needToUpdateTimers, expectedNeedToUpdateTimers, key,
                //         isShipDestroyed, shipCollidesWithAlien, alienCollidesWithCoverWall,
                //         alienReachesBottomArenaWall, aliensEmpty, expectedState, numSetTimers

                // Initial timer setup
                Arguments.of(1000L, true, false, null,
                        false, false, false, false, false,
                        null, 1, 0),

                // Escape key pressed
                Arguments.of(1000L, false, true, escapeKey,
                        false, false, false, false, false,
                        GameStates.PAUSE, 0, 1),

                // Game over - ship destroyed
                Arguments.of(1000L, false, false, normalKey,
                        true, false, false, false, false,
                        GameStates.GAME_OVER, 0, 1),

                // Game over - ship collides with alien
                Arguments.of(1000L, false, false, normalKey,
                        false, true, false, false, false,
                        GameStates.GAME_OVER, 0, 1),

                // Game over - alien hits cover wall
                Arguments.of(1000L, false, false, normalKey,
                        false, false, true, false, false,
                        GameStates.GAME_OVER, 0, 1),

                // Game over - alien reaches bottom
                Arguments.of(1000L, false, false, normalKey,
                        false, false, false, true, false,
                        GameStates.GAME_OVER, 0, 1),

                // New round - all aliens destroyed
                Arguments.of(1000L, false, false, normalKey,
                        false, false, false, false, true,
                        GameStates.NEW_GAME_ROUND, 0, 1),

                // Normal gameplay
                Arguments.of(1000L, false, false, normalKey,
                        false, false, false, false, false,
                        null, 0, 0)
        );
    }

    @BeforeEach
    void setUp() {
        controller = new ArenaController(arena);
        Field arenaModifierField = assertDoesNotThrow(() -> GameController.class.getDeclaredField("arenaModifier"));
        arenaModifierField.setAccessible(true);
        assertDoesNotThrow(() -> arenaModifierField.set(controller, arenaModifier));

        controller.setShipController(shipController);
        controller.setAlienController(alienController);
        controller.setProjectileController(projectileController);
        controller.setCollectableController(collectableController);
        controller.setAlienShipController(alienShipController);
        controller.setArenaModifier(arenaModifier);
    }

    @Test
    void testConstruction() {
        assertNotNull(controller);
        assertEquals(arena, controller.getModel());
    }

    @Test
    void testSetTimers() {
        int pauseGameTime = 10;
        int init = 10;
        int delta = 1000;
        int expected = (init + delta) - pauseGameTime;

        controller.setPauseGameTime(pauseGameTime);

        ShipController realShipController = new ShipController(arena);
        AlienController realAlienController = new AlienController(arena);
        CollectableController realCollectableController = new CollectableController(arena);
        AlienShipController realAlienShipController = new AlienShipController(arena);

        realShipController.setMovementTime(init);
        realShipController.setShootingTime(init);
        realAlienController.setLastMovementTime(init);
        realAlienController.setLastShotTime(init);
        realCollectableController.setGenerateCollectableTime(init);
        realAlienShipController.setLastAppearance(init);
        realAlienShipController.setLastMovementTime(init);

        controller.setShipController(realShipController);
        controller.setAlienController(realAlienController);
        controller.setCollectableController(realCollectableController);
        controller.setAlienShipController(realAlienShipController);

        controller.setTimers(delta);

        assertEquals(expected, realShipController.getMovementTime());
        assertEquals(expected, realShipController.getShootingTime());
        assertEquals(expected, realAlienController.getLastMovementTime());
        assertEquals(expected, realAlienController.getLastShotTime());
        assertEquals(expected, realCollectableController.getGenerateCollectableTime());
        assertEquals(expected, realAlienShipController.getLastAppearance());
        assertEquals(expected, realAlienShipController.getLastMovementTime());
    }

    @ParameterizedTest
    @MethodSource("valuesTestCollisionBetween")
    void testCollisionBetween(Position p1, Position p2, boolean expected) {
        Element e1 = mock(Element.class);
        Element e2 = mock(Element.class);

        when(e1.getPosition()).thenReturn(p1);
        when(e2.getPosition()).thenReturn(p2);

        boolean result = controller.collisionBetween(e1, e2);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("shipCollisionTestCases")
    void testShipCollidesWithAlien(Ship ship,
                                   List<Alien> aliens, boolean expected) {
        when(arena.getShip()).thenReturn(ship);
        when(arena.getAliens()).thenReturn(aliens);

        assertEquals(expected, controller.shipCollidesWithAlien());
    }

    @ParameterizedTest
    @MethodSource("alienCollidesWithCoverWallTestCases")
    void testAlienCollidesWithCoverWall(List<Alien> aliens, List<CoverWall> coverWalls, boolean expected) {
        when(arena.getAliens()).thenReturn(aliens);
        when(arena.getCoverWalls()).thenReturn(coverWalls);

        assertEquals(expected, controller.alienCollidesWithCoverWall());
    }

    @ParameterizedTest
    @MethodSource("alienReachesBottomArenaWallTestCases")
    void testAlienReachesBottomArenaWall(List<Alien> aliens, boolean expected) {
        when(arena.getAliens()).thenReturn(aliens);
        when(arena.getHeight()).thenReturn(100);

        assertEquals(expected, controller.alienReachesBottomArenaWall());
    }

    @ParameterizedTest
    @MethodSource("projectileWallCollisionTestCases")
    void testProjectileCollisionsWithWalls(List<Wall> walls, List<Projectile> projectiles, List<Projectile> expectedRemovedProjectiles) {
        when(arena.getWalls()).thenReturn(walls);
        when(arena.getProjectiles()).thenReturn(projectiles);

        controller.projectileCollisionsWithWalls();

        for (Projectile projectile : expectedRemovedProjectiles) {
            verify(arenaModifier).removeProjectile(projectile);
        }
        verifyNoMoreInteractions(arenaModifier);
    }

    @ParameterizedTest
    @MethodSource("projectileCollisionsWithShipTestCases")
    void testProjectileCollisionsWithShip(Ship ship, List<Projectile> projectiles, List<Projectile> expectedCollisions) {
        when(arena.getProjectiles()).thenReturn(projectiles);
        when(arena.getShip()).thenReturn(ship);

        controller.projectileCollisionsWithShip();

        for (Projectile projectile : expectedCollisions) {
            verify(shipController).hitByProjectile(projectile);
            verify(arenaModifier).removeProjectile(projectile);
        }
        verifyNoMoreInteractions(arenaModifier);
        verifyNoMoreInteractions(shipController);
    }

    @ParameterizedTest
    @MethodSource("projectileAlienCollisionTestCases")
    void testProjectileCollisionsWithAliens(List<Alien> aliens,
                                            List<Projectile> projectiles,
                                            Map<Alien, List<Projectile>> expectedCollisions) {
        List<Projectile> mutableProjectiles = new ArrayList<>(projectiles);
        List<Alien> mutableAliens = new ArrayList<>(aliens);
        when(arena.getProjectiles()).thenReturn(mutableProjectiles);
        when(arena.getAliens()).thenReturn(mutableAliens);


        controller.projectileCollisionsWithAliens();

        expectedCollisions.forEach((alien, hitProjectiles) -> hitProjectiles.forEach(projectile -> {
            verify(alienController).hitByProjectile(alien, projectile);
            verify(arenaModifier).removeProjectile(projectile);
        }));
    }

    @ParameterizedTest
    @MethodSource("projectileCoverWallCollisionTestCases")
    void testProjectileCollisionsWithCoverWalls(List<CoverWall> walls,
                                                List<Projectile> projectiles,
                                                Map<CoverWall, List<Projectile>> expectedCollisions) {
        when(arena.getProjectiles()).thenReturn(projectiles);
        when(arena.getCoverWalls()).thenReturn(walls);

        var controllerSpy = spy(controller);

        controllerSpy.projectileCollisionsWithCoverWalls();

        expectedCollisions.forEach((wall, hitProjectiles) -> hitProjectiles.forEach(projectile -> {
            verify(arenaModifier).removeProjectile(projectile);
            verify(controllerSpy).coverWallHitByProjectile(wall, projectile);
        }));
        verifyNoMoreInteractions(arenaModifier);
    }

    @ParameterizedTest
    @MethodSource("projectileAlienShipCollisionTestCases")
    void testProjectileCollisionWithAlienShip(
            AlienShip alienShip,
            List<Projectile> projectiles,
            List<Projectile> expectedCollisions) {

        when(arena.getProjectiles()).thenReturn(projectiles);
        when(arena.getAlienShip()).thenReturn(alienShip);


        controller.projectileCollisionWithAlienShip();

        for (Projectile projectile : expectedCollisions) {
            verify(alienShipController).hitByProjectile(alienShip, projectile);
            verify(arenaModifier).removeProjectile(projectile);
        }
        verifyNoMoreInteractions(alienShipController, arenaModifier);
    }

    @ParameterizedTest
    @MethodSource("shipCollectableCollisionTestCases")
    void testShipCollisionsWithCollectables(
            Ship ship,
            Collectable collectable,
            int expectCollision) {

        when(arena.getShip()).thenReturn(ship);
        when(arena.getActiveCollectable()).thenReturn(collectable);

        SoundManager soundManager = mock(SoundManager.class);
        MockedStatic<SoundManager> mockedStatic = mockStatic(SoundManager.class);
        mockedStatic.when(SoundManager::getInstance).thenReturn(soundManager);

        controller.shipCollisionsWithCollectables();

        if (collectable != null) {
            verify(collectable, times(expectCollision)).execute();
        }
        verify(arenaModifier, times(expectCollision)).removeActiveCollectable();
        verify(soundManager, times(expectCollision)).playSound(Sound_Options.COLLECTABLE);

        mockedStatic.close();
    }

    @ParameterizedTest
    @MethodSource("collectableWallCollisionTestCases")
    void testCollectableCollisionsWithWalls(List<Wall> walls, Collectable collectable, int numCollision) {
        when(arena.getWalls()).thenReturn(walls);
        when(arena.getActiveCollectable()).thenReturn(collectable);

        controller.collectableCollisionsWithWalls();

        verify(arena, times(numCollision)).setActiveCollectable(null);
    }

    @ParameterizedTest
    @MethodSource("removeDestroyedCoverWalls")
    void testRemoveDestroyedCoverWalls(List<CoverWall> coverWalls, List<CoverWall> coverWallsRemoved) {
        when(arena.getCoverWalls()).thenReturn(coverWalls);

        controller.removeDestroyedCoverWalls();

        coverWallsRemoved.forEach(coverWall -> verify(arenaModifier).removeCoverWall(coverWall));
        verifyNoMoreInteractions(arenaModifier);
    }

    @Test
    void testCheckCollisions() {
        ArenaController controllerSpy = spy(controller);

        controllerSpy.checkCollisions();

        verify(controllerSpy).projectileCollisionsWithWalls();
        verify(controllerSpy).projectileCollisionsWithShip();
        verify(controllerSpy).projectileCollisionsWithAliens();
        verify(controllerSpy).shipCollisionsWithCollectables();
        verify(controllerSpy).collectableCollisionsWithWalls();
        verify(controllerSpy).projectileCollisionsWithCoverWalls();
        verify(controllerSpy).projectileCollisionWithAlienShip();
    }

    @ParameterizedTest
    @MethodSource("stepMethodTestCases")
    void testStep(long time, boolean needToUpdateTimers, boolean expectedNeedToUpdateTimers, KeyStroke key, boolean isShipDestroyed, boolean shipCollidesWithAlien, boolean alienCollidesWithCoverWall, boolean alienReachesBottomArenaWall, boolean aliensEmpty, GameStates expectedState, int numSetTimers, int numExpectedState) {
        var controllerSpy = spy(controller);

        controllerSpy.setNeedToUpdateTimers(needToUpdateTimers);

        var ship = mock(Ship.class);
        when(ship.isDestroyed()).thenReturn(isShipDestroyed);
        when(arena.getShip()).thenReturn(ship);

        doReturn(shipCollidesWithAlien).when(controllerSpy).shipCollidesWithAlien();
        doReturn(alienCollidesWithCoverWall).when(controllerSpy).alienCollidesWithCoverWall();
        doReturn(alienReachesBottomArenaWall).when(controllerSpy).alienReachesBottomArenaWall();

        var aliens = mock(List.class);
        when(aliens.isEmpty()).thenReturn(aliensEmpty);
        when(arena.getAliens()).thenReturn(aliens);

        var game = mock(Game.class);

        assertDoesNotThrow(() -> controllerSpy.step(game, key, time));

        assertEquals(expectedNeedToUpdateTimers, controllerSpy.isNeedToUpdateTimers());
        assertDoesNotThrow(() -> verify(game, times(numExpectedState)).setState(expectedState));
        verify(controllerSpy, times(numSetTimers)).setTimers(time);
        verify(controllerSpy).checkCollisions();
        verify(controllerSpy).removeDestroyedCoverWalls();
        assertDoesNotThrow(() -> verify(shipController).step(game, key, time));
        assertDoesNotThrow(() -> verify(alienController).step(game, key, time));
        assertDoesNotThrow(() -> verify(projectileController).step(game, key, time));
        assertDoesNotThrow(() -> verify(collectableController).step(game, key, time));
        assertDoesNotThrow(() -> verify(alienShipController).step(game, key, time));
    }

    @Test
    void testGetSetPause() {
        long time = 1000;
        controller.setPauseGameTime(time);
        assertEquals(time, controller.getPauseGameTime());
    }

    @Test
    public void testCoverWallHitByProjectile(){
        int expectedHealth = 10;
        CoverWall wall = new CoverWall(mock(Position.class), 20);
        Projectile projectile = mock(Projectile.class);
        when(projectile.getElement()).thenReturn(mock(AttackingElement.class));
        when(projectile.getElement().getDamagePerShot()).thenReturn(10);

        controller.coverWallHitByProjectile(wall, projectile);

        assertEquals(expectedHealth, wall.getHealth());
    }

    @Test
    public void testRemoveDestroyedElements(){
        var controllerSpy = spy(controller);

        controllerSpy.removeDestroyedElements();

        verify(controllerSpy).removeDestroyedCoverWalls();
        verify(alienController).removeDestroyedAliens();
        verify(alienShipController).removeAlienShip();
    }
}
