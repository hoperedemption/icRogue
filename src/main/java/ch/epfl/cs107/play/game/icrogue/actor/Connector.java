package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends AreaEntity {

    /**
     * Enum State
     * Gives the possible types of connectors :
     * OPEN, CLOSED, LOCKED, INVISIBLE
     */
    public enum State {
        OPEN,
        CLOSED,
        LOCKED,
        INVISIBLE
    }
    private final static int NO_KEY_ID = -2;
    private int keyID;
    private Sprite sprite;
    private String areaDestination;
    private DiscreteCoordinates positionAreaDestination;
    private State state;

    /**
     * Current Cells Getter
     * @return (List<DiscreteCoordinates>) returns the current cells
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates(); return List.of(coord, coord.jump(new
                Vector((getOrientation().ordinal()+1)%2, getOrientation().ordinal()%2)));
    }

    /**
     * Gets the keyId
     * @return (int) the key id associated with the connector
     */
    public int getKeyID(){
        return keyID;
    }

    /**
     * Gets the Area Destination
     * @return (String) the area destination
     */
    public String getAreaDestination(){
        return areaDestination;
    }

    /**
     * Gets the Position of Area Destination
     * @return (DiscreteCoordinates) the position Area Destination
     */
    public DiscreteCoordinates getPositionAreaDestination() {
        return positionAreaDestination;
    }

    /**
     * Gets take cell space
     * @return (boolean) returns the state open status
     * take cell Space if not open
     */
    @Override
    public boolean takeCellSpace() {
        return state != State.OPEN;
    }

    /**
     * Gets Cell Interactable Status
     * @return (boolean) true by default, because if cell interactable
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Gets View Interactable
     * @return (boolean) true by default, because is view interactable
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Makes the visitor v handle the interaction between the two entities.
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean) : tells if the interaction is a CellInteraction or not.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
    }

    /**
     * Set Destination
     * @param orientation (orientation) : the orientation of the connector
     * @return (DiscreteCoordinates) : the destination depending on the connector orientation
     */
    private DiscreteCoordinates setDestination(Orientation orientation) {
        return switch (orientation.opposite()) {
            case LEFT ->  Level0Room.Level0Connectors.W.getDestination();
            case RIGHT ->  Level0Room.Level0Connectors.E.getDestination();
            case DOWN ->  Level0Room.Level0Connectors.S.getDestination();
            case UP ->  Level0Room.Level0Connectors.N.getDestination();
        };
    }

    /**
     * Default Connector Constructor
     * @param area (area) the connector's area
     * @param orientation (orientation) the connector's orientation
     * @param position (position) the connector's position
     * @param newKeyID (newKeyID) the connector's new key id
     * @param newState (newState) the connector's
     */
    public Connector (Area area, Orientation orientation, DiscreteCoordinates position,
                      int newKeyID, State newState) {
        super(area, orientation, position);
        positionAreaDestination = setDestination(orientation);
        keyID = newKeyID;
        state = newState;
        setSprite();
    }

    /**
     * Sets the area destination
     * @param newAreaDestination (String) changes the area destination string
     */
    public void setAreaDestination(String newAreaDestination) {
        areaDestination = newAreaDestination;
    }

    /**
     * Default Connector Constructor
     * @param owner (Area) the connector's area
     * @param orientation (Orientation) the connector's orientation
     * @param position (DiscreteCoordinates) the connector's position
     * @param state (State) the connector's state
     */
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates position, State state){
        this(owner, orientation, position, NO_KEY_ID, state);
    }

    /**
     * Set Sprite Function
     * sets the sprite depending on the current state type
     */
    private void setSprite() {
        if(state == State.INVISIBLE) {
            sprite = new Sprite("icrogue/invisibleDoor_"+ getOrientation().ordinal(),
                    (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this);
        } else if(state == State.CLOSED) {
            sprite = new Sprite("icrogue/door_"+ getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this);
        } else if(state == State.LOCKED) {
            sprite = new Sprite("icrogue/lockedDoor_"+getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1,
                    this);
        } else {
            sprite = null;
        }
    }

    /**
     * Draws the associated sprite
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(state != State.OPEN){
            sprite.draw(canvas);
        }
    }

    /**
     * Sets the state to the connector
     * @param (state) the new state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Sets the new keyID to the connector
     * @param (newKeyID) the new KeyID
     */
    public void setKeyID(int newKeyID){
        keyID = newKeyID;
    }

    /**
     * @return true, if the state of the connector is locked
     */
    public boolean isLocked(){
        return state == State.LOCKED;
    }

    /**
     * switch the state from OPEN to CLOSED
     * switch the state from CLOSED to OPEN
     */
    public void switchState() {
        if(state == State.OPEN) {
            state = State.CLOSED;
        } else if (state == State.CLOSED) {
            state = State.OPEN;
        }
    }

    /**
     * Get State
     * @return state of the connector
     */
    public State getState(){
        return state;
    }

    /**
     * calls setSprite to set the appropriate sprite depending on the connector state
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        setSprite();
    }
}