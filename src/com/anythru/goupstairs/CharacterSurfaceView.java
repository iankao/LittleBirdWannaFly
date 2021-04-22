package com.anythru.goupstairs;

import java.util.ArrayList;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CharacterSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	SurfaceHolder holder;
	Canvas canvas;
	Context mContext;
	Paint mPaint = new Paint();
	private static Bitmap character;
	private static Bitmap character_left;
	private static Bitmap character_right;
	private static Bitmap floor_ground;
	private static Bitmap floor_normal_bitmap;
	private static Bitmap floor_acc_right_bitmap;
	private static Bitmap floor_acc_left_bitmap;
	private static Bitmap floor_srping;
	private static Bitmap floor_srping_pressed;
	private static Bitmap power_bar_background;
	private static Bitmap power_outer;
	private static Bitmap background;
	private static Bitmap gameover_background;
	private static Bitmap pause_button;
	private static Bitmap stop_button;
	private static Bitmap replay_button;
	private static Bitmap exit_button;
	private static Bitmap resume_button;
	private static Bitmap resume_button_bigger;
	private static Bitmap top5_label;
	
	private static Bitmap number_0;
	private static Bitmap number_1;
	private static Bitmap number_2;
	private static Bitmap number_3;
	private static Bitmap number_4;
	private static Bitmap number_5;
	private static Bitmap number_6;
	private static Bitmap number_7;
	private static Bitmap number_8;
	private static Bitmap number_9;
    private static Bitmap number_0_small;
    private static Bitmap number_1_small;
    private static Bitmap number_2_small;
    private static Bitmap number_3_small;
    private static Bitmap number_4_small;
    private static Bitmap number_5_small;
    private static Bitmap number_6_small;
    private static Bitmap number_7_small;
    private static Bitmap number_8_small;
    private static Bitmap number_9_small;
	
	private boolean goRight = true;
	
	private int velocity = 10;
	
	private int accelerator = 0;
	
	public static float floor_horizontal_moving_velocity;
	
	public static float floor_vertical_moving_velocity;
	/**
	 * it stand for "degree"
	 */
	private double jumpingFactor = 0;
	private int powerFactor = 1;
	private int powerAccelerator = 4;
	/**
	 * 往上彈跳的擴大係數
	 */
	//private int deltaYFactor = 9;
	private final int frame_rate = 50;
	
	public static float x_MovingUnit;
	
	public static int x;
	/**
	 * the width of the screen
	 */
	public static int screenWidth;
	/**
	 * the height of the screen
	 */
	public static int screenHeight;
	/**
	 * 最高紀錄
	 */
	public static int currentRecord = 0;
	
	float number_1_x = 0;
	float number_2_x;
	float number_3_x;
	float number_1_y;
	float number_1_x_records;
	float number_2_x_records;
    float number_3_x_records;
    
	/**
	 * The ground Y position
	 */
	public static int baseLineY = 600;
	/**
	 * the next y position the man is about to when redraw
	 */
	public static int y = 600;
	/**
	 * Delta Y, when the man reach the notify-line, start to redraw floors with dY
	 */
	public static int floors_move_delta_Y = 0;
	/**
	 * For the vertical moving floor, recode old offset, then next round can get the 偏移 of the last round
	 */
	public static int old_moving_offset_y_of_current_floor = 0;
	/**
	 * the distance to baseline in y-axis
	 */
	public static int distance_to_baseline_Y;
	/**
	 * the Y moving up unit, this value can also used to define the floor y position
	 */
	public static int y_MovingUnit;
	/**
	 * the max vertical movement of vertical moving floor(half of each floor distance)
	 */
	public static int maxVerticalMovement;
	/**
	 * The y value of current floor man is standing on
	 */
	public static int currentFloor_Y = 600;
	public static int currentFloor_X_start;
	public static int currentFloor_X_end;
	/**
	 * When over this line, start to change canvas
	 */
	public static int notifyChangeLine;
	/**
	 * the floor width depends on the screen width
	 */
	public static int floorWidth;
	/**
	 * the denominator to divide screen width to floor width
	 */
	public static int denominator = 3;
	/**
	 * the index of the current floor, used to catch the arraylist object
	 */
	public static int currentFloorIndex;

	public static boolean manIsRunning = true;
	public static boolean manIsJumping = false;
	public static boolean manIsFalling = false;
	
	Thread characterThread;
	
	private static FloorFactory floorFactory;
	/**
	 * the Array of Floors which should be shown within view
	 */
	private static ArrayList<Floor> floorArray = new ArrayList<Floor>();
	/**
	 * counter of floors driven under the baseline, when reach 4, render a new set on top
	 */
	private static int drivenFloorCount = 1;
	/**
	 * the difficulty that will difine the offset of the floors
	 */
	private static int difficulty = 1;
	
	private boolean DEBUG = true;
	
	public CharacterSurfaceView(Context context, int baseLine) {
		super(context);
		mContext = context;
		holder = getHolder();
		holder.addCallback(this);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //baseLineY = wm.getDefaultDisplay().getHeight();
		baseLineY = wm.getDefaultDisplay().getHeight() - baseLine; 
        floorWidth = wm.getDefaultDisplay().getWidth() / denominator;
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        x_MovingUnit = Float.valueOf(Math.round(wm.getDefaultDisplay().getWidth() * 10 / 320)) / 10;
		y_MovingUnit = wm.getDefaultDisplay().getHeight() / 480;
		floor_vertical_moving_velocity = 1;
		
		notifyChangeLine = (int) (wm.getDefaultDisplay().getHeight() * 0.45);
		velocity = wm.getDefaultDisplay().getWidth() / 60;
		
		new BitmapFactory();
		
        character_left =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.running_bird_left), wm.getDefaultDisplay().getWidth() / 8, wm.getDefaultDisplay().getWidth() / 8, true);
        character_right =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.running_bird_right), wm.getDefaultDisplay().getWidth() / 8, wm.getDefaultDisplay().getWidth() / 8, true);
        character = character_left;
        
        floor_ground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor_ground_001), wm.getDefaultDisplay().getWidth(), floorWidth / 4, true);
        floor_normal_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor_normal_002), floorWidth, floorWidth / 8, true);
        floor_acc_right_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor_acc_right_002), floorWidth, floorWidth / 8, true);
        floor_acc_left_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor_acc_left_002), floorWidth, floorWidth / 8, true);
        floor_srping = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor_spring_002), floorWidth, floorWidth / 8, true);
        floor_srping_pressed = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor_spring_pressed_002), floorWidth, floorWidth / 16, true);
        power_bar_background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.power_bar_background_002), wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getWidth() / 7, true);
        power_outer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.power_outer_001), (int) (wm.getDefaultDisplay().getWidth() / 1.9), wm.getDefaultDisplay().getWidth() / 15, true);
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background_cloud_002), screenWidth, wm.getDefaultDisplay().getHeight(), true);
        gameover_background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gameover_background_001), screenWidth, wm.getDefaultDisplay().getHeight(), true);
        pause_button = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pause_002), screenWidth / 9, screenWidth / 9, true);
        stop_button = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stop_002), screenWidth / 9, screenWidth / 9, true);
        replay_button = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.replay_002), screenWidth / 4, screenWidth / 4, true);
        exit_button = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.exit_001), screenWidth / 5, screenWidth / 5, true);
        resume_button = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.resume_001), screenWidth / 9, screenWidth / 9, true);
        resume_button_bigger = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.resume_001), screenWidth / 4, screenWidth / 4, true);
        top5_label = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.top5_002), screenWidth / 2, (int) (screenWidth / 3.2f), true);
        
        
        number_0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number0_001), screenWidth / 8, screenWidth / 4, true);
        number_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number1_001), screenWidth / 8, screenWidth / 4, true);
        number_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number2_001), screenWidth / 8, screenWidth / 4, true);
        number_3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number3_001), screenWidth / 8, screenWidth / 4, true);
        number_4 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number4_001), screenWidth / 8, screenWidth / 4, true);
        number_5 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number5_001), screenWidth / 8, screenWidth / 4, true);
        number_6 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number6_001), screenWidth / 8, screenWidth / 4, true);
        number_7 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number7_001), screenWidth / 8, screenWidth / 4, true);
        number_8 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number8_001), screenWidth / 8, screenWidth / 4, true);
        number_9 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number9_001), screenWidth / 8, screenWidth / 4, true);
        
        number_0_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number0_001), screenWidth / 12, screenWidth / 6, true);
        number_1_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number1_001), screenWidth / 12, screenWidth / 6, true);
        number_2_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number2_001), screenWidth / 12, screenWidth / 6, true);
        number_3_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number3_001), screenWidth / 12, screenWidth / 6, true);
        number_4_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number4_001), screenWidth / 12, screenWidth / 6, true);
        number_5_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number5_001), screenWidth / 12, screenWidth / 6, true);
        number_6_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number6_001), screenWidth / 12, screenWidth / 6, true);
        number_7_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number7_001), screenWidth / 12, screenWidth / 6, true);
        number_8_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number8_001), screenWidth / 12, screenWidth / 6, true);
        number_9_small = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.number9_001), screenWidth / 12, screenWidth / 6, true);
        
        
        number_1_x = 0;
    	number_2_x = screenWidth * 9 / 48;
    	number_3_x = screenWidth * 18 / 48;
    	number_1_y = screenWidth / 6;
        
    	number_1_x_records = screenWidth * 40 / 72;
        number_2_x_records = screenWidth * 49 / 72;
        number_3_x_records = screenWidth * 58 / 72;
    	
        resetParams();
        
