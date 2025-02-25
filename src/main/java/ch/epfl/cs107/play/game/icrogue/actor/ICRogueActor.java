package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

abstract public class ICRogueActor extends MovableAreaEntity {
    /**
     * Default ICRogueActor Constructor
     * @param (area) the area of ICRogueActor
     * @param (orientation) the orientation of ICRogueActor
     * @param (position) the position of ICRogueActor
     */
    public ICRogueActor(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * Takes Cell Space
     * @return (boolean) returns falls, because doesn't take the cell's space
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Is Cell Interactable
     * @return (boolean) return true, because can interact in the given cell
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Is View Interactable
     * @return (boolean) false, because ICRogueActor is not view interactable
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Is Leave Area
     * unregisters the actor from the current area
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Enter Area
     * Enters the area, with give position
     * Registeres the actor in the given are, sets his owner area as the given area
     * sets the current position to the given position, then resets the motion
     * @param (area) the area to be entered
     * @param (position) the position in the area to be entered
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * Get Current Cells
     * @return (List<DiscreteCoordinates>) gets the current cells that the actor occupies
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

}
