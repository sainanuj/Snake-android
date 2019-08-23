package iamanujsain.github.snake;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    Paint fillPaint;
    Rect r;
    static int dWidth, dHeight;
    Handler handler;
    Runnable runnable;

    Snake snake;
    Food food;

    private float previousX;
    private float previousY;

    public GameView(Context context) {
        super(context);

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.BLACK);
        
        Display display = ((Activity) getContext())
                .getWindowManager()
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;

        r = new Rect(0, 0, dWidth, dHeight);

        snake = new Snake();
        food = new Food(snake);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                update();
                invalidate();
            }
        };
    }

    void update() {
        snake.update();
        food.update();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(r, fillPaint);

        snake.draw(canvas);
        food.draw(canvas);

        handler.postDelayed(runnable, 1000/20);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                previousX = event.getX();
                previousY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                float deltaX = Math.abs(finalX - previousX);
                float deltaY = Math.abs(finalY - previousY);

                if (deltaX > deltaY) {
                    if (finalX > previousX) {
                        if (snake.getDx() != -1 && snake.isMoving()) {
                            snake.setDy(0);
                            snake.setDx(1);
                        }
                    } else if (finalX < previousX) {
                        if (snake.getDx() != 1 && snake.isMoving()) {
                            snake.setDy(0);
                            snake.setDx(-1);
                        }
                    }
                }

                if (deltaX < deltaY) {
                    if (finalY > previousY) {
                        if (snake.getDy() != -1 && snake.isMoving()) {
                            snake.setDx(0);
                            snake.setDy(1);
                        }
                    } else if (finalY < previousY) {
                        if (snake.getDy() != 1 && snake.isMoving()) {
                            snake.setDx(0);
                            snake.setDy(-1);
                        }
                    }
                }

                break;
        }
        return true;
    }
}
