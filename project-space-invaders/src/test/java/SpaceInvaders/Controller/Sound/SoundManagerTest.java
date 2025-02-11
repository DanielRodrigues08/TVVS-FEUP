package SpaceInvaders.Controller.Sound;

import SpaceInvaders.Model.Sound.Sound;
import SpaceInvaders.Model.Sound.Sound_Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SoundManagerTest {

    private MockedStatic<SoundManager> mockedStatic;
    private SoundManager mockSoundManager;
    private Sound laser;
    private Sound dyingSound;
    private Sound switchOption;
    private Sound backgroundMusic;
    private Sound collectableSound;
    private Sound alienShipLowPitch;
    private Sound alienShipHighPitch;

    @BeforeEach
    public void setUp() {
        // Create mock instances
        laser = mock(Sound.class);
        dyingSound = mock(Sound.class);
        switchOption = mock(Sound.class);
        backgroundMusic = mock(Sound.class);
        collectableSound = mock(Sound.class);
        alienShipLowPitch = mock(Sound.class);
        alienShipHighPitch = mock(Sound.class);

        // Create and configure SoundManager mock
        mockSoundManager = mock(SoundManager.class);
        mockedStatic = mockStatic(SoundManager.class);
        mockedStatic.when(SoundManager::getInstance).thenReturn(mockSoundManager);

        // Configure mock behavior for playSound
        doCallRealMethod().when(mockSoundManager).playSound(any());
        doCallRealMethod().when(mockSoundManager).stopSound(any());
        doCallRealMethod().when(mockSoundManager).resumePlayingMusic();
        doCallRealMethod().when(mockSoundManager).resumePlayingAlienShipSound();
        doCallRealMethod().when(mockSoundManager).stopAllSounds();
        doCallRealMethod().when(mockSoundManager).setAlienShipHighPitch(any());
        doCallRealMethod().when(mockSoundManager).setAlienShipLowPitch(any());
        doCallRealMethod().when(mockSoundManager).setBackgroundMusic(any());
        doCallRealMethod().when(mockSoundManager).setCollectableSound(any());
        doCallRealMethod().when(mockSoundManager).setDyingSound(any());
        doCallRealMethod().when(mockSoundManager).setLaser(any());
        doCallRealMethod().when(mockSoundManager).setSwitchOption(any());

        // Set the mocked sounds
        mockSoundManager.setAlienShipHighPitch(alienShipHighPitch);
        mockSoundManager.setAlienShipLowPitch(alienShipLowPitch);
        mockSoundManager.setBackgroundMusic(backgroundMusic);
        mockSoundManager.setCollectableSound(collectableSound);
        mockSoundManager.setDyingSound(dyingSound);
        mockSoundManager.setLaser(laser);
        mockSoundManager.setSwitchOption(switchOption);
    }


    @ParameterizedTest
    @CsvSource({
            "MUSIC, 1, 0, 0, 0, 0, 0, 0",
            "LASER, 0, 1, 0, 0, 0, 0, 0",
            "MENU_SWITCH, 0, 0, 1, 0, 0, 0, 0",
            "DESTRUCTION, 0, 0, 0, 1, 0, 0, 0",
            "COLLECTABLE, 0, 0, 0, 0, 1, 0, 0",
            "ALIEN_SHIP_LOW, 0, 0, 0, 0, 0, 1, 0",
            "ALIEN_SHIP_HIGH, 0, 0, 0, 0, 0, 0, 1"
    })
    public void testPlaySound(Sound_Options soundOption, int numMusic, int numLaser, int numSwitch, int numDying, int numCollectable, int numAlienShipLow, int numAlienShipHigh) {
        mockSoundManager.playSound(soundOption);

        verify(laser, times(numLaser)).play();
        verify(dyingSound, times(numDying)).play();
        verify(switchOption, times(numSwitch)).play();
        verify(backgroundMusic, times(numMusic)).playContinuously();
        verify(collectableSound, times(numCollectable)).play();
        verify(alienShipLowPitch, times(numAlienShipLow)).playContinuously();
        verify(alienShipHighPitch, times(numAlienShipHigh)).playContinuously();
        mockedStatic.close();

    }

    @Test
    public void testPlaySoundNull() {
        assertThrows(Exception.class, () -> mockSoundManager.playSound(null));
        mockedStatic.close();

    }

    @ParameterizedTest
    @CsvSource({
            "MUSIC, 1, 0, 0, 0, 0, 0, 0",
            "LASER, 0, 1, 0, 0, 0, 0, 0",
            "MENU_SWITCH, 0, 0, 1, 0, 0, 0, 0",
            "DESTRUCTION, 0, 0, 0, 1, 0, 0, 0",
            "COLLECTABLE, 0, 0, 0, 0, 1, 0, 0",
            "ALIEN_SHIP_LOW, 0, 0, 0, 0, 0, 1, 0",
            "ALIEN_SHIP_HIGH, 0, 0, 0, 0, 0, 0, 1"
    })
    public void testStopSound(Sound_Options soundOption, int numMusic, int numLaser, int numSwitch, int numDying, int numCollectable, int numAlienShipLow, int numAlienShipHigh) {
        mockSoundManager.stopSound(soundOption);

        verify(laser, times(numLaser)).stop();
        verify(dyingSound, times(numDying)).stop();
        verify(switchOption, times(numSwitch)).stop();
        verify(backgroundMusic, times(numMusic)).stop();
        verify(collectableSound, times(numCollectable)).stop();
        verify(alienShipLowPitch, times(numAlienShipLow)).stop();
        verify(alienShipHighPitch, times(numAlienShipHigh)).stop();
        mockedStatic.close();

    }

    @Test
    public void testStopSoundNull() {
        assertThrows(Exception.class, () -> mockSoundManager.stopSound(null));
        mockedStatic.close();

    }

    @Test
    public void testResumePlayingMusic() {
        mockSoundManager.resumePlayingMusic();
        verify(backgroundMusic).resumePlaying();
        mockedStatic.close();

    }

    @Test
    public void testResumePlayingAlienShipSound() {
        mockSoundManager.resumePlayingAlienShipSound();
        verify(alienShipLowPitch).resumePlaying();
        verify(alienShipHighPitch).resumePlaying();
        mockedStatic.close();

    }

    @Test
    public void testStopAllSounds() {
        mockSoundManager.stopAllSounds();
        verify(backgroundMusic).stop();
        verify(laser).stop();
        verify(switchOption).stop();
        verify(dyingSound).stop();
        verify(collectableSound).stop();
        verify(alienShipLowPitch).stop();
        verify(alienShipHighPitch).stop();
        mockedStatic.close();

    }

    @Test
    public void testSoundManagerConstructor() throws Exception {
        Constructor<SoundManager> constructor = SoundManager.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        List<String> expectedPaths = Arrays.asList(
                "src/main/resources/sounds/shoot.wav",
                "src/main/resources/sounds/invaderkilled.wav",
                "src/main/resources/sounds/Menu_option.wav",
                "src/main/resources/sounds/spaceinvaders1.wav",
                "src/main/resources/sounds/Collectable.wav",
                "src/main/resources/sounds/ufo_highpitch.wav",
                "src/main/resources/sounds/ufo_lowpitch.wav"
        );

        //ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
        MockedConstruction<Sound> mockedSound = mockConstruction(Sound.class,
                (mock, context) -> {
                    assertTrue(expectedPaths.contains(context.arguments().getFirst()));
                });

        constructor.newInstance();

        assertEquals(7, mockedSound.constructed().size());
        List<Sound> constructedSounds = mockedSound.constructed();
        assertTrue(constructedSounds.stream().allMatch(sound -> sound instanceof Sound));

        mockedSound.close();
        mockedStatic.close();

    }

    @Test
    public void testGetInstance() {
        mockedStatic.close();

        Field instance = assertDoesNotThrow(() -> SoundManager.class.getDeclaredField("soundManager"));
        instance.setAccessible(true);
        assertDoesNotThrow(() -> instance.set(null, null));

        SoundManager firstCall = SoundManager.getInstance();
        assertNotNull(firstCall);

        SoundManager secondCall = SoundManager.getInstance();
        assertSame(firstCall, secondCall);
    }

    @AfterEach
    public void clear() {
        clearInvocations(mockSoundManager);
        clearInvocations(laser);
        clearInvocations(dyingSound);
        clearInvocations(switchOption);
        clearInvocations(backgroundMusic);
        clearInvocations(collectableSound);
        clearInvocations(alienShipLowPitch);
        clearInvocations(alienShipHighPitch);
    }
}