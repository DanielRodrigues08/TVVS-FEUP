package SpaceInvaders.State;

import SpaceInvaders.Controller.Menu.GameOverController;
import SpaceInvaders.Controller.Menu.PauseMenuController;
import SpaceInvaders.Controller.Menu.StartMenuController;
import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Menu.*;
import SpaceInvaders.Model.Sound.Sound_Options;
import SpaceInvaders.Viewer.Menu.*;
import SpaceInvaders.Controller.Controller;
import SpaceInvaders.Controller.Game.ArenaController;
import SpaceInvaders.Controller.Menu.OnlyTextMenuController;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.ArenaBuilderByRound;
import SpaceInvaders.Viewer.Game.GameViewer;
import SpaceInvaders.Viewer.Viewer;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;
import java.net.URISyntaxException;

public class State {
    private GameStates currentState;
    private GameStates previousState;

    private Controller controller;

    private ArenaController arenaController;

    private Viewer viewer;

   private Arena arena;

   private static  State instance;


    private State(){
        currentState = GameStates.START_MENU;
        previousState = GameStates.START_MENU;
        StartMenu menu = new StartMenu();
        viewer = new StartMenuViewer(menu);
        controller = new StartMenuController(menu);
    }

    public static State getInstance(){
        if(instance == null){
            instance = new State();
        }
        return instance;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

    public Controller getController() {
        return controller;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public GameStates getCurrentState() {
        return currentState;
    }

    public GameStates getPreviousState() {
        return previousState;
    }

    public void UpdateState(GameStates newState) throws IOException, URISyntaxException {
        if(newState == GameStates.START_MENU){
            previousState = GameStates.START_MENU;
        }
        else {
            previousState = currentState;
        }
        currentState = newState;
        StateActions();
    }

    public void UpdateToPrevious() throws IOException, URISyntaxException {
        GameStates aux = currentState;
        currentState = previousState;
        previousState = aux;
        StateActions();
    }

    public void step(GUI gui, Game game, long time) throws IOException, URISyntaxException {
        KeyStroke key = gui.getNextAction();
        controller.step(game,key,time);
        viewer.draw(gui, time);
    }

    public void StateActions () throws IOException, URISyntaxException {

        switch (currentState){
            case START_MENU:
                StartMenu menuS = new StartMenu();
                controller = new StartMenuController(menuS);
                viewer = new StartMenuViewer(menuS);
                break;

            case PAUSE:
                PauseMenu menuP = new PauseMenu();
                controller = new PauseMenuController(menuP);
                viewer = new PauseMenuViewer(menuP);
                SoundManager.getInstance().stopAllSounds();
                break;

            case NEW_GAME:
                ArenaBuilderByRound arenaBuilder;
                arenaBuilder = new ArenaBuilderByRound(1);
                this.arena = arenaBuilder.buildArena();
                controller = new ArenaController(arena);
                arenaController = (ArenaController) controller;
                viewer = new GameViewer(arena);
                SoundManager.getInstance().playSound(Sound_Options.MUSIC);
                break;

            case LEADERBOARD:
                Leaderboard leaderboard = new Leaderboard();
                controller = new OnlyTextMenuController(leaderboard);
                viewer = new LeaderboardViewer(leaderboard);
                break;

            case GAME_OVER:
                GameOverMenu menuG = new GameOverMenu(arena.getScore());
                controller = new GameOverController(menuG);
                viewer = new GameOverMenuViewer(menuG);
                SoundManager.getInstance().stopAllSounds();
                break;

            case NEW_GAME_ROUND:
                ArenaBuilderByRound newArenaBuilder = new ArenaBuilderByRound(arena.getRound() + 1);
                int score = this.arena.getScore();
                this.arena = newArenaBuilder.buildArena();
                this.arena.increaseScore(score);
                controller = new ArenaController(arena);
                arenaController = (ArenaController) controller;
                viewer = new GameViewer(arena);
                break;

            case RESUME_GAME:
                controller = arenaController;
                viewer = new GameViewer(arena);
                SoundManager.getInstance().resumePlayingMusic();
                if(arena.getAlienShip() != null){
                    SoundManager.getInstance().resumePlayingAlienShipSound();
                }
                break;

            case INSTRUCTIONS:
                Instructions instructions = new Instructions();
                controller = new OnlyTextMenuController(instructions);
                viewer = new InstructionsViewer(instructions);
                break;

            case QUIT_GAME: // No new controllers or viewers, the main function will close the game
        }

    }

}
