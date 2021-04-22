package com.anythru.goupstairs;

import com.google.android.gms.ads.*;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements OnTouchListener {

	CharacterSurfaceView characterSurfaceView;
	Thread gatherPowerThread;
	int screenWidth;
	int screenHeight;
	
	FrameLayout surface_view_fl;
	
	final static int GOING = 1001;
	final static int PAUSE = 1002;
	final static int GAMEOVER = 1003;
	final static int PAUSED_IN_BACKGROUND = 1004;
	
	static Bundle pauseBundle = new Bundle();
	
	static int mode = GOING;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//new AssetFileParser(this, "floors_default.txt");
		screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		screenHeight =  ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		
		setContentView(R.layout.activity_main);
		
		final AdView adView = (AdView)this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest.Builder().build());
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				characterSurfaceView = new CharacterSurfaceView(MainActivity.this, adView.getHeight());
				characterSurfaceView.setOnTouchListener(MainActivity.this);
				
				surface_view_fl = (FrameLayout) findViewById(R.id.surface_view_fl);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				surface_view_fl.addView(characterSurfaceView, params);
			}
		}, 200);
		
		//setContentView(characterSurfaceView);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		pauseBundle = characterSurfaceView.pauseAndSaveData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mode == PAUSED_IN_BACKGROUND){
			characterSurfaceView = new CharacterSurfaceView(this, 0);
			characterSurfaceView.setOnTouchListener(this);
			characterSurfaceView.resumeFromSavedState(pauseBundle);
			surface_view_fl.removeAllViews();
			surface_view_fl.addView(characterSurfaceView);
			//setContentView(characterSurfaceView);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(mode == GAMEOVER){
			if((event.getX() > (screenWidth * 4 / 5) && event.getX() < screenWidth) && event.getY() > 0 && event.getY() < (screenWidth / 5)){
			    
			    System.exit(0);
			}
			else
			{
				mode = GOING;
                characterSurfaceView.resumeFromGameOver();
			}
		} else if(mode == PAUSED_IN_BACKGROUND){
			mode = GOING;
			return false;
		}
				
		
		if(event.getX()>(screenWidth * 0.85f) && event.getY() < (screenWidth * 0.2f)){
			if(mode == GOING)
				mode = PAUSE;
			else if(mode == PAUSE)
				mode = GOING;
			return false;
		}
		
		if(event.getX()>(screenWidth * 0.7f) && event.getX()<(screenWidth * 0.85f) && event.getY() < (screenWidth * 0.2f)){
			System.exit(0);
//			finish();
		}
		
		if(characterSurfaceView.manIsJumping || characterSurfaceView.manIsFalling){
			PowerController.onPause();
			return false;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN){

			gatherPowerThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					PowerController.startToGatherPower();
				}
			});
			gatherPowerThread.setName("gatherPowerThread");
			gatherPowerThread.start();

		}
		else if(event.getAction() == MotionEvent.ACTION_UP){

			characterSurfaceView.jumpByPower(PowerController.getPowerValue());
			PowerController.resetPowerToDefault();

		}
		return true;
	}

}
