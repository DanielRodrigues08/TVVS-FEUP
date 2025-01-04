package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.ArenaModifier;
import SpaceInvaders.Model.Game.Collectables.Collectable;
import SpaceInvaders.Model.Game.Element;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Sound.Sound_Options;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
                Arguments.of(new ArrayList<>(Arrays.asList(wall1)),
                        new ArrayList<>(Arrays.asList(proj1)),
                        new ArrayList<>(Arrays.asList(proj1))),
                Arguments.of(new ArrayList<>(Arrays.asList(wall1)),
                        new ArrayList<>(Arrays.asList(proj1, proj3)),
                        new ArrayList<>(Arrays.asList(proj1))),
                Arguments.of(new ArrayList<>(Arrays.asList(wall1, wall2)),
                        new ArrayList<>(Arrays.asList(proj1)),
                        new ArrayList<>(Arrays.asList(proj1))),
                Arguments.of(new ArrayList<>(Arrays.asList(wall1, wall2)),
                        new ArrayList<>(Arrays.asList(proj1, proj2)),
                        new ArrayList<>(Arrays.asList(proj1, proj2))),
                Arguments.of(new ArrayList<>(Arrays.asList(wall1)),
                        new ArrayList<>(Arrays.asList(proj3)),
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
                Arguments.of(ship, null, false),

                // No collision
                Arguments.of(ship, nonCollidingCollectable, false),

                // Collision occurs
                Arguments.of(ship, collidingCollectable, true)
        );
    }

    @BeforeEach
    void setUp() {
        controller = new ArenaController(arena);
    }

    @Test
    void testConstruction() {
        assertNotNull(controller);
        assertEquals(arena, controller.getModel());
    }

    @Test
    void testSetTimers() {
        int init = 1;
        int delta = 1000;
        int expected = init + delta;


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
    void testProjectileCollisionsWithShip(Ship ship, List<Projectile> projectiles, List<Projectile> expectedCollisions) throws NoSuchFieldException, IllegalAccessException {
        when(arena.getProjectiles()).thenReturn(projectiles);
        when(arena.getShip()).thenReturn(ship);

        ArenaModifier arenaModifier = mock(ArenaModifier.class);
        Field arenaModifierField = GameController.class.getDeclaredField("arenaModifier");
        arenaModifierField.setAccessible(true);
        arenaModifierField.set(controller, arenaModifier);

        ShipController shipController = mock(ShipController.class);
        Field shipControllerField = ArenaController.class.getDeclaredField("shipController");
        shipControllerField.setAccessible(true);
        shipControllerField.set(controller, shipController);

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
        // Mock Arena
        List<Projectile> mutableProjectiles = new ArrayList<>(projectiles);
        List<Alien> mutableAliens = new ArrayList<>(aliens);
        when(arena.getProjectiles()).thenReturn(mutableProjectiles);
        when(arena.getAliens()).thenReturn(mutableAliens);


        controller.setAlienController(alienController);
        controller.setArenaModifier(arenaModifier);

        // Execute
        controller.projectileCollisionsWithAliens();

        // Verify
        expectedCollisions.forEach((alien, hitProjectiles) -> {
            hitProjectiles.forEach(projectile -> {
                verify(alienController).hitByProjectile(alien, projectile);
                verify(arenaModifier).removeProjectile(projectile);
            });
        });
    }

    @ParameterizedTest
    @MethodSource("projectileCoverWallCollisionTestCases")
    void testProjectileCollisionsWithCoverWalls(List<CoverWall> walls,
                                                List<Projectile> projectiles,
                                                Map<CoverWall, List<Projectile>> expectedCollisions) throws NoSuchFieldException, IllegalAccessException {
        when(arena.getProjectiles()).thenReturn(projectiles);
        when(arena.getCoverWalls()).thenReturn(walls);


        ArenaModifier arenaModifier = mock(ArenaModifier.class);
        Field arenaModifierField = GameController.class.getDeclaredField("arenaModifier");
        arenaModifierField.setAccessible(true);
        arenaModifierField.set(controller, arenaModifier);

        controller.projectileCollisionsWithCoverWalls();

        expectedCollisions.forEach((wall, hitProjectiles) -> {
            hitProjectiles.forEach(projectile -> {
                verify(arenaModifier).removeProjectile(projectile);
            });
        });
        verifyNoMoreInteractions(arenaModifier);
    }

    @ParameterizedTest
    @MethodSource("projectileAlienShipCollisionTestCases")
    void testProjectileCollisionWithAlienShip(
            AlienShip alienShip,
            List<Projectile> projectiles,
            List<Projectile> expectedCollisions) throws NoSuchFieldException, IllegalAccessException {

        when(arena.getProjectiles()).thenReturn(projectiles);
        when(arena.getAlienShip()).thenReturn(alienShip);


        ArenaModifier arenaModifier = mock(ArenaModifier.class);
        Field arenaModifierField = GameController.class.getDeclaredField("arenaModifier");
        arenaModifierField.setAccessible(true);
        arenaModifierField.set(controller, arenaModifier);

        AlienShipController alienShipController = mock(AlienShipController.class);
        Field alienShipControllerField = ArenaController.class.getDeclaredField("alienShipController");
        alienShipControllerField.setAccessible(true);
        alienShipControllerField.set(controller, alienShipController);

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
            boolean expectCollision) throws NoSuchFieldException, IllegalAccessException {

        when(arena.getShip()).thenReturn(ship);
        when(arena.getActiveCollectable()).thenReturn(collectable);

        ArenaController controller = new ArenaController(arena);

        ArenaModifier arenaModifier = mock(ArenaModifier.class);
        Field arenaModifierField = GameController.class.getDeclaredField("arenaModifier");
        arenaModifierField.setAccessible(true);
        arenaModifierField.set(controller, arenaModifier);

        SoundManager soundManager = mock(SoundManager.class);
        try (MockedStatic<SoundManager> mockedStatic = mockStatic(SoundManager.class)) {
            mockedStatic.when(SoundManager::getInstance).thenReturn(soundManager);

            // Execute
            controller.shipCollisionsWithCollectables();

            // Verify
            if (expectCollision) {
                verify(collectable).execute();
                verify(arenaModifier).removeActiveCollectable();
                verify(soundManager).playSound(Sound_Options.COLLECTABLE);
            } else {
                verifyNoInteractions(arenaModifier, soundManager);
                if (collectable != null) {
                    verify(collectable, never()).execute();
                }
            }
        }
    }


}