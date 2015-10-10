package game.james.com.particles.gameobjects;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by James on 10/10/2015.
 */
public class GameObject {

    private int x,y;
    private int size;
    private Paint paint = new Paint();

    public GameObject(int x,int y,int size)
    {
        this.x=x;
        this.y=y;
        this.size=size;
        paint.setColor(Color.rgb((int)(Math.random()*155+100),(int)(Math.random()*100+155),(int)(Math.random()*155+100)));
    }

    public void drawGameObject(Canvas canvas)
    {
        canvas.drawRect(x,y,x+size,y+size,paint);
    }

    public void step()
    {
        x++;
    }



}
