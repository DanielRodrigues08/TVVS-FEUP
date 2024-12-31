package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProjectileTest {

    private Projectile projectile;
    private AttackingElement attackingElement;

    @BeforeEach
    void setUp() {
        attackingElement = new AttackingElement(new Position(5, 5), 100, 10);
        projectile = new Projectile(new Position(10, 20), attackingElement);
    }

    @Test
    void testGetElement() {
        assertSame(attackingElement, projectile.getElement());
    }

    @Test
    void testGetPosition() {
        assertEquals(new Position(10, 20), projectile.getPosition());
    }
}