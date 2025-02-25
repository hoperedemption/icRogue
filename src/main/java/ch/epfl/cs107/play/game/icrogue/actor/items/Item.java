package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Item extends CollectableAreaEntity {
    protected Sprite sprite;

    /**
     * Default Item Constructor
     * @param area (area) The item's area
     * @param orientation (orientation) item's orientation
     * @param position (position) The item's position in the given area
     */
    public Item(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

    }

    /**
     * Take Cell Space Getter
     * @return (boolean) false, bc doesn't take the cell's space by default
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Cell Interactable Getter
     * @return (boolean) true, bc possible to have a cell interaction
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * View Interactable
     * @return (boolean) false bc not view interactable by default
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Get Current Cells
     * @return (List<DiscreteCoordinates>) the current cells
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Draw
     * @param canvas target, not null
     * draws the associated sprite
     */
    @Override
    public void draw(Canvas canvas) {
        if(!isCollected()) {
            sprite.draw(canvas);
        }
    }

    /**
     * Update
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * unregistered the item from the area if is collected
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(isCollected()){
            getOwnerArea().unregisterActor(this);
        }
    }
}
