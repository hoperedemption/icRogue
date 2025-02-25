package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Button;

import javax.swing.*;

public class ICRogue extends AreaGame {
    private ICRoguePlayer player;
    private Level0Room salleCourante;
    private Level level;

    private boolean isOn = true;

    /**
     * Default ICRogue constructor
     */
    public ICRogue() {

    }

    /**
     * Switch Room :
     * player leaves the current area
     * sets the current area to the area destination
     * player enters the area destination, i.e the current room
     * sets the current room as visited
     */
    private void switchRoom(){
        player.leaveArea();
        salleCourante = (Level0Room) setCurrentArea(player.getDestination(), false);
        player.enterArea(salleCourante, player.getCoordDestination());
        salleCourante.setVisited();
    }

    /**
     * calls begin of super class, then initialises the level
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if initialization successful
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            initLevel();
            return true;
        }
        return false;
    }

    /**
     * initLevel
     * initializes the level, in this case with call to a random map
     * a map with standard fixed generation can be called if wished
     *
     * We add all the adequate rooms to the level and set the
     * current area as the start room.
     *
     * We are then creating the player with standard spawn position (2, 2)
     * He then enters the area and the current room is automatically set as visited
     */
    private void initLevel() {
        // Creating the current player Area
        level = new Level0(true);
        level.addAllRooms(this);
        setCurrentArea(level.getNameStartRoom(), false);

        // Creating the player
        salleCourante = (Level0Room) getCurrentArea();
        DiscreteCoordinates playerCoordinates = new DiscreteCoordinates(2, 2);
        player = new ICRoguePlayer(salleCourante, Orientation.UP, playerCoordinates);
        player.enterArea(salleCourante, playerCoordinates);
        salleCourante.setVisited();
    }

    /**
     * update function
     * calls update function from super-class. Gets the keyboard and the needed button
     * for reset (if R is pressed the level, hence also the generated map,  will be reset).
     *
     * The level is automatically updated. If the player goes through a connector then
     * we switch rooms.
     *
     * If the level is resolved pop a message "Winner, winner, chicken dinner!"
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getCurrentArea().getKeyboard();

        resetIfPressed(keyboard.get(Keyboard.R));
        level.update();

        salleCourante.GestionDialogue(player, keyboard.get(Keyboard.A));

        if (player.isGoingThrough()){
            switchRoom();
            player.setGoingThrough();
        }

        if(level.isOff() && isOn) {
            isOn = false;
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "Winner, winner, chicken dinner!");
        }

    }

    /**
     * Resets the level if R is pressed
     * hence calls initLevel
     * @param (b) the button
     */
    private void resetIfPressed(Button b) {
        if(b.isPressed()) {
            initLevel();
        }
    }

    /**
     * Get Title
     * @return the title of the game
     */
    public String getTitle() {
        return "ICRogue";
    }
}