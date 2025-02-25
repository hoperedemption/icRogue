package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Enemy extends ICRogueActor {
    private boolean alive = true;
    protected Sprite sprite;

    /**
     * Default Enemy constructor
     * @param area (Area) : The area where the enemy is registered
     * @param orientation (Orientation) : The orientation of the enemy
     * @param position (DiscreteCoordinates) : the coordinates used to place the enemy
     */
    public Enemy(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * Kill Setter
     * Set the enemy as killed then makes him leave the area
     */
    public void kill() {
        alive = false;
        leaveArea();
    }

    /**
     * Get if the enemy is alive or dead.
     * @return alive status
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Draw Method
     * Draw the enemy in the canvas
     * @param canvas (Canvas) target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(isAlive()) {
            sprite.draw(canvas);
        }
    }

    /**
     * Update Overridden Method
     * update all the parameters of the enemy.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        if(isAlive()) {
            super.update(deltaTime);
        }
    }
}
