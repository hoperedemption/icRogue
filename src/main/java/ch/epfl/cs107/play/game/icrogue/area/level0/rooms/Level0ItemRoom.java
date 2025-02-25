package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class Level0ItemRoom extends Level0Room {
    protected List<Item> items;

    /**
     * Default constructor of Level0ItemRoom.
     * @param pos (DiscreteCoordinates) : the position of the room in the map.
     */
    public Level0ItemRoom(DiscreteCoordinates pos) {
        super(pos);
        items = new ArrayList<>();
    }

    /**
     * adds an items in the list of the available items of the room.
     * @param item (Item) : the item that must be added in the room
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Initialize the room and register all the items into it, so that they can be displayed on the screen.
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true is has been initialised
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);
        for(Item item : items) {
            registerActor(item);
        }
        return true;
    }


    /**
     * A room is resolved only if all its items has been taken.
     * Thus, this method verifies this to set
     * if the room is resolved or not
     */
    @Override
    public void setResolved() {
        if(isVisited) {
            int i = 0;
            while(i < items.size() && items.get(i).isCollected()) {
                ++i;
            }

            if(i >= items.size()) {
                isResolved = true;
            }
        }
    }
}

