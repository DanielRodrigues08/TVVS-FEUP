package SpaceInvaders.Gui;

import SpaceInvaders.GUI.GUILanterna;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GuiTest {
    @Test
    void createGUILanternaNegative(){
        assertThrows(IllegalArgumentException.class, () -> new GUILanterna(-1, -1));
    }

    @Test
    void createGUILanternaZero(){
        assertThrows(IllegalArgumentException.class, () -> new GUILanterna(0, 0));
    }


}
