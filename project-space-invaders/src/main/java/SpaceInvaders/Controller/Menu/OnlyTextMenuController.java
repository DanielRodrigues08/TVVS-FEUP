package SpaceInvaders.Controller.Menu;

import SpaceInvaders.Controller.Controller;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Menu.OnlyTextMenu;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.net.URISyntaxException;

public class OnlyTextMenuController extends Controller<OnlyTextMenu> {

    public OnlyTextMenuController(OnlyTextMenu menu){
        super(menu);
    }

    @Override
    public void step(Game game, KeyStroke key, long time) throws IOException, URISyntaxException {
        if(key == null){
            return;
        }
       if(key.getKeyType() == KeyType.Escape){
           game.setPrevState();
       }
    }
}
