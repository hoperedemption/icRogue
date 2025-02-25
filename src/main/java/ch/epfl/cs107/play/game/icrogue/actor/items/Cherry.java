package ch.epfl.cs107.play.game.icrogue.actor.items;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Cherry extends Item {
    private boolean isEaten;

    /**
     * Default Cherry constructor
     * @param area (Area) : The area where the cherry is registered
     * @param orientation (orientation) : The orientation of the created cherry
     * @param position (position) : The position of the created cherry in its area
     * Calls constructor of super class with the given paramaters
     * Gives isEaten the value false by default (when created not eaten by the player)
     * Assigns the particular sprite to the cherry
     */
    public Cherry(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        isEaten = false;
        sprite = new Sprite("icrogue/cherry", 0.6f, 0.6f, this);
    }


    /**
     * Makes the visitor v handle the interaction between the two entities.
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean) : tells if the interaction is a CellInteraction or not.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    /**
     * isEaten method indicator
     * @return (boolean) isEaten : gives the eaten status
     */
    public boolean isEaten(){
        return isEaten;
    }

    /**
     * isEaten Setter
     * sets isEaten to true : --> the player eats the cherry
     */
    public void eat(){
        isEaten = true;
    }
}
