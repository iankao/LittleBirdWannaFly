package com.anythru.goupstairs;


import java.util.ArrayList;
import java.util.Random;


public class FloorFactory {
    
    private static ArrayList<Floor> floorArray;
    private int screenHeight;
    /**
     * the equally y-offset in each floor
     */
    private static int eachDis = 50;
    
    private static int heightFactor = 20;
    /**
     * the y-offset of ground(first floor)
     */
    private int groundOffset = 20;
    
    private static int[] basicFourFloorPosition = {85, 221, 0, 136};
    
    public FloorFactory(int screenHeight){
    	this.screenHeight = screenHeight;
        initFloorArray();
        
    }
    
    public void setFloorsData(ArrayList floorArray){
    	
    	this.floorArray = floorArray; 
    	
    }
    
    public ArrayList<Floor> getFloorsSpec(){
    	
    	return this.floorArray;
    	
    }
    
    public int getEachFloorDis(){
        return this.eachDis;
    }
    
    public ArrayList<Floor> onFloorArrayChange(ArrayList<Floor> oldFloorArray){
    	
        
    	
    	return floorArray;
    }
    
    /**
     * When first render the base four floors,
     * initial the floorArray
     * @param screenHeight The height of the screen, parse it in then we can generate the floors properly as we want
     */
    private void initFloorArray()
    {
        floorArray = new ArrayList<Floor>();
        floorArray.add(0, new Floor(0, this.groundOffset, 3, 8, Floor.FLOOR_TYPE_GROUND)); // The first floor(ground)
        
        // Render a set of unchangeable floors, for test
//        Floor[] floors = renderFixedFloorSet();
//        for(Floor floor:floors){
//            floorArray.add(floor);
//        }
        int totalHeight = this.groundOffset;
        while(totalHeight < this.screenHeight + 0 * eachDis){
            // Render a basic set
            Floor[] floors = renderBasicFloorSet(1);
            for(Floor floor:floors){
                floorArray.add(floor);
            }
            totalHeight += 4 * eachDis;
        }
        //floorArray.add(new Floor(0, 400, 3, 62, Floor.FLOOR_TYPE_GROUND)); // The far far away floor(Ceiling)

    }
    
    /**
     * render the base four floor
     * 
     * @param difficulty the param to difine offset of the floor by how high now you are
     * 
     * @return
     */
    private static synchronized Floor[] renderBasicFloorSet(int difficulty)
    {
        Floor[] baseFloorSet = new Floor[4];
//        baseFloorSet[0] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_VERTICAL_MOVING, true, 0, false);
//        //baseFloorSet[0] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);
//        baseFloorSet[1] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_VERTICAL_MOVING, false, 0, false);
//        //baseFloorSet[1] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
//        baseFloorSet[2] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[3] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
        for(int counter = 0;counter < 4;counter++){
        	baseFloorSet[counter] = getRandomFloor(counter, difficulty);
        }
        return baseFloorSet;
    }
    /**
     * add a set of four basic floors
     * 
     * @param floors
     * @param difficulty The param to difine offset of the floor by how high now you are 
     * @return
     */
    public static synchronized ArrayList<Floor> renderNewFloorSet(ArrayList<Floor> floors, int difficulty){
        Floor[] newFloorSet = renderBasicFloorSet(difficulty);
        for (Floor floor : newFloorSet) {
            floors.add(floor);
        }
        
        return floors;
    }
    
    public static synchronized ArrayList<Floor> removeFloorIndex0(ArrayList<Floor> floors){
    	floors.remove(0);
    	return floors;
    }
    
    /**
     * Get a random floor
     * 
     * @param position 0~3
     * @param offset_by_difficulty The param to difine offset of the floor by how high now you are
     * 
     * @return
     */
    public static Floor getRandomFloor(int position, int offset_by_difficulty){
    	Floor floor;
    	Random random = new Random();
    	Random difficulty_random = new Random();
    	if((random.nextInt(8)) == 1){
    		floor = new Floor(basicFourFloorPosition[position] + difficulty_random.nextInt(offset_by_difficulty),eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
    	}
    	else if((random.nextInt(8)) == 2){
    		floor = new Floor(basicFourFloorPosition[position] + difficulty_random.nextInt(offset_by_difficulty),eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
    	}
    	else if((random.nextInt(8)) == 3){
    		floor = new Floor(basicFourFloorPosition[position] + difficulty_random.nextInt(offset_by_difficulty),eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
    	}
    	else if((random.nextInt(8)) == 4){
    		floor = new Floor(basicFourFloorPosition[position] + difficulty_random.nextInt(offset_by_difficulty) - (offset_by_difficulty / 2),eachDis,1,heightFactor,Floor.FLOOR_TYPE_HORIZONTAL_MOVING, true, basicFourFloorPosition[position], 16);
    	}
    	else if((random.nextInt(8)) == 5){
    		floor = new Floor(basicFourFloorPosition[position] + difficulty_random.nextInt(offset_by_difficulty) - (offset_by_difficulty / 2),eachDis,1,heightFactor,Floor.FLOOR_TYPE_VERTICAL_MOVING, true, 0, false);
    	}
    	else{
    		floor = new Floor(basicFourFloorPosition[position] + difficulty_random.nextInt(offset_by_difficulty) - (offset_by_difficulty / 2),eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);
    	}
    	
    	return floor;
    }
    
    /**
     * Render a set of unchangeable floors, for test
     * 
     * @return
     */
    private Floor[] renderFixedFloorSet()
    {
        Floor[] baseFloorSet = new Floor[28];
        baseFloorSet[0] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_VERTICAL_MOVING, true, 0, false);
        //baseFloorSet[0] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);
        baseFloorSet[1] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_VERTICAL_MOVING, true, 0, false);
        //baseFloorSet[1] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[2] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[3] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
        baseFloorSet[4] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_HORIZONTAL_MOVING, true, 85, 22);
        //baseFloorSet[5] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_VERTICAL_MOVING, true, 0);
        baseFloorSet[5] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[6] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[7] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
        baseFloorSet[8] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);
        baseFloorSet[9] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[10] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[11] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_HORIZONTAL_MOVING, true, 136, 16);
        baseFloorSet[12] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[13] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
        baseFloorSet[14] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[15] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);
        baseFloorSet[16] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_HORIZONTAL_MOVING, true, 85, 22);
        baseFloorSet[17] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[18] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[19] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
        baseFloorSet[20] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);
        baseFloorSet[21] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[22] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[23] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_HORIZONTAL_MOVING, true, 136, 16);
        baseFloorSet[24] = new Floor(85,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_RIGHT);
        baseFloorSet[25] = new Floor(221,eachDis,1,heightFactor,Floor.FLOOR_TYPE_SPRING);
        baseFloorSet[26] = new Floor(0,eachDis,1,heightFactor,Floor.FLOOR_TYPE_ACCELERATE_LEFT);
        baseFloorSet[27] = new Floor(136,eachDis,1,heightFactor,Floor.FLOOR_TYPE_NORMAL);

        return baseFloorSet;
    }
    
}
