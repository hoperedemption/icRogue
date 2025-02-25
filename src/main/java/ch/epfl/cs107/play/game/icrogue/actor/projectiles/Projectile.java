package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable, Interactor  {
    protected int frames;
    protected int damage;
    protected boolean isConsumed;
    protected static final int DEFAULT_DAMAGE = 1;
    protected static final int DEFAULT_MOVE_DURATION = 10;

    protected Sprite sprite;

    /**
     * Default constructor of Projectile
     * @param owner (Area) : the area where the projectile lives
     * @param orientation (Orientation) : the orientation of the projectile
     * @param coordinates (DiscreteCoordinates) : the coordinates that are used to place the projectile on area
     * @param newFrames (int) : the default frame used by the projectile to move
     * @param newDamage (int) : the default damage inflicted by the projectile
     */
    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates, int newFrames, int newDamage) {
        super(owner, orientation, coordinates);
        frames = newFrames;
        damage = newDamage;
        isConsumed = false;
        setSprite();
    }

    /**
     * Constructor of Projectile that is using preset damages and preset frames to move. It is transferring all these information
     * to the constructor that is above.
     * @param owner (Area) : the area where the projectile lives
     * @param orientation (Orientation) : the orientation of the projectile
     * @param coordinates (DiscreteCoordinates) : the coordinates that are used to place the projectile on area
     */
    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        this(owner, orientation, coordinates, DEFAULT_MOVE_DURATION, DEFAULT_DAMAGE);
    }

    /**
     * Abstract method that is supposed to set the sprite of a projectile.
     */
    abstract public void setSprite();

    /**
     * Sets the projectile as consumed.
     */
    @Override
    public void consume() {
        isConsumed = true;
    }

    /**
     * @return that the projectile is not taking the cell. Thus, we can go through it.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * @return if the projectile is consumed or not yet.
     */
    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    /**
     * Makes the visitor v handle the interaction between the two entities.
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean) : tells if the interaction is a CellInteraction or not.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if(!isConsumed()){
            ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
        }
    }

    /**
     * Draws the projectile on the canvas.
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * @return the current field of view coordinates around the projectile.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * @return that the projectile accepts to take cell interactions.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * @return that the projectile accepts to take view interactions.
     */
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    /**
     * @return the damages that are inflicted by the projectile.
     */
    public int getDamage() {
        return damage;
    }

}