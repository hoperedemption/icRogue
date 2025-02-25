package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Staff extends Item {

    /**
     * Default Staff Constructor
     * @param area (area) The staff's area
     * @param orientation (orientation) staff's orientation
     * @param position (position) The staff's position in the given area
     */
    public Staff(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        sprite = new Sprite("zelda/staff_water.icon", .5f, .5f, this);
    }

    /**
     * View Interaction Getter
     * @return (boolean) true, because interaction is by distance, the player picks up the staff
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Take Cell Space
     * @return (boolean) true, because takes the cell's space
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * Makes the visitor v handle the interaction between the two entities.
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean) : tells if the interaction is a CellInteraction or not.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
}
