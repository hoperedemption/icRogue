package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.*;

public abstract class Level implements Logic {
    /**
     * The MapState type allows the placement algorithm to operate
     * by marking the status of a given position while the algorithm is running.
     */
    protected enum MapState {
        NULL, // Empty space
        PLACED, // The room has been placed but not yet
        // explored by the room placement algorithm
        EXPLORED,
        // The room has been placed and explored by the algorithm
        BOSS_ROOM, // The room is a boss room
        CREATED; // The room has been instantiated in the room map

        @Override
        public String toString() {
            return Integer.toString(ordinal());
        }
    }

    protected ICRogueRoom[][] map;

    private MapState[][] mainStateMap;

    private int nbRooms;
    protected DiscreteCoordinates arrivalRoom;
    protected DiscreteCoordinates bossPosition;
    protected String roomStart;

    private final int mapWidth;
    private final int mapHeight;
    protected boolean isResolved = false;
    protected List<DiscreteCoordinates> salleInitialisee = new ArrayList<>();

    /**
     * Places a room at a given position on a map
     * @param (coords) the given coordinates
     * @param (room) the room to be placed
     */
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room) {
        map[coords.x][coords.y] = room;
    }

    /**
     * Places the area destination of a given connector depending on:
     * @param (coords) the coordinates of the connector in the room
     * @param (destination) the coordinates of the destination room
     * @param (connector) the given connector
     */
    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector) {
        toChange(coords, connector).setAreaDestination(destination);
    }

    /**
     * Sets the connector depending on :
     * @param (coords) (DiscreteCoordinates) the coordinates of the room in which the connector shall be placed
     * @param destination (String) the name of the area destination
     * @param connector (ConnectorInRoom) the connector to be placed
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector) {
        setRoomConnectorDestination(coords, destination, connector);
        toChange(coords, connector).setState(Connector.State.CLOSED);
    }

    /**
     * Set lock room connector depending on :
     * @param (coords) (DiscreteCoordinates) the coordinates of the room in which the connector shall be placed
     * @param (connector) (connector) the connector to be placed
     * @param (keyId) (int) the keyID wich will allow to open the connector given the key with same id
     */
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId) {
        toChange(coords, connector).setKeyID(keyId);
        toChange(coords, connector).setState(Connector.State.LOCKED);
    }

    /**
     * Gives the connector on map in the connector arrays depending on:
     * @param (coords) (DiscreteCoordinates) the room position on the map
     * @param (connector) (ConnectorInRoom) the given connector
     * @return the connector as said above
     */
    private Connector toChange(DiscreteCoordinates coords, ConnectorInRoom connector) {
        int index = connector.getIndex();
        return map[coords.x][coords.y].connectors.get(index);
    }

    /**
     * Sets the start room name to the given position
     * @param (coords) (DiscreteCoordinates) the position of the start room
     */
    public void setNameStartRoom(DiscreteCoordinates coords) {
        roomStart = map[coords.x][coords.y].getTitle();
    }

    /**
     * @return the names of room where the game is to be started
     */
    public String getNameStartRoom() {
        return roomStart;
    }

    /**
     * If a random map is :
     * --> not asked :
     *     - generates the map given the height and width. Then sets the bossPosition to (0, 0)
     *       as convention. Generates fixed map in a non-abstract implementation of Level,
     *       in our case Level0. The arrivalRoom is set to be the startPosition that is given.
     *       The start room name is set accordingly.
     *       - generates a totally random map. First counts the total number of rooms given in the
     *       wished room distribution. Then generates the random room placement on the state map,
     *       saving generated maps into an array (salleInitialisee).
     *       Prints the state map after the room placement is done. Generates the random map
     *       accordingly to the map state scheme generated previously, using the positions
     *       of the generated maps on the state map (salleInitialisee)
     *       For each room we initialized we will set up connectors, it is a boss room
     *       we will also lock the connectors of neighbouring rooms.
     * @param (randomMap) (boolean) indicates if a random map is asked
     * @param (startPosition) (DiscreteCoordinates) the position in the start room
     *                        where the player shall be spawned
     * @param (roomDistribution) (int[]) the roomDistribution
     * @param (width) (int) width of the map
     * @param height (int) height of the map
     */
    public Level(boolean randomMap, DiscreteCoordinates startPosition, int[] roomDistribution,
                 int width, int height) {
        if(!randomMap) {
            mapWidth = width;
            mapHeight = height;
            map = new ICRogueRoom[height][width];
            bossPosition = new DiscreteCoordinates(0, 0);
            generateFixedMap();

            arrivalRoom = startPosition;
            setNameStartRoom(arrivalRoom);
        } else {
            nbRooms = getSumRooms(roomDistribution);
            mapWidth = nbRooms;
            mapHeight = nbRooms;
            map = new ICRogueRoom[mapHeight][mapWidth];


            mainStateMap = generateRandomRoomPlacement();
            printMap(mainStateMap);


            generateRandomMap(roomDistribution, salleInitialisee);

            for(DiscreteCoordinates coord : salleInitialisee){
                if(mainStateMap[coord.x][coord.y]!=MapState.BOSS_ROOM){
                    mainStateMap[coord.x][coord.y] = MapState.CREATED;
                } else {
                    setUpLockedConnector(mainStateMap, map[coord.x][coord.y]);
                }
                setUpConnector(mainStateMap, map[coord.x][coord.y]);
            }

            arrivalRoom = new DiscreteCoordinates(nbRooms/2, nbRooms/2);
            setNameStartRoom(arrivalRoom);
        }

    }

    /**
     * abstract method that will be overwritten in Level0
     * @param (roomDistribution) (int[])
     * @param salle (List<DiscreteCoordinates>)
     */
    abstract public void generateRandomMap(int[] roomDistribution, List<DiscreteCoordinates> salle);

    /**
     * abstract method that will set adequately the connectors, overwritten in Level0
     * @param (roomsPlacement) (MapState [][])
     * @param room (ICRogueRoom)
     */
    abstract protected void setUpConnector(MapState [][] roomsPlacement , ICRogueRoom room);

    /**
     * abstract method that will lock adequately the connectors, overwritten in Level0
     * @param (roomsPlacement) (MapState [][])
     * @param (boss) (ICRogueRoom)
     */
    abstract protected void setUpLockedConnector(MapState [][] roomsPlacement , ICRogueRoom boss);

    /**
     * Note that the stateMap matrice will be enlarged in this part of the generation
     * in order to ignore the edge cases. We will thus enlarge the borders : add two columns on the right
     * and left with null pointers and two lines on the bottom and on the top with null pointers.
     * The centered matrice will have entries initialised to MapState.NULL.
     * The first map placed is the one on the center of the stateMap.
     *
     * We will then have nbRooms + 1 rooms to place counting the rooms that we have to place
     * and additionally the boosRoom.
     *
     * We decided to implement a breadth first search that places the rooms depending on
     * the last rooms placed that we stock in a queue. Rooms are placed in a queue
     * when placed on the stateMap : the queue of placed maps.
     *
     * On the iteration where it is taken out of the queue it is considered explored. Then we
     * place a random number of rooms (place) using the inner while. Here we generate a random
     * position (integer) for the room to be placed. It is always one of the room's neighbours
     * (top, bottom, right, left).
     *
     * We also always have a lastPosition variable that stocks the last positioned room.
     * The last positioned room will be effectively the bossRoom.
     *
     * The last part would be just to loop through the mapState matrice and add the initialised
     * maps into the salleInitialisee array. We decided to implement it this way rather
     * than just adding rooms into the arrays as we place them, because it
     * solved a clash we had with another method, generally makes more sense because you
     * would add them to the array after they've been created and not just placed and
     * doesn't ruin overall asymptotic (looping through the matrice is not
     * ideal but also not dramatic).
     * @return the MapState matrice with randomly generated rooms
     */
    protected MapState[][] generateRandomRoomPlacement() {
        int localWidth = nbRooms + 2;
        int localHeight = nbRooms + 2;

        MapState[][] stateMap = new MapState[localHeight][localWidth];
        // two lines and two columns will be null to instantiate the border

        for(int i = 1; i <= nbRooms; ++i) {
            for(int j = 1; j <= nbRooms; ++j) {
                stateMap[i][j] = MapState.NULL;
            }
        }

        int centerWidth = localWidth/2;
        int centerHeight = localHeight/2;

        stateMap[centerHeight][centerWidth] = MapState.PLACED;

        DiscreteCoordinates lastPlace = new DiscreteCoordinates(centerHeight, centerWidth);

        int roomsToPlace = nbRooms + 1;

        Queue<DiscreteCoordinates> q = new LinkedList<>();
        DiscreteCoordinates p1 = new DiscreteCoordinates(centerHeight, centerWidth);
        q.add(p1);
        roomsToPlace--;

        while(!q.isEmpty() && roomsToPlace > 0) {
            DiscreteCoordinates t = q.peek();
            q.remove();

            int x = t.x;
            int y = t.y;

            stateMap[x][y] = MapState.EXPLORED;

            int freeSlots = 0;

            if(stateMap[x + 1][y] == MapState.NULL) {
                ++freeSlots;
            }
            if(stateMap[x - 1][y] == MapState.NULL) {
                ++freeSlots;
            }
            if(stateMap[x][y + 1] == MapState.NULL) {
                ++freeSlots;
            }
            if(stateMap[x][y - 1] == MapState.NULL) {
                ++freeSlots;
            }

            int minToPlace = Math.min(freeSlots, roomsToPlace);

            int place = 1;
            if(minToPlace > 1) {
                int min = Math.min(freeSlots, roomsToPlace);
                if(min > 1) {
                    place = RandomHelper.roomGenerator.nextInt(1, Math.min(freeSlots, roomsToPlace));
                }

            }

            while(place > 0) {
                int position = RandomHelper.roomGenerator.nextInt(1, 5);
                switch (position) {
                    case 1 -> {
                        if(x + 1 < nbRooms + 2) {
                            if(stateMap[x+1][y] == MapState.NULL)
                            {
                                stateMap[x + 1][y] = MapState.PLACED;

                                DiscreteCoordinates p2 = new DiscreteCoordinates(x + 1, y);
                                q.add(p2);

                                lastPlace = p2;

                                place--;
                                roomsToPlace--;
                            }
                        }
                    }

                    case 2 -> {
                        if(x - 1 >= 0) {
                            if(stateMap[x-1][y] ==MapState.NULL && place > 0)
                            {
                                stateMap[x - 1][y] = MapState.PLACED;

                                DiscreteCoordinates p2 = new DiscreteCoordinates(x - 1, y);
                                q.add(p2);

                                lastPlace = p2;

                                place--;
                                roomsToPlace--;
                            }
                        }
                    }

                    case 3 -> {
                        if(y + 1 < nbRooms + 2){
                            if(stateMap[x][y+1]  == MapState.NULL)
                            {
                                stateMap[x][y + 1] = MapState.PLACED;

                                DiscreteCoordinates p2 = new DiscreteCoordinates(x, y + 1);
                                q.add(p2);

                                lastPlace = p2;

                                place--;
                                roomsToPlace--;
                            }
                        }
                    }

                    case 4 -> {
                        if(y - 1 >= 0) {
                            if (stateMap[x][y - 1] == MapState.NULL) {

                                stateMap[x][y - 1] = MapState.PLACED;

                                DiscreteCoordinates p2 = new DiscreteCoordinates(x, y - 1);
                                q.add(p2);

                                lastPlace = p2;

                                place--;
                                roomsToPlace--;
                            }
                        }
                    }
                }
            }

        }

        stateMap[lastPlace.x][lastPlace.y] = MapState.BOSS_ROOM;
        MapState[][] result = new MapState[nbRooms][nbRooms];

        int x = 0;
        int y = 0;

        int i;
        int j;

        for(i = 1; i <= nbRooms; ++i) {
            y = 0;
            for(j = 1; j <= nbRooms; ++j) {
                result[x][y] = stateMap[i][j];
                if(stateMap[i][j]!=MapState.NULL && stateMap[i][j] != MapState.BOSS_ROOM){
                    salleInitialisee.add(new DiscreteCoordinates(x,y));
                }

                if(stateMap[i][j] == MapState.BOSS_ROOM) {
                    bossPosition = new DiscreteCoordinates(x, y);
                }
                ++y;
            }
            ++x;
        }

        salleInitialisee.add(bossPosition);

        return result;
    }

    /**
     * Print map : given method just prints the map
     * @param (map) (MapState[][]) the MapState matrice
     */
    private void printMap(MapState[][] map) {
        System.out.println("Generated map:");
        System.out.print(" | ");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("--|-");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print("--");
        }
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Get Sum Rooms
     * Gets the overall number of rooms from the given room distribution
     * @param (roomDistribution) (int[]) the roomDistribution
     * @return the overall number of rooms
     */
    protected int getSumRooms(int[] roomDistribution) {
        int result = 0;
        for(int i = 0; i < roomDistribution.length; ++i) {
            result += roomDistribution[i];
        }
        return result;
    }

    /**
     * adds all the rooms to ICRogue game
     * @param (rogue) (ICRogue) the game itself
     */
    public void addAllRooms(ICRogue rogue) {
        for(int i = 0; i < mapHeight; ++i) {
            for(int j = 0; j < mapWidth; ++j) {
                if(map[i][j] != null) {
                    rogue.addArea(map[i][j]);
                }
            }
        }
    }

    /**
     * update function of the Level
     * if the map is logically off and that it has not yet been resolved (to avoid
     * printing "Winner" many times) then tell the player that he won by printing "Winner"
     * on the console
     */
    public void update() {
        if(map[bossPosition.x][bossPosition.y].isOff() && !isResolved) {
            setResolved();
            System.out.println("Winner");
        }
    }

    /**
     * abstract generateFixedMap function to be implemented in
     * subclasses, in our case Level0. Generates a map with fixed parameters.
     */
    public abstract void generateFixedMap();

    /**
     * implementation of Logic interface
     * gives the status of the Level
     * @return true if not yet resolved
     */
    @Override
    public boolean isOn() {
        return !isResolved;
    }

    /**
     * implementation of Logic interface
     * gives the status of the Level
     * @return true if already resolved
     */
    @Override
    public boolean isOff() {
        return isResolved;
    }

    /**
     * @return 1.f if isOn() otherwise 0.f
     */
    @Override
    public float getIntensity() {
        return isOn() ? 1.f : 0.f;
    }

    /**
     * sets resolved status to true
     */
    public void setResolved() {
        isResolved = true;
    }
}