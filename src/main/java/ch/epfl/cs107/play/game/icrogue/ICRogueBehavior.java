package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.window.Window;

public class ICRogueBehavior extends AreaBehavior {

    /**
     * ICRogueBehavior
     * initializes the behavior map with given height and width
     * initializes the cells of the behavior map appropriately
     * @param window (Window)
     * @param name (String)
     */
    public ICRogueBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICRogueBehavior.ICRogueCellType color = ICRogueBehavior.ICRogueCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICRogueBehavior.ICRogueCell(x,y,color));
            }
        }
    }

    public enum ICRogueCellType {
        NONE(0,false), // Should never been used except in the toType method
        GROUND(-16777216, true), // traversable
        WALL(-14112955, false), // non traversable
        HOLE(-65536, true);

        final int type;
        final boolean isWalkable;

        /**
         * Standard ICRogueCellType Constructor
         * @param type (int) the type of the cell
         * @param isWalkable (boolean) the status of cell, hence if is walkable or not
         */
        ICRogueCellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }

        /**
         * @param type (int) position in the enum
         * @return the cell coresponding to given type
         */
        public static ICRogueCellType toType(int type){
            for(ICRogueCellType ict : ICRogueCellType.values()){
                if(ict.type == type)
                    return ict;
            }

            return NONE;
        }

        /**
         * @param other (ICRogueCellType) other type
         * @return if the two ICRogueCellType are equal or not
         * They are equal if they have the same type and are both walkable
         */
        public boolean equals(ICRogueBehavior.ICRogueCellType other) {
            return other.type == type && other.isWalkable == isWalkable;
        }
    }

    public class ICRogueCell extends Cell  {
        private final ICRogueBehavior.ICRogueCellType type;

        /**
         * Default Constructor of ICRogueCell
         * @param x first coordinate
         * @param y second coordinate
         * @param type the cell's type
         */
        public  ICRogueCell(int x, int y, ICRogueBehavior.ICRogueCellType type){
            super(x, y);
            this.type = type;
        }

        /**
         * @param entity (Interactable), not null
         * @return true, because you can always leave a cell
         */
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /**
         * Loops through all entities in the cell
         * and checks if they take cell space or not
         * If one of them takes the cell's space
         * then the cell cannot be entered
         * @param entity (Interactable), not null
         * @return true, if no other entities are taking the cell
         */
        @Override
        protected boolean canEnter(Interactable entity) { // canEnter(Baton)
            for(Interactable e : entities) {
                if(e.takeCellSpace()) {
                    return false;
                }
            }
            return type.isWalkable;
        }

        /**
         * @return true, because you can always interact inside cell with cell
         */
        @Override
        public boolean isCellInteractable() {
            return true;
        }

        /**
         * @return false, because cannot interact with cell outside cell
         */
        @Override
        public boolean isViewInteractable() {
            return false;
        }

        /**
         * @return type of the ICRogueCellType
         */
        public ICRogueBehavior.ICRogueCellType getType() {
            return type;
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
}
