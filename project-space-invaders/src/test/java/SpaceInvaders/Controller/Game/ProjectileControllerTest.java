package SpaceInvaders.Controller.Game;

import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Game.RegularGameElements.AttackingElement;
import SpaceInvaders.Model.Game.RegularGameElements.Projectile;
import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProjectileControllerTest {

    private Arena arenaMock;

    public static Stream<Arguments> provideProjectiles() {
        return Stream.of(
                Arguments.of(new Position(5, 5), new Position(5, 4), Mockito.mock(Ship.class)),
                Arguments.of(new Position(5, 5), new Position(5, 6), Mockito.mock(Alien.class))
        );
    }

    @BeforeEach
    public void setUp() {
        this.arenaMock = Mockito.mock(Arena.class);
    }

    @ParameterizedTest
    @MethodSource("provideProjectiles")
    public void moveProjectileTest(Position initialPosition, Position expectedPosition, Object elementMock) {
        ProjectileController projectileController = new ProjectileController(arenaMock);
        Projectile projectile = new Projectile(initialPosition, (AttackingElement) elementMock);
        List<Projectile> projectiles = List.of(projectile);
        when(arenaMock.getProjectiles()).thenReturn(projectiles);

        projectileController.step(null, null, 0L);

        assertEquals(expectedPosition, projectile.getPosition());
    }

}
