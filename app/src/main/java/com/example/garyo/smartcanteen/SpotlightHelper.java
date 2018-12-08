package com.example.garyo.smartcanteen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.Shape;
import com.takusemba.spotlight.target.SimpleTarget;
/*
This is the spotlight helper class written by me

View documentation here
https://github.com/TakuSemba/Spotlight
 */
//todo stretch goal if have time can make a layout for the spotlight
public class SpotlightHelper {
    public static Spotlight createSpotlight(final Activity activity, SimpleTarget target){
        Spotlight spotlight = Spotlight.with(activity)
                .setOverlayColor(R.color.background)
                .setDuration(1000L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(target)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                    @Override
                    public void onStarted() {
                        //Toast.makeText(activity, "spotlight is started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEnded() {
                        //Toast.makeText(activity, "spotlight is ended", Toast.LENGTH_SHORT).show();
                    }
                });
        return spotlight;
    }


    public static SimpleTarget createTarget(Activity activity, float x, float y, Shape shape){
        SimpleTarget simpleTarget = new SimpleTarget.Builder(activity)
                .setPoint(x, y)
                .setShape(shape)
                .setTitle("Seat here")
                .setDescription("")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        // do something
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        // do something
                    }
                })
                .build();
        return simpleTarget;
    }
    //inner class used to create shape for spotlight
    class RectangleShape implements Shape{
        private float width;
        private float height;
        public RectangleShape(float width,float height){
            this.width= width;
            this.height = height;
        }
        @Override
        public void draw(Canvas canvas, PointF point, float value, Paint paint) {
            canvas.drawRect(point.x, point.y,point.x+width*value,point.y+height*value,paint);
        }
        @Override
        public int getHeight() {
            return (int) height;
        }

        @Override
        public int getWidth() {
            return (int) width;
        }
    }
}
