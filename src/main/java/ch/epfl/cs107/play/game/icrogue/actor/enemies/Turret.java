package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class Turret extends Enemy {
    private final static float COOLDOWN = 2.f;
    private List<Orientation> directions;
    private float counter;

    /**
     * Default constructor of Turret
     * @param area (Area) : The area where the turret lives
     * @param orientation (Orientation) : The orientation of the turret
     * @param position (DiscreteCoordinates) : The position of the turret within the area
     * @param newDirections (List<Orientation></>) : List of the orientations of the arrows that are used to orientate
     * these projectiles.
     */
    public Turret(Area area, Orientation orientation, DiscreteCoordinates position,
                  List<Orientation> newDirections) {
        super(area, orientation, position);

        setSprite();
        directions = new ArrayList<>();
        directions.addAll(newDirections);
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

    /**
     * Updates the current parameters of Turret (i.e. Coordinates). Checks if the Turret is alive or not. Depending on that
     * the Turret can attack or not.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(isAlive()) {
            if(counter>=COOLDOWN){
                counter = 0.f;
                attack();
            }
            counter += deltaTime;
        }
    }

    /**
     * This function goes through all the list of the orientations of all
     * the arrows and shoots according to this list.
     */
    private void attack() {
        for(Orientation direction : directions) {
            Arrow arrow = new Arrow(getOwnerArea(), direction, getCurrentCells().get(0));
            arrow.enterArea(getOwnerArea(), getCurrentCells().get(0));
            arrow.move();
        }
    }


    /**
     * Set Sprite
     * sets the associated sprite, to be called in the construction
     */
    private void setSprite() {
        sprite = new Sprite("icrogue/static_npc", 1.5f, 1.5f,
                this, null, new Vector(-0.25f, 0));
    }
}
