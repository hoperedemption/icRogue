package ch.epfl.cs107.play.game.icrogue.area.level0;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0 extends Level {
    public enum RoomType {
        CHERRY_ROOM(1),
        STAFF_ROOM(1),
        BOSS_KEY(1),
        SELLER_ROOM(1),
        TURRET_ROOM(3),
        COIN(1),
        SPAWN(1),
        NORMAL(1);

        private final int number;

        /**
         * Default constructor of RoomType.
         * @param numbers (int) : the number of rooms of the specified type of room
         */
        RoomType(int numbers){
            number = numbers;
        }

        /**
         * goes through the whole enum and creates an distribution as an array
         * regrouping by index all the room types and their numbers.
         * @return int[] the distribution
         */
        public static int[] getDestribution() {
            RoomType[] v = values();
            int[] result = new int[values().length - 1];

            for(int i = 0; i < v.length - 2; ++i) {
                result[i] = v[i].number;
            }

            result[6] = v[6].number + v[7].number;

            return result;
        }
    }

    private final int PART_1_KEY_ID = 1;
    private final int BOSS_KEY_ID = 100;

    /**
     * generates a map of 2 rooms, a Level0KeyRoom and a Level0Room
     */
    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    /**
     * generates a map with fixed positions for each type of rooms. the connectors that lead
     * to created rooms are initially closed. The only exception is the boss room
     * because the connectors leading to it are locked with a keyID.
     */
    private void generateMap2() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0MewRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);


        DiscreteCoordinates room01 = new DiscreteCoordinates(0, 1);
        setRoom(room01, new Level0CherryRoom(room01));
        setRoomConnector(room01, "icrogue/level011", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0CoinRoom(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
        setRoomConnector(room11, "icrogue/level001", Level0Room.Level0Connectors.W);
        setRoomConnector(room11, "icrogue/level021", Level0Room.Level0Connectors.E);


        DiscreteCoordinates room12 = new DiscreteCoordinates(2, 1);
        setRoom(room12, new Level0TurretRoom(room12));
        setRoomConnector(room12, "icrogue/level011", Level0Room.Level0Connectors.W);
        setRoomConnector(room12, "icrogue/level031", Level0Room.Level0Connectors.E);


        DiscreteCoordinates room13 = new DiscreteCoordinates(3, 1);
        setRoom(room13, new Level0SellerRoom(room13));
        setRoomConnector(room13, "icrogue/level021", Level0Room.Level0Connectors.W);



    }

    /**
     * Third constructor of Level0. Generates randomly or not a level according to the specifications given in parameters.
     * Also, depending on the room distribution, it constructs a level with these type of rooms.
     * @param randomMap (boolean) : States if the generation is random or not
     * @param roomDistribution (int[]) : the array with the number of rooms to be generated for each type.
     */
    public Level0(boolean randomMap, int[] roomDistribution) {
        super(randomMap, new DiscreteCoordinates(1, 0), roomDistribution , 2, 4);
    }

    /**
     * Second constructor of Level0. Generates randomly or not a level according
     * to the specifications given in parameters.
     * @param randomMap (boolean) : States if the generation must be random or not
     */
    public Level0(boolean randomMap) {
        this(randomMap, RoomType.getDestribution());
    }

    /**
     * Default constructor of Level0. Generates the random map automatically
     */
    public Level0(){
        this(true);
    }

    /**
     * According to an Array of initialized rooms and an array of rooms distribution, this method choose randomly a k number
     * of positions to create the rooms of a certain type. The tab array is filled with the total number of rooms placed,
     * minus the boss position, that represents the indexes of each placed rooms. It will then, choose k random int
     * from this table and then, depending on that will refer to the array of placed rooms to create them in the good position.
     * At the end of the process the boss room is placed at the boss position.
     * @param roomDistribution (int[]) : a room distribution of each room type
     * @param salle (List<DiscreteCoordinates>) : the array of rooms that were placed in the Map-state map
     */
    public void generateRandomMap(int[] roomDistribution, List<DiscreteCoordinates> salle){
        List<Integer> tab = new ArrayList<>();
        DiscreteCoordinates position;
        for(int i =0 ; i < salle.size() - 1; ++i){
            tab.add(i);
        }

        List<Integer> distribution = RandomHelper.chooseKInList(roomDistribution[6], tab);

        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0Room(position));
        }

        distribution = RandomHelper.chooseKInList(roomDistribution[1], tab);
        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0StaffRoom(position));
            //bien mettre les connecteurs.
        }

        distribution = RandomHelper.chooseKInList(roomDistribution[2], tab);
        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0KeyRoom(position, 1 ));
            //bien mettre les connecteurs.
        }

        distribution = RandomHelper.chooseKInList(roomDistribution[4], tab);
        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0TurretRoom(position));
            //bien mettre les connecteurs.
        }

        distribution = RandomHelper.chooseKInList(roomDistribution[5], tab);
        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0CoinRoom(position));
            //bien mettre les connecteurs.
        }

        distribution = RandomHelper.chooseKInList(roomDistribution[0], tab);
        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0CherryRoom(position));
            //bien mettre les connecteurs.
        }

        distribution = RandomHelper.chooseKInList(roomDistribution[3], tab);
        for(int i = 0; i<distribution.size(); ++i){
            tab.remove(distribution.get(i));
            position = salle.get(distribution.get(i));
            setRoom(position, new Level0SellerRoom(position));
            //bien mettre les connecteurs.
        }

        setRoom(bossPosition, new Level0MewRoom(bossPosition));

    }

    /**
     * This method testes every edge case to assure that every connector will point to a valid room that was created. All
     * of that depends on the position of the destination that is verified (with the fact that if a destination is a null
     * pointer, it is ignored).
     * @param roomsPlacement (MapState[][]) : the map with all placed rooms and null pointers
     * @param room (ICRogueRoom) : the room in which the connectors will be initialized
     */
    @Override
    protected void setUpConnector(MapState [][] roomsPlacement , ICRogueRoom room){

        int x = room.getPosition().x;
        int y = room.getPosition().y;

        if(x==0){
            if(y==0){
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x+1][y].getTitle(), Level0Room.Level0Connectors.S);

                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y+1].getTitle(), Level0Room.Level0Connectors.E);
                }
            }
            else if(y== roomsPlacement.length-1){
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x+1][y].getTitle(), Level0Room.Level0Connectors.S);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y-1].getTitle(), Level0Room.Level0Connectors.W);
                }
            }
            else{
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x+1][y].getTitle(), Level0Room.Level0Connectors.S);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y-1].getTitle(), Level0Room.Level0Connectors.W);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y+1].getTitle(), Level0Room.Level0Connectors.E);
                }
            }
        }
        else if(x == roomsPlacement.length-1){
            if(y==0){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x-1][y].getTitle(), Level0Room.Level0Connectors.N);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y+1].getTitle(), Level0Room.Level0Connectors.E);
                }
            }
            else if(y== roomsPlacement.length-1){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x-1][y].getTitle(), Level0Room.Level0Connectors.N);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y-1].getTitle(), Level0Room.Level0Connectors.W);
                }
            }
            else{
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x-1][y].getTitle(), Level0Room.Level0Connectors.N);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y-1].getTitle(), Level0Room.Level0Connectors.W);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y+1].getTitle(), Level0Room.Level0Connectors.E);
                }
            }
        }
        else{
            if(y==0){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x-1][y].getTitle(), Level0Room.Level0Connectors.N);
                }
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x+1][y].getTitle(), Level0Room.Level0Connectors.S);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y+1].getTitle(), Level0Room.Level0Connectors.E);
                }
            }
            else if(y== roomsPlacement.length-1){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x-1][y].getTitle(), Level0Room.Level0Connectors.N);
                }
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x+1][y].getTitle(), Level0Room.Level0Connectors.S);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y-1].getTitle(), Level0Room.Level0Connectors.W);
                }
            }
            else{
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x-1][y].getTitle(), Level0Room.Level0Connectors.N);
                }
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x+1][y].getTitle(), Level0Room.Level0Connectors.S);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y-1].getTitle(), Level0Room.Level0Connectors.W);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    setRoomConnector(new DiscreteCoordinates(x,y), map[x][y+1].getTitle(), Level0Room.Level0Connectors.E);
                }
            }
        }
    }

    /**
     * This method test every edge case to assure that every connector that would lead to the boss room is locked. It
     * takes the boss room, verifies if the tested room is not a null pointer, and then lock the corresponding connector.
     * @param roomsPlacement (MapState[][]) : the map with all the placed rooms and le null pointer.
     * @param boss (ICRogueRoom) : The boss room
     */
    @Override
    protected void setUpLockedConnector(MapState [][] roomsPlacement , ICRogueRoom boss){

        int x = boss.getPosition().x;
        int y = boss.getPosition().y;


        if(x==0){
            if(y==0){
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x + 1 ,y), Level0Room.Level0Connectors.N, 1);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y + 1), Level0Room.Level0Connectors.W, 1);
                }
            }
            else if(y== roomsPlacement.length-1){
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x + 1,y), Level0Room.Level0Connectors.N, 1);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y - 1), Level0Room.Level0Connectors.E, 1);
                }
            }
            else{
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x + 1,y), Level0Room.Level0Connectors.N, 1);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y - 1), Level0Room.Level0Connectors.E, 1);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y + 1), Level0Room.Level0Connectors.W, 1);
                }
            }
        }
        else if(x == roomsPlacement.length-1){
            if(y==0){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x - 1,y), Level0Room.Level0Connectors.S, 1);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y + 1), Level0Room.Level0Connectors.W, 1);
                }
            }
            else if(y== roomsPlacement.length-1){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x - 1,y), Level0Room.Level0Connectors.S, 1);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y - 1), Level0Room.Level0Connectors.E, 1);
                }
            }
            else{
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x - 1,y), Level0Room.Level0Connectors.S, 1);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y - 1), Level0Room.Level0Connectors.E, 1);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y + 1), Level0Room.Level0Connectors.W, 1);
                }
            }
        }
        else{
            if(y==0){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x - 1,y), Level0Room.Level0Connectors.S, 1);
                }
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x + 1,y), Level0Room.Level0Connectors.N, 1);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y + 1), Level0Room.Level0Connectors.W, 1);
                }
            }
            else if(y== roomsPlacement.length-1){
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x - 1,y), Level0Room.Level0Connectors.S, 1);
                }
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x + 1,y), Level0Room.Level0Connectors.N, 1);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y - 1), Level0Room.Level0Connectors.E, 1);
                }
            }
            else{
                if(roomsPlacement[x-1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x - 1,y), Level0Room.Level0Connectors.S, 1);
                }
                if(roomsPlacement[x+1][y]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x + 1,y), Level0Room.Level0Connectors.N, 1);
                }
                if(roomsPlacement[x][y-1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y - 1), Level0Room.Level0Connectors.E, 1);
                }
                if(roomsPlacement[x][y+1]!=MapState.NULL){
                    lockRoomConnector(new DiscreteCoordinates(x,y + 1), Level0Room.Level0Connectors.W, 1);
                }
            }
        }
    }

    /**
     * generate a fixed non-random map depending on which type of map do we want.
     */
    @Override
    public void generateFixedMap() {
        generateMap2();
    }
}