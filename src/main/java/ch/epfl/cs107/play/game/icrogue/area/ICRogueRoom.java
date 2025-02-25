package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area implements Logic {
    protected DiscreteCoordinates position; // donne [x][y] position dans la grille
    protected String behaviorName;
    protected ICRogueBehavior behavior;
    protected List<Connector> connectors;

    protected boolean isVisited = false;
    protected boolean isResolved = false;

    /**
     * Default Constructor of ICRogueRoom
     * initializes the position and the behavior name. Then initializes the connector list.
     * Adds all connectors to the connector list.
     * @param newPosition (DiscreteCoordinates) the position of the player in the room
     * @param newBehaviorName (String) the name of the behavior associated to the room
     * @param newConnectorCoordinates (List<DiscreteCoordinates>) the list of all connector coordinates
     * @param newOrientations (List<Orientation>) the list of all orientations
     */
    public ICRogueRoom(DiscreteCoordinates newPosition, String newBehaviorName,
                       List<DiscreteCoordinates> newConnectorCoordinates, List<Orientation> newOrientations
    ) {
        position = newPosition;
        behaviorName = newBehaviorName;
        connectors = new ArrayList<>();
        for(int i = 0; i < newConnectorCoordinates.size(); ++i) {
            connectors.add(new Connector(this, newOrientations.get(i).opposite(),
                    newConnectorCoordinates.get(i), Connector.State.INVISIBLE));
        }
    }

    /**
     * @return the position of the player
     */
    public DiscreteCoordinates getPosition() {
        return position;
    }

    /**
     * @return the camera scale factor
     */
    @Override
    public float getCameraScaleFactor() {
        return 11.f;
    }

    /**
     * Begin
     * begins the game : calls method from super class
     * Sets the behavior of the room and registers the background
     * Then creates the area
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if initialized
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            behavior = new ICRogueBehavior(window, behaviorName);
            this.setBehavior(behavior);
            this.registerActor(new Background(this, behaviorName));
            createArea();
            return true;
        }
        return false;
    }

    /**
     * registers all connectors of the area as its actors, thus creating it
     */
    protected void createArea(){
        for(Connector connector : connectors){
            this.registerActor(connector);
        }
    }

    /**
     * calls method from super class. Checks if cheat method is pressed (the one asked
     * in the paper). If the room is still logically on, thus still resolved then
     * iterate through all connectors and get their state. If it is closed
     * the open them, because the room is resolved.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(isOff()){
            for(Connector connector : connectors){
                if(connector.getState() == Connector.State.CLOSED){
                    connector.setState(Connector.State.OPEN);
                }
            }
        }
        setResolved();
    }

    /**
     * Cheat method ifPressed (not necessary, but was asked)
     * Opens all connectors by pressing O
     * Locks all connectors by pressing L
     * Switches the state of connectors by pressing T
     */
    private void ifPressed(){
        Keyboard keyboard = getKeyboard();

        if(keyboard.get(Keyboard.O).isPressed()){
            for(Connector connector : connectors){
                connector.setState(Connector.State.OPEN);
            }
        }
        else if(keyboard.get(Keyboard.L).isPressed()){
            connectors.get(0).setKeyID(1);
            connectors.get(0).setState(Connector.State.LOCKED);
        }else if(keyboard.get(Keyboard.T).isPressed()){
            for(Connector connector : connectors){
                if(!connector.isLocked()){
                    connector.switchState();
                }
            }
        }
    }

    /**
     * Implementation of Logic interface
     * @return true if not resolved
     */
    @Override
    public boolean isOn() {
        return !isResolved;
    }

    /**
     * Implementation of Logic interface
     * @return true if resolved
     */
    @Override
    public boolean isOff() {
        return isResolved;
    }

    /**
     * @return 1.f if is on otherwise 0.f
     */
    @Override
    public float getIntensity() {
        return isOn() ? 1.f : 0.f;
    }

    /**
     * sets visited status to true
     */
    public void setVisited() {
        isVisited = true;
    }

    /**
     * sets resolved status to true
     */
    public void setResolved() {
        isResolved = isVisited;
    }

}