package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu() {
            {
                options = Arrays.asList("Option1", "Option2", "Option3");
            }
        };
    }

    @Test
    void testNextOption() {
        assertEquals(0, menu.selected);
        menu.nextOption();
        assertEquals(1, menu.selected);
        menu.nextOption();
        assertEquals(2, menu.selected);
        menu.nextOption();
        assertEquals(0, menu.selected);
    }

    @Test
    void testPreviousOption() {
        assertEquals(0, menu.selected);
        menu.previousOption();
        assertEquals(2, menu.selected);
        menu.previousOption();
        assertEquals(1, menu.selected);
        menu.previousOption();
        assertEquals(0, menu.selected);
    }

    @Test
    void testGetOption() {
        assertEquals("Option1", menu.getOption(0));
        assertEquals("Option2", menu.getOption(1));
        assertEquals("Option3", menu.getOption(2));
    }

    @Test
    void testIsSelected() {
        assertTrue(menu.isSelected(0));
        menu.nextOption();
        assertTrue(menu.isSelected(1));
        menu.nextOption();
        assertTrue(menu.isSelected(2));
        menu.nextOption();
        assertTrue(menu.isSelected(0));
    }

    @Test
    void testIsSelectedFalse() {
        assertFalse(menu.isSelected(1));
    }

    @Test
    void testGetNumberOptions() {
        assertEquals(3, menu.getNumberOptions());
    }
}