package com.anythru.goupstairs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;

public class AssetFileParser {
	
	Context context;
	
	public AssetFileParser(Context context, String fileName){
		
		this.context = context;
		parseFloorsFromFile(fileName);
	}
	
	public boolean parseFloorsFromFile(String fileName){
		InputStream is;
		InputStreamReader isReader;
		BufferedReader bufferedReader;
		ArrayList<Floor> floorArray = new ArrayList<Floor>();
		try {
			is = context.getApplicationContext().getAssets().open(fileName);
			isReader =  new InputStreamReader(is);
			bufferedReader = new BufferedReader(isReader);
			String line = "";
			while((line = bufferedReader.readLine()) != null){
				if(line.startsWith("=>")){
					continue;
				}
				String[] floorSpec = line.split(";");
				floorArray.add(new Floor(Integer.valueOf(floorSpec[0]), Integer.valueOf(floorSpec[1]), Integer.valueOf(floorSpec[2]), Integer.valueOf(floorSpec[3]), Integer.valueOf(floorSpec[4])));
			}
			new FloorFactory(150).setFloorsData(floorArray);
			is.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String[][] getFloorsInRange(int start_Y, int end_Y){
		
		return null;
	}
	
}
