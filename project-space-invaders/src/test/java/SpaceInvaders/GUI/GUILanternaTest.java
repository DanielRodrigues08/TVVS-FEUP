package SpaceInvaders.GUI;

import SpaceInvaders.Model.Position;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUILanternaTest {

    private GUILanterna gui;
    private Screen screen;

    @BeforeEach
    void setUp() {
        screen = Mockito.mock(Screen.class);
        gui = new GUILanterna(screen);
    }

    @Test
    void testGUILanternaValid() {
        GUILanterna guiLanterna = assertDoesNotThrow(() -> new GUILanterna(10, 10));

        Field screenField = assertDoesNotThrow(() -> GUILanterna.class.getDeclaredField("screen"));
        screenField.setAccessible(true);
        Object screen = assertDoesNotThrow(() -> screenField.get(guiLanterna));

        assertInstanceOf(Screen.class, screen);
    }

    @Test
    void testGUILanternaInvalid() {
        assertThrows(Exception.class, () -> new GUILanterna(0, 0));
    }

    @Test
    void testDrawElement() {
        var tgMock = mock(TextGraphics.class);
        when(screen.newTextGraphics()).thenReturn(tgMock);
        Position position = new Position(1, 1);

        gui.drawElement(position, 'X', "#FFFFFF");

        verify(tgMock).putString(1, 2, "X");
    }

    @Test
    void testDrawText() {
        var tgMock = mock(TextGraphics.class);
        when(screen.newTextGraphics()).thenReturn(tgMock);
        Position position = new Position(1, 1);

        gui.drawText(position, "Hello", "#FFFFFF");

        verify(tgMock).putString(1, 1, "Hello");
    }

    @Test
    void testGetNextAction() {
        KeyStroke keyStroke = mock(KeyStroke.class);
        assertDoesNotThrow(() -> when(screen.pollInput()).thenReturn(keyStroke));
        KeyStroke result = assertDoesNotThrow(() -> gui.getNextAction());
        assertNotNull(result);
        assertDoesNotThrow(() -> verify(screen).pollInput());
    }

    @Test
    void testClear() {
        gui.clear();
        verify(screen).clear();
    }

    @Test
    void testRefresh() {
        assertDoesNotThrow(() -> {
            gui.refresh();
            verify(screen).refresh();
        });
    }

    @Test
    void testClose() {
        assertDoesNotThrow(() -> {
            gui.close();
            verify(screen).close();
        });
    }
}