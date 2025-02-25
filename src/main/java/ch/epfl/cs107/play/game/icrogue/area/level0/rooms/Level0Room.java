package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueDialogueHandler;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom implements ICRogueDialogueHandler {

    public enum Level0Connectors implements ConnectorInRoom {
        // ordre des attributs: position , destination , orientation
        W(new DiscreteCoordinates(0, 4),
                new DiscreteCoordinates(8, 5), Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0),
                new DiscreteCoordinates(5, 8), Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4),
                new DiscreteCoordinates(1, 5), Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9),
                new DiscreteCoordinates(5, 1), Orientation.UP);

        private DiscreteCoordinates position;
        private DiscreteCoordinates destination;
        private Orientation orientation;

        /**
         * Default constructor of Level0Connectors.
         * @param newPosition (DiscreteCoordinates) : the position of the connector in the room where it belongs
         * @param newDestination (DiscreteCoordinates) : the position of the room they are leading to according to their
         *                       orientation
         * @param newOrientation (Orientation) : the orientation of the connectors
         */
        Level0Connectors(DiscreteCoordinates newPosition, DiscreteCoordinates newDestination,
                         Orientation newOrientation) {
            position = newPosition;
            destination = newDestination;
            orientation = newOrientation;
        }

        /**
         * @return the position of the occurrence of the connector in the enum.
         * the order of apparition is the following : W, S, E, N
         */
        @Override
        public int getIndex() {
            return this.ordinal();
        }

        /**
         * @return the position of the room of destination according to its orientation
         */
        @Override
        public DiscreteCoordinates getDestination() {
            return destination;
        }

        /**
         * @return the list of the orientation of
         * all the connectors that are active in the room (i.e. the ones
         * that are not invisible)
         */
        public static List<Orientation> getAllConnectorsOrientation() {
            List<Orientation> result = new ArrayList<>();
            for (Level0Connectors connector : values()) {
                result.add(connector.orientation);
            }
            return result;
        }

        /**
         * @return the list of coordinates of all the active
         * connectors in the room (i.e. the ones that are not invisible)
         */
        public static List<DiscreteCoordinates> getAllConnectorsPosition() {
            List<DiscreteCoordinates> result = new ArrayList<>();
            for (Level0Connectors connector : values()) {
                result.add(connector.position);
            }
            return result;
        }

    }

    /**
     * @return the title of the room. The scheme is the following : level0 + x + y
     */
    public String getTitle(){
        return ("icrogue/level0" + position.x) + position.y;
    }

    /**
     * Default constructor of Level0Room.
     * @param pos (DiscreteCoordinates) : the position of the room in the map
     */
    public Level0Room(DiscreteCoordinates pos) {
        super(pos, "icrogue/Level0Room", Level0Connectors.getAllConnectorsPosition(),
                Level0Connectors.getAllConnectorsOrientation());
    }

    /**
     * Begins the initialization of the room.
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return true if initialization was successful
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);
        return true;
    }
}