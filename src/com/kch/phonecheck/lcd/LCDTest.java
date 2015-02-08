package com.kch.phonecheck.lcd;

import com.kch.phonecheck.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * 
 */
public class LCDTest extends Activity implements OnGestureListener{	
	private GestureDetector gest;
	private int count=0;
	private FrameLayout lcd;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lcdtest);
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		lcd=(FrameLayout)findViewById(R.id.lcd);
		gest=new GestureDetector(this);
		

	}
	
	@Override
    public boolean onTouchEvent(MotionEvent me) {
        return gest.onTouchEvent(me);
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		
		
		try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
 
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	setNextColor(++count);
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	if(count<2){
            		count=2;
            	}
            	setNextColor(--count);            	
            }
            // down to up swipe
            else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(), "Swipe up", Toast.LENGTH_SHORT).show();
            }
            // up to down swipe
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(), "Swipe down", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
 
        }
        return true;
	}
	
	public void setNextColor(int i){
		switch (i) {
		case 1:
			lcd.setBackgroundColor(Color.RED);
			break;
		case 2:
			lcd.setBackgroundColor(Color.GREEN);
			break;
		case 3:
			lcd.setBackgroundColor(Color.BLUE);
			break;
		case 4:
			lcd.setBackgroundColor(Color.YELLOW);
			break;		
		case 5:
			lcd.setBackgroundColor(Color.rgb(239, 61, 255));
			break;		
		case 6:
			lcd.setBackgroundColor(Color.rgb(255, 82, 157));
			break;			
		case 7:
			lcd.setBackgroundColor(Color.WHITE);
			break;		
		case 8:
			lcd.setBackgroundColor(Color.BLACK);
			break;
		default:
			finish();
			break;
		}
	}
}
