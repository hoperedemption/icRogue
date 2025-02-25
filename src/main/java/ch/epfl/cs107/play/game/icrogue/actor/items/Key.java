package ch.epfl.cs107.play.game.icrogue.actor.items;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Key extends Item{

    private final int identifiant;

    /**
     * Default Key Constructor
     * @param area (area) The key's area
     * @param orientation (orientation) key's orientation
     * @param position (position) The key's position in the given area
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, int id) {
        super(area, orientation, position);
        identifiant = id;
        sprite = new Sprite("icrogue/key", 0.6f, 0.6f, this);
    }

    /**
     * Identification Getter
     * @return (int) the key's id
     */
    public int getIdentifiant(){
        return identifiant;
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
}