package com.anythru.goupstairs;

public class PowerController {
	
	private static int increasingVelocity = 2;
	private static int power = 18;
	private static boolean gathering = false;
	public static int powerMax = 40;
	public static int powerDefault = 18;
	
	PowerController(){
		
	}
	
	public static void startToGatherPower(){
		gathering = true;
		onGatherPower();
	}
	
	private static void onGatherPower(){
		power = powerDefault;
		gathering = true;
		while(gathering){
			power += increasingVelocity;
			if(power > powerMax)
				power = powerDefault;
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getPowerValue(){
		gathering = false;
		return power;
	}
	
	public static void onPause(){
		gathering = false;
		power = powerDefault;
	}
	
	public static float getPowerPercentageOnGathering(){
	    return power - powerDefault;
	}
	
	public static void resetPowerToDefault(){
		power = powerDefault;
	}
	
	public static void resumeGatherPower(){
		onGatherPower();
	}

}
