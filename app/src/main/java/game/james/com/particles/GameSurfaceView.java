package game.james.com.particles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

import game.james.com.particles.gameobjects.GameObject;
import game.james.com.particles.gameobjects.Particle;

public class GameSurfaceView extends SurfaceView implements Runnable,View.OnTouchListener {
    private boolean isRunning = false;
    private Thread gameThread;
    private SurfaceHolder holder;

    public static int screenWidth;
    public static int screenHeight;
    private int touchX,touchY;

    //private Sprite[] sprites;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<Particle> particles = new ArrayList<>();

    private final static int MAX_FPS = 40; //desired fps
    private final static int FRAME_PERIOD = 1000 / MAX_FPS; // the frame period

    public GameSurfaceView(Context context) {
        super(context);

        setOnTouchListener(this);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                screenWidth = width;
                screenHeight = height;
                spawn(1000);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        gameObjects.add(new GameObject(100,100,100));
    }

    public void spawn(int amount)
    {
        for(int i = 0;i<amount;i++)
            particles.add(new Particle((int)(Math.random()*screenWidth),(int)(Math.random()*screenHeight),(int)(Math.random()*Math.random()*Math.random()*5)+3));
    }


    /**
     * Start or resume the game.
     */
    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Pause the game loop
     */
    public void pause() {
        isRunning = false;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    protected void step()
    {
        for(int i=0;i<gameObjects.size();i++)
        {
            gameObjects.get(i).step();
        }
        calculateParticleMoves();
        Log.d("num particles",": "+particles.size());
    }

    public void calculateParticleMoves()
    {
        /*for (Particle p : particles)
            p.move(mouseX,mouseY);*/

        for(int i = 0;i<particles.size();i++)
        {
            particles.get(i).move(touchX,touchY);
        }
    }

  /*protected void render(Canvas canvas) {
    canvas.drawColor(Color.BLACK);
    for (int index = 0, length = sprites.length; index < length; index++) {
      Paint p = null;
      if (sprites[index].color != 0) {
        p = new Paint();
        ColorFilter filter = new LightingColorFilter(sprites[index].color, 0);
        p.setColorFilter(filter);
      }

      canvas.drawBitmap(sprites[index].image, sprites[index].x, sprites[index].y, p);
    }
  }*/


    protected void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        for (int index = 0; index < gameObjects.size(); index++) {
            gameObjects.get(index).drawGameObject(canvas);
        }

        drawParticles(canvas);
    }

    public void drawParticles(Canvas g)
    {
        for(int i=0;i<particles.size();i++)
            particles.get(i).drawParticle(g);
    }

    @Override
    public void run() {
        while(isRunning) {
            // We need to make sure that the surface is ready
            if (! holder.getSurface().isValid()) {
                continue;
            }
            long started = System.currentTimeMillis();

            // update
            step();
            // draw
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                render(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            float deltaTime = (System.currentTimeMillis() - started);
            int sleepTime = (int) (FRAME_PERIOD - deltaTime);
            if (sleepTime > 0) {
                try {
                    gameThread.sleep(sleepTime);
                }
                catch (InterruptedException e) {
                }
            }
            while (sleepTime < 0) {
                step();
                sleepTime += FRAME_PERIOD;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchX=(int)event.getX();
        touchY=(int)event.getY();
        return false;
    }
}
