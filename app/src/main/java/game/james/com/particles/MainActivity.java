package game.james.com.particles;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements View.OnTouchListener{
    protected GameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gameView = new GameSurfaceView(this);
        gameView.setOnTouchListener(this);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       // if(event.getAction()==MotionEvent.ACTION_MOVE)
        gameView.updateTouch((int)event.getX(),(int)event.getY());
        //touchX=(int)event.getX();
        //touchY=(int)event.getY();
        Log.d("motion", "test " + event.getAction());
        return false;
    }
}