//        floorFactory = new FloorFactory(baseLineY);
//		floorArray = floorFactory.getFloorsSpec();
//		distance_to_baseline_Y = floorArray.get(0).origin_offset_y * y_MovingUnit;
//		currentFloor_Y =  baseLineY - distance_to_baseline_Y;
//		y = currentFloor_Y  - character.getHeight();
//		maxVerticalMovement = floorFactory.getEachFloorDis() / 2;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(MainActivity.mode == MainActivity.PAUSED_IN_BACKGROUND){
			return;
		}
		
		if(characterThread == null || characterThread.getState() == Thread.State.TERMINATED){
			characterThread = new Thread(this);
		}
		characterThread.setName("characterThread");
		characterThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			characterThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Bundle pauseAndSaveData(){
		MainActivity.mode = MainActivity.PAUSED_IN_BACKGROUND;
		String[] positionArray = {String.valueOf(baseLineY), String.valueOf(x), String.valueOf(y), String.valueOf(currentFloorIndex), 
				String.valueOf(currentFloor_Y), String.valueOf(distance_to_baseline_Y), String.valueOf(goRight)
				, String.valueOf(manIsJumping), String.valueOf(manIsFalling), String.valueOf(jumpingFactor)};
		
		Bundle bundle = new Bundle();
		bundle.putStringArray("ObjectsPosition", positionArray);
		bundle.putSerializable("FloorArray", floorArray);
		// Set these three params to false then can let stop the thread.
		manIsRunning = false;
		manIsJumping = false;
		manIsFalling = false;
		
		return bundle;
	}
	
	public void resumeFromSavedState(Bundle savedState){
		String[] savedData = (String[]) savedState.get("ObjectsPosition");
		ArrayList<Floor> savedfloorArray = (ArrayList<Floor>) savedState.get("FloorArray");
		restoreState(savedData, savedfloorArray);
		if(characterThread == null || characterThread.getState() == Thread.State.TERMINATED){
			//resetParams();
			characterThread = new Thread(this);
			characterThread.start();
		}
	}
	
	public void resumeFromGameOver(){
		currentRecord = 0;
		
		if(characterThread == null || characterThread.getState() == Thread.State.TERMINATED){
			resetParams();
			characterThread = new Thread(this);
			characterThread.start();
		}
	}
	
	private void resetParams(){
		x = 0;
		currentFloorIndex = 0;
		floorFactory = new FloorFactory(baseLineY);
		floorArray = floorFactory.getFloorsSpec();
		distance_to_baseline_Y = floorArray.get(0).origin_offset_y * y_MovingUnit;
		currentFloor_Y =  baseLineY - distance_to_baseline_Y;
		y = currentFloor_Y  - character.getHeight();
		currentFloor_X_start = 0;
		currentFloor_X_end = screenWidth;
		maxVerticalMovement = floorFactory.getEachFloorDis() / 2;
		manIsRunning = true;
		if(MainActivity.mode != MainActivity.PAUSED_IN_BACKGROUND)
			MainActivity.mode = MainActivity.GOING;
	}
	
	private void restoreState(String[] position, ArrayList<Floor> savedFloorArray){
		baseLineY = Integer.valueOf(position[0]);
		x = Integer.valueOf(position[1]);
		y = Integer.valueOf(position[2]);
		currentFloorIndex = Integer.valueOf(position[3]);
		currentFloor_Y = Integer.valueOf(position[4]);
		distance_to_baseline_Y = Integer.valueOf(position[5]);
		goRight = Boolean.valueOf(position[6]);
		manIsJumping = Boolean.valueOf(position[7]);
		manIsFalling = Boolean.valueOf(position[8]);
		jumpingFactor = Double.valueOf(position[9]);
		
		floorArray = savedFloorArray;
		
		currentFloor_X_start = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit);
		currentFloor_X_end = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit) + ( floorWidth * floorArray.get(currentFloorIndex).widthFactor);
	}

	@Override
	public void run() {
		canvas = null;
		
		while(manIsRunning){
			
			if(MainActivity.mode != MainActivity.GOING){
				if(MainActivity.mode == MainActivity.PAUSED_IN_BACKGROUND){
					
					if(!holder.getSurface().isValid()) continue;
					canvas = holder.lockCanvas();
					drawResume(canvas);
					holder.unlockCanvasAndPost(canvas);
					try {
						Thread.sleep(frame_rate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				continue;
			}
			
			while(manIsJumping){
				
				if(MainActivity.mode != MainActivity.GOING){
					continue;
				}
				
				if(!holder.getSurface().isValid()) continue;
				accelerator = 0;
				canvas = holder.lockCanvas();
				if(goRight){ 
					x += velocity;
					if(x > canvas.getWidth() - character.getWidth()) goRight = false;
					
				}
				else {
					x -= velocity;
					if(x < 1) goRight = true;
					
				}
				
				if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_SPRING_PRESSED){
					floorArray.get(currentFloorIndex).type = Floor.FLOOR_TYPE_SPRING;
					floorArray.get(currentFloorIndex).origin_offset_y += floorArray.get(currentFloorIndex).heightFactor;
					floorArray.get(currentFloorIndex + 1).origin_offset_y -= floorArray.get(currentFloorIndex).heightFactor;
					currentFloor_Y -= floorArray.get(currentFloorIndex).heightFactor * y_MovingUnit;
					floorArray.get(currentFloorIndex).heightFactor /= 0.5;
					
				}
				
				int former_jump_Y = (int)(powerFactor * powerAccelerator * y_MovingUnit * Math.sin(Math.toRadians(jumpingFactor)));
				if(powerFactor < 15){
					jumpingFactor += 30;
				} else {
					jumpingFactor += 10;
				}
				int delta_Y = (int)(powerFactor * powerAccelerator * y_MovingUnit * Math.sin(Math.toRadians(jumpingFactor))) - former_jump_Y;
			    
			    if(jumpingFactor == 90){
					manIsJumping = false;
					manIsFalling = true;
					if(y <= notifyChangeLine)
					{
						// when finishing jumping, relocate nowPositionY
						floors_move_delta_Y = delta_Y;
					}
					else
					{
						// when finishing jumping, relocate nowPositionY
						y -= delta_Y;
					}
				}
			    else
			    {
			    	// when y is over the line, the whole canvas start to change in the mean time
			    	if(y <= notifyChangeLine)
			    	{
			    		floors_move_delta_Y = delta_Y;
			    		//notifyFloorsChange();
			    	} 
			    	else
			    	{
			    		y -= delta_Y;
			    	}
			    	
			    }
			    
			    // if the floor type is Floor.FLOOR_TYPE_VERTICAL_MOVING, current floor y should re-check each time
			    if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_VERTICAL_MOVING){
			    	currentFloor_Y += (old_moving_offset_y_of_current_floor - floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
			    	old_moving_offset_y_of_current_floor = floorArray.get(currentFloorIndex).moving_offset_y;
			    }
			    
			    // when the y  position is upper than the nearest floor-y
			    if(y + character.getHeight() < currentFloor_Y - (floorArray.get(currentFloorIndex + 1).origin_offset_y + floorArray.get(currentFloorIndex + 1).moving_offset_y) * y_MovingUnit){
			    	currentFloorIndex ++;
			    	// 跳高過一個未被踩過的階梯，把dirty改成true,並且在最高紀錄+1
			    	if(!floorArray.get(currentFloorIndex).dirty){
			    		currentRecord++;
			    		floorArray.get(currentFloorIndex).dirty = true;
			    	}
			    	// record the old offset of the y movement last move, then I can use it in the next round to get the y-diff
			    	old_moving_offset_y_of_current_floor = floorArray.get(currentFloorIndex).moving_offset_y;
			        currentFloor_Y -= (floorArray.get(currentFloorIndex).origin_offset_y + floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
			        currentFloor_X_start = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit);
			        currentFloor_X_end = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit) + ( floorWidth * floorArray.get(currentFloorIndex).widthFactor);
			    }
			    
				reDraw(canvas);
				holder.unlockCanvasAndPost(canvas);
				try {
					Thread.sleep(frame_rate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
			while(manIsFalling){
                
				if(MainActivity.mode != MainActivity.GOING){
					continue;
				}
				
                if(!holder.getSurface().isValid()) continue;
                accelerator = 0;
                floors_move_delta_Y = 0;
                canvas = holder.lockCanvas();
                if(goRight){ 
                    x += velocity;
                    if(x > canvas.getWidth() - character.getWidth()) goRight = false;
                    
                }
                else {
                    x -= velocity;
                    if(x < 1) goRight = true;
                    
                }
                int former_falling_Y = (int)(powerFactor * powerAccelerator * y_MovingUnit * (Math.sin(Math.toRadians(jumpingFactor))));
                
                if(powerFactor<PowerController.powerDefault) powerFactor = PowerController.powerDefault;
                
                if(jumpingFactor == 180){
                    y += (int)(powerFactor * powerAccelerator * y_MovingUnit * (Math.sin(Math.toRadians(170)) - Math.sin(Math.toRadians(180))));
                }
                else {
                    jumpingFactor += 10;
                    y += former_falling_Y - (int)(powerFactor * powerAccelerator * y_MovingUnit * Math.sin(Math.toRadians(jumpingFactor)));
                }
                
                // finish the game when the man is not visible in the view
                if(y > baseLineY){
                	holder.unlockCanvasAndPost(canvas);
                	break;
                }
                
        	    if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_VERTICAL_MOVING){
        	    	currentFloor_Y += (old_moving_offset_y_of_current_floor - floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
        	    	old_moving_offset_y_of_current_floor = floorArray.get(currentFloorIndex).moving_offset_y;
        	    }
                
                // Falling below the current floor y-position
                if(y + character.getHeight() >= currentFloor_Y){
                   
                	if(x + character.getWidth() > currentFloor_X_start && x < currentFloor_X_end){ // within the floor range
                	    
                	    // when landing on the spring floor, make it pressed, make it half of the height,
                	    // and set its type to FLOOR_TYPE_SPRING_PRESSED, and redefine current_floor_y position
                	    if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_SPRING){
                	        floorArray.get(currentFloorIndex).heightFactor *= 0.5; 
                	        floorArray.get(currentFloorIndex).origin_offset_y -= floorArray.get(currentFloorIndex).heightFactor;
                	        floorArray.get(currentFloorIndex + 1).origin_offset_y += floorArray.get(currentFloorIndex).heightFactor;
                	        currentFloor_Y += floorArray.get(currentFloorIndex).heightFactor * y_MovingUnit;
                	        floorArray.get(currentFloorIndex).type = Floor.FLOOR_TYPE_SPRING_PRESSED;
                	        powerAccelerator = 10;
                	    }
                	    else{
                	        powerAccelerator = 4;
                	    }
                	    // within the floor x-range, landing on the floor
                	    if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_ACCELERATE_RIGHT)
                	        accelerator = 5;
                	    else if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_ACCELERATE_LEFT)
                	        accelerator = -5;
                	    
                	    if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_HORIZONTAL_MOVING)
                	    	floor_horizontal_moving_velocity = x_MovingUnit;
                	    else
                	    	floor_horizontal_moving_velocity = 0;
                	    
                		y = currentFloor_Y - character.getHeight();
                		currentFloor_X_start = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit);
        				currentFloor_X_end = currentFloor_X_start + ( floorWidth * floorArray.get(currentFloorIndex).widthFactor);
                        jumpingFactor = 0;
                        manIsFalling = false;
                	} 
                	else 
                	{
                		// when out of range, drop to lower stair
                		currentFloor_Y += (floorArray.get(currentFloorIndex).origin_offset_y + floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
                		currentFloorIndex --;
                		// record the old offset of the y movement last move, then I can use it in the next round to get the y-diff
    			    	old_moving_offset_y_of_current_floor = floorArray.get(currentFloorIndex).moving_offset_y;
                		currentFloor_X_start = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit);
        				currentFloor_X_end = currentFloor_X_start + ( floorWidth * floorArray.get(currentFloorIndex).widthFactor);
                	}
                }
                
                reDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(frame_rate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
			
			if(!holder.getSurface().isValid()) continue;
			
			canvas = holder.lockCanvas();
			
			if(y > baseLineY){
            	finishGame(canvas);
            	holder.unlockCanvasAndPost(canvas);
            	break;
            }
			
			if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_HORIZONTAL_MOVING){
				floor_horizontal_moving_velocity = floorArray.get(currentFloorIndex).isMovingRight ? x_MovingUnit : -x_MovingUnit;
			}
			
			if(goRight){ 
				x += velocity + accelerator + floor_horizontal_moving_velocity;
				
				if(x > canvas.getWidth() - character.getWidth()) 
					goRight = false;
			}
			else {
				x -= velocity - accelerator - floor_horizontal_moving_velocity;
				if(x < 1) goRight = true;
				
			}
			
			if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_HORIZONTAL_MOVING){
				currentFloor_X_start = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit);
				currentFloor_X_end = currentFloor_X_start + ( floorWidth * floorArray.get(currentFloorIndex).widthFactor);

			}
			// If the floor type is FLOOR_TYPE_VERTICAL_MOVING, need to change y position every time
			if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_VERTICAL_MOVING){
				currentFloor_Y += (old_moving_offset_y_of_current_floor - floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
				y += (old_moving_offset_y_of_current_floor - floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
				old_moving_offset_y_of_current_floor = floorArray.get(currentFloorIndex).moving_offset_y;
			}
			
			// if the man is running out of the floor range, start to fall
			if(x + character.getWidth() > currentFloor_X_start ^ x < currentFloor_X_end){
				
				if(floorArray.get(currentFloorIndex).type == Floor.FLOOR_TYPE_SPRING_PRESSED){
					floorArray.get(currentFloorIndex).type = Floor.FLOOR_TYPE_SPRING;
					floorArray.get(currentFloorIndex).origin_offset_y += floorArray.get(currentFloorIndex).heightFactor;
					floorArray.get(currentFloorIndex + 1).origin_offset_y -= floorArray.get(currentFloorIndex).heightFactor;
					currentFloor_Y -= floorArray.get(currentFloorIndex).heightFactor * y_MovingUnit;
					floorArray.get(currentFloorIndex).heightFactor /= 0.5;
				}
				
				currentFloor_Y += (floorArray.get(currentFloorIndex).origin_offset_y + floorArray.get(currentFloorIndex).moving_offset_y) * y_MovingUnit;
				currentFloorIndex --;
				// record the old offset of the y movement last move, then I can use it in the next round to get the y-diff
		    	old_moving_offset_y_of_current_floor = floorArray.get(currentFloorIndex).moving_offset_y;
				currentFloor_X_start = (int) (floorArray.get(currentFloorIndex).x  * x_MovingUnit);
				currentFloor_X_end = currentFloor_X_start + floorWidth * floorArray.get(currentFloorIndex).widthFactor;

				powerFactor = 18;
			    jumpingFactor = 90;
			    manIsFalling = true;
			}
			    
			reDraw(canvas);
			holder.unlockCanvasAndPost(canvas);
			try {
				Thread.sleep(frame_rate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void jumpByPower(int power){
		manIsJumping = true;
		powerFactor = power;
	}
	
	private void reDraw(Canvas canvas){
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
		drawFloors();
		canvas.drawBitmap(goRight ? character_right: character_left, x, y, null);
		drawPowerBar();
	}
	
	public void drawFloors(){
	    
	    distance_to_baseline_Y -= floors_move_delta_Y;
	    currentFloor_Y += floors_move_delta_Y;
	    if(distance_to_baseline_Y + (2 * floorFactory.getEachFloorDis() * y_MovingUnit) <= 0)
	    {
	    	floorArray = FloorFactory.removeFloorIndex0(floorArray);
	        //floorArray.remove(0);
	        // If the vertical moving floor is out of the range, change its status to dead, and stop moving it
	        if(floorArray.get(0).type == Floor.FLOOR_TYPE_VERTICAL_MOVING) 
	        	floorArray.get(0).isDeadFloor = true;
	        
	        drivenFloorCount ++;
	        this.difficulty++;
	        if(drivenFloorCount == 5){
	            floorArray = FloorFactory.renderNewFloorSet(floorArray, this.difficulty);
	            drivenFloorCount = 1;
	            
	            android.util.Log.d("loganTest","floorArray.size()::" + floorArray.size());
	        }
	        distance_to_baseline_Y += (floorArray.get(0).origin_offset_y + floorArray.get(0).moving_offset_y) * y_MovingUnit;
	        currentFloorIndex--;
	    }
		int drawTempY = baseLineY - distance_to_baseline_Y;
		
	    for(int i = 0;i < floorArray.size();i++){
	    	Floor floor = (Floor)floorArray.get(i);
	    	Bitmap floorBitmap = floor_normal_bitmap;
	    	switch(floor.type){
	    	case Floor.FLOOR_TYPE_GROUND:
	    		floorBitmap = floor_ground;
	    	    //mPaint.setColor(Color.BLACK);
                break;
	    	case Floor.FLOOR_TYPE_NORMAL:
	    		floorBitmap = floor_normal_bitmap;
	    	    //mPaint.setColor(Color.DKGRAY);
	            break;
	    	case Floor.FLOOR_TYPE_ACCELERATE_RIGHT:
	    		floorBitmap = floor_acc_right_bitmap;
	    	    //mPaint.setColor(Color.GREEN);
	            break;
	    	case  Floor.FLOOR_TYPE_ACCELERATE_LEFT:
	    		floorBitmap = floor_acc_left_bitmap;
                //mPaint.setColor(Color.BLUE);
                break;
	    	case Floor.FLOOR_TYPE_SPRING:
	    		floorBitmap = floor_srping;
                //mPaint.setColor(Color.RED);
                break;
	    	case Floor.FLOOR_TYPE_SPRING_PRESSED:
	    		floorBitmap = floor_srping_pressed;
                //mPaint.setColor(Color.RED);
                break;
	    	case Floor.FLOOR_TYPE_HORIZONTAL_MOVING:
	    		//mPaint.setColor(Color.BLACK);
	    		
	    		if(floor.isMovingRight) floor.x += x_MovingUnit;
	    		else floor.x -= x_MovingUnit;
	    		
	    		if(floor.x > floor.originX + floor.maxOffset_X) floor.isMovingRight = false;
	    		if(floor.x < floor.originX - floor.maxOffset_X) floor.isMovingRight = true;
                
	    		break;
	    	case Floor.FLOOR_TYPE_VERTICAL_MOVING:
	    		//mPaint.setColor(Color.BLACK);
	    		
	    		// If the vertical moving floor is dead, stop moving it
	    		if(floor.isDeadFloor)
	    			break;
	    		
	    		if(floor.isMovingUp) {
	    			floor.moving_offset_y += floor_vertical_moving_velocity;
	    			if(i+1 < floorArray.size())
	    				floorArray.get(i + 1).origin_offset_y -= floor_vertical_moving_velocity;
	    		}
	    		else if(!floor.isMovingUp){
	    			floor.moving_offset_y -= floor_vertical_moving_velocity;
	    			if(i+1 < floorArray.size())
	    				floorArray.get(i + 1).origin_offset_y += floor_vertical_moving_velocity;
	    		}
	    		if(floor.moving_offset_y >= maxVerticalMovement) 
	    			floor.isMovingUp = false;
	    		if(floor.moving_offset_y <= -maxVerticalMovement) 
	    			floor.isMovingUp = true;
	    		break;
	    	}
	    	
	    	if(i != 0)
	    		drawTempY -= (floor.origin_offset_y + floor.moving_offset_y) * y_MovingUnit;
	    	canvas.drawBitmap(floorBitmap, floor.x * x_MovingUnit, drawTempY, null);
	    	//canvas.drawRect(floor.x * x_MovingUnit, drawTempY, floor.x * x_MovingUnit + floorWidth * floor.widthFactor, drawTempY + floor.heightFactor * y_MovingUnit, mPaint);
	    }
	    // draw a line to observe notify where is the nofity line
	    mPaint.setColor(Color.RED);
//    	canvas.drawRect(0, notifyChangeLine - 2, 320 * x_MovingUnit, notifyChangeLine, mPaint);
    	
	}
	
	public void drawPowerBar(){
//		mPaint.setColor(Color.YELLOW);
//		canvas.drawRect(14 * y_MovingUnit, 14  * y_MovingUnit, floorWidth * 2 + 4, 62 * y_MovingUnit, mPaint);
		canvas.drawBitmap(power_bar_background, 0, 0, null);
		canvas.drawBitmap(pause_button, screenWidth * 0.85f, screenWidth / 75, null);
		canvas.drawBitmap(stop_button, screenWidth * 0.7f, screenWidth / 75, null);
		mPaint.setColor(Color.MAGENTA);
		canvas.drawRect(screenWidth * 0.05f, screenWidth / 24, (screenWidth * 0.05f + (screenWidth * 0.50f * PowerController.getPowerPercentageOnGathering() / 22)), screenWidth / 11, mPaint);
		canvas.drawBitmap(power_outer, screenWidth * 0.04f, screenWidth / 30, null);
		drawFloorAmount(canvas, String.format("%03d", currentRecord));
	}
	
	public void finishGame(Canvas canvas){
		manIsRunning = false;
		canvas.drawBitmap(gameover_background, 0, 0, null);
		Paint finishPaint = new Paint();
		finishPaint.setColor(Color.RED);
		finishPaint.setTextSize(canvas.getWidth() / 10);
		canvas.drawBitmap(exit_button, screenWidth * 4 / 5, 0, null);
		canvas.drawBitmap(replay_button, screenWidth / 8, (screenHeight * 4 / 5) - (screenWidth / 8), null);
		drawFloorRecordAtEnd(canvas, String.format("%03d", currentRecord));
		MainActivity.mode = MainActivity.GAMEOVER;
	}
	
	public void drawResume(Canvas canvas){
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(gameover_background, 0, 0, null);
		canvas.drawBitmap(resume_button_bigger, screenWidth *3 /8, (screenHeight / 2) - (screenWidth / 8), null);
	}
	
	public void drawFloorAmount(Canvas canvas, String amount){
		canvas.drawBitmap(decideNumber(Integer.valueOf(amount.charAt(0)), false), number_1_x, number_1_y, null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(amount.charAt(1)), false), number_2_x, number_1_y, null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(amount.charAt(2)), false), number_3_x, number_1_y, null);
		
	}
	
	public void drawFloorRecordAtEnd(Canvas canvas, String amount){
		canvas.drawBitmap(decideNumber(Integer.valueOf(amount.charAt(0)), false), number_1_x, number_1_y, null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(amount.charAt(1)), false), number_2_x, number_1_y, null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(amount.charAt(2)), false), number_3_x, number_1_y, null);
		
		saveRecordToDBAndDraw(currentRecord, canvas);
		
	}
	
	public Bitmap decideNumber(int number, boolean small){

		if(number == 48){
			return small? number_0_small:number_0;
		} else if(number == 49){
			return small? number_1_small:number_1;
		} else if(number == 50){
			return small? number_2_small:number_2;
		} else if(number ==51){
			return small? number_3_small:number_3;
		} else if(number == 52){
			return small? number_4_small:number_4;
		} else if(number == 53){
			return small? number_5_small:number_5;
		} else if(number == 54){
			return small? number_6_small:number_6;
		} else if(number == 55){
			return small? number_7_small:number_7;
		} else if(number == 56){
			return small? number_8_small:number_8;
		} else if(number == 57){
			return small? number_9_small:number_9;
		} else {
			return small? number_0_small:number_0;
		}
		
	}
	
	public void saveRecordToDBAndDraw(int record, Canvas canvas){
		
		SharedPreferences pref = mContext.getSharedPreferences("Game_Recorder", 0);
		
		if(!pref.contains("1st")){
			SharedPreferences.Editor editor = pref.edit();
			editor.putInt("1st", record);
			editor.putInt("2nd", 0);
			editor.putInt("3rd", 0);
			editor.putInt("4th", 0);
			editor.putInt("5th", 0);
			editor.commit();
		}
		else
		{
			int[] records = {pref.getInt("1st", 0), pref.getInt("2nd", 0)
					, pref.getInt("3rd", 0), pref.getInt("4th", 0), pref.getInt("5th", 0)};
			for(int i=0; i < 5;i++){
				
				SharedPreferences.Editor editor = pref.edit();
				
				if(record >= records[i]){
					switch(i){
						case 0:
							editor.putInt("1st", record);
							editor.putInt("2nd", records[0]);
							editor.putInt("3rd", records[1]);
							editor.putInt("4th", records[2]);
							editor.putInt("5th", records[3]);
							break;
						case 1:
							editor.putInt("2nd", record);
							editor.putInt("3rd", records[1]);
							editor.putInt("4th", records[2]);
							editor.putInt("5th", records[3]);
							break;
						case 2:
							editor.putInt("3rd", record);
							editor.putInt("4th", records[2]);
							editor.putInt("5th", records[3]);
							break;
						case 3:
							editor.putInt("4th", record);
							editor.putInt("5th", records[3]);
							break;
						case 4:
							editor.putInt("5th", record);
							break;
					}
					editor.commit();
					break;
				}	
				
			}
			
		}
		
		drawPastRecords(canvas);
		
	}
	
	private void drawPastRecords(Canvas canvas){
		
		
		canvas.drawBitmap(top5_label, 0, screenHeight * 0.3f, null);
		
		SharedPreferences pref = mContext.getSharedPreferences("Game_Recorder", 0);
		String record_1st = String.format("%03d", pref.getInt("1st", 0));
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_1st.charAt(0)), true), number_1_x_records, screenHeight * 0.34f, null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_1st.charAt(1)), true), number_2_x_records, screenHeight * 0.34f, null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_1st.charAt(2)), true), number_3_x_records, screenHeight * 0.34f, null);
		String record_2nd = String.format("%03d", pref.getInt("2nd", 0));
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_2nd.charAt(0)), true), number_1_x_records, (screenHeight * 0.34f) + (screenWidth / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_2nd.charAt(1)), true), number_2_x_records, (screenHeight * 0.34f) + (screenWidth / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_2nd.charAt(2)), true), number_3_x_records, (screenHeight * 0.34f) + (screenWidth / 6), null);
		String record_3rd = String.format("%03d", pref.getInt("3rd", 0));
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_3rd.charAt(0)), true), number_1_x_records, (screenHeight * 0.34f) + (screenWidth * 2 / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_3rd.charAt(1)), true), number_2_x_records, (screenHeight * 0.34f) + (screenWidth * 2 / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_3rd.charAt(2)), true), number_3_x_records, (screenHeight * 0.34f) + (screenWidth * 2 / 6), null);
		String record_4th = String.format("%03d", pref.getInt("4th", 0));
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_4th.charAt(0)), true), number_1_x_records, (screenHeight * 0.34f) + (screenWidth * 3 / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_4th.charAt(1)), true), number_2_x_records, (screenHeight * 0.34f) + (screenWidth * 3 / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_4th.charAt(2)), true), number_3_x_records, (screenHeight * 0.34f) + (screenWidth * 3 / 6), null);
		String record_5th = String.format("%03d", pref.getInt("5th", 0));
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_5th.charAt(0)), true), number_1_x_records, (screenHeight * 0.34f) + (screenWidth * 4 / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_5th.charAt(1)), true), number_2_x_records, (screenHeight * 0.34f) + (screenWidth * 4 / 6), null);
		canvas.drawBitmap(decideNumber(Integer.valueOf(record_5th.charAt(2)), true), number_3_x_records, (screenHeight * 0.34f) + (screenWidth * 4 / 6), null);
		
	}
	
}
