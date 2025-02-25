package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.Seller;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Window;

public class Level0SellerRoom extends Level0Room{

    private Seller seller;

    /**
     * Default constructor of Level0SellerRoom.
     * @param position (DiscreteCoordinates) : the position of the room in the map.
     */
    public Level0SellerRoom(DiscreteCoordinates position){
        super(position);
    }

    /**
     * This method initialize the room with a seller in the middle of this one..
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if initialization successful
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);
        seller = new Seller(this, Orientation.DOWN, new DiscreteCoordinates(5,5));
        registerActor(seller);
        return true;
    }

    /**
     * if a dialog is displayed, it will block the player until he presses the right button.
     * When the right button is pressed,
     * the message is erased from the canvas and the player can move freely again.
     * @param player (ICRoguePlayer) : the main player of the game
     * @param button (Button) : a button used to handle these dialogs
     */
    public void GestionDialogue(ICRoguePlayer player, Button button){
        if(button.isPressed()){
            player.setMessageDisplayed();
            seller.EraseMessage();
        }
    }
}
