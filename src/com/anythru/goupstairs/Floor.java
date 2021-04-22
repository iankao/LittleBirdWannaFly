package com.anythru.goupstairs;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

import android.os.Parcel;
import android.os.Parcelable;

public class Floor implements Serializable {
    
	public static final int FLOOR_TYPE_GROUND = 1000;
    public static final int FLOOR_TYPE_NORMAL = 1001;
    public static final int FLOOR_TYPE_ACCELERATE_RIGHT = 1002;
    public static final int FLOOR_TYPE_ACCELERATE_LEFT = 1003;
    public static final int FLOOR_TYPE_SPRING = 1004;
    public static final int FLOOR_TYPE_SPRING_PRESSED = 1005;
    public static final int FLOOR_TYPE_HORIZONTAL_MOVING = 1006;
    public static final int FLOOR_TYPE_VERTICAL_MOVING = 1007;
    
    int x, origin_offset_y, widthFactor, heightFactor;
    int type;
    
    //variables only used for FLOOR_TYPE_HORIZONTAL_MOVING
    boolean isMovingRight;
    int originX;
    int maxOffset_X;
    
    //variables only used for FLOOR_TYPE_VERTICAL_MOVING
    boolean isMovingUp;
    int moving_offset_y;
    boolean isDeadFloor;
    boolean dirty = false;
    
    /**
     * 
     * @param x the start x (x_MovingUnit == Screen Width / 320)
     * @param origin_offset_y the y offset relate to the last floor
     * @param widthFactor the width(need to compute with width unit for real pixel)
     * @param heightFactor the height(need to compute with height unit for real pixel, must be even integer, or will make the offset one pixel swift)
     * @param type
     */
    Floor(int x, int origin_offset_y, int widthFactor, int heightFactor, int type){
    	this.x = x;
    	this.origin_offset_y = origin_offset_y;
    	this.widthFactor = widthFactor;
    	this.heightFactor = heightFactor;
    	this.type = type;
    }
    
    /**
     * Only used in floor type FLOOR_TYPE_VERTICAL_MOVING
     * 
     * @param x the start x (x_MovingUnit == Screen Width / 320)
     * @param origin_offset_y the y offset relate to the last floor(originally)
     * @param widthFactor the width(need to compute with width unit for real pixel)
     * @param heightFactor the height(need to compute with height unit for real pixel, must be even integer, or will make the offset one pixel swift)
     * @param type
     * @param isMovingUp see if the floor is moving up(only used in floor type FLOOR_TYPE_VERTICAL_MOVING)
     * @param moving_offset_y the offset which is related to original y-point
     * @param isDeadFloor if the floor is not moving anymore(when moving down out of the screen in a specific distance)
     */
    Floor(int x, int origin_offset_y, int widthFactor, int heightFactor, int type, boolean isMovingUp, int moving_offset_y, boolean isDeadFloor){
    	this.x = x;
    	this.origin_offset_y = origin_offset_y;
    	this.widthFactor = widthFactor;
    	this.heightFactor = heightFactor;
    	this.type = type;
    	this.isMovingUp = isMovingUp;
    	this.moving_offset_y = moving_offset_y;
    	this.isDeadFloor = isDeadFloor;
    }
    
    /**
     * Only used in floor type FLOOR_TYPE_HORIZONTAL_MOVING
     * 
     * @param x the start x (x_MovingUnit == Screen Width / 320)
     * @param origin_offset_y the y offset relate to the last floor
     * @param widthFactor the width(need to compute with width unit for real pixel)
     * @param heightFactor the height(need to compute with height unit for real pixel, must be even integer, or will make the offset one pixel swift)
     * @param type
     * @param isMovingRight see if the floor is moving right(only used in floor type FLOOR_TYPE_HORIZONTAL_MOVING)
     * @param originX the origin x position of the floor
     * @param maxOffset_X max moving offset of this floor
     */
    Floor(int x, int origin_offset_y, int widthFactor, int heightFactor, int type, boolean isMovingRight, int originX, int maxOffset_X){
    	this.x = x;
    	this.origin_offset_y = origin_offset_y;
    	this.widthFactor = widthFactor;
    	this.heightFactor = heightFactor;
    	this.type = type;
    	this.isMovingRight = isMovingRight;
    	this.originX = originX;
    	this.maxOffset_X = maxOffset_X;
    }

}
