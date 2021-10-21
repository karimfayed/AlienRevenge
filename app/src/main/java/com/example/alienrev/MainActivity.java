package com.example.alienrev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;
import android.animation.Animator;
import android.view.animation.AccelerateInterpolator;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManagerAccelerometer;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerListener;
    private SensorManager sensorManagerLight;
    private Sensor lightSensor;
    private SensorEventListener lightListener;
    float maxvalue;
    ImageView imgView;
    private SensorManager sensorManager;
    private Sensor gyrpscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    private int[] location = new int[2];
    private int width;
    private int height;
    private ImageView img_UFO;
    private double[] gravity = new double[3];
    private double[] linear_acceleration = new double[3];
    private double lastTimer = 0;
    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private int delay =5000;
    private int birdWidth, temp;
    private  boolean flag=false;
    private SensorManager sensorManagerProximity;
    private float[] birdsX = new float[3];
    private float[] birdsY = new float[3];
    private float[] UFO = new float[2];
    private Sensor proximity;
    private ImageView bird1;
    private ImageView bird2;
    private ImageView bird4;
    private SensorEventListener proximityListener;
    int UFO_width;
    int UFO_height;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**image view initialization**/
        img_UFO =  findViewById(R.id.img_ufo);
        imgView = (ImageView)findViewById(R.id.img);

        /**helping variables to get the width and height of the screen**/
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        width = size.x;
        height = size.y;
        UFO_width = img_UFO.getLayoutParams().width;
        UFO_height = img_UFO.getLayoutParams().height;

        // Move to out of screen.
        img_UFO.setX((width / 2) - (UFO_width / 2 ));
        img_UFO.setY(height -  2 * UFO_height);
//        Log.v("the content:", " screen " + width + ", " + height + " elsora data" + UFO_width + ", "+ UFO_width );
//        Log.v("the content:", " elsora " + img_UFO.getX() + ", " + img_UFO.getY());

        /** sensor data initialization **/
        sensorManagerAccelerometer = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManagerAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyrpscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManagerLight = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        /**check if the sensor is avaliable in the device**/
        if (accelerometerSensor == null) {
            Toast.makeText(this, "The device has no Accelerometer", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (gyrpscopeSensor == null) {
            Toast.makeText(this, "The device has no Gyroscope", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (lightSensor == null) {
            Toast.makeText(this, "The device has no Light Sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        //max value of lux
        maxvalue = lightSensor.getMaximumRange();


        lightListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_LIGHT)
                {

                    if(event.values[0] >= 0 && event.values[0] < (maxvalue/6))
                    {
                        imgView.setImageResource(R.drawable.bg_skynight);
//                        t.setText("Night");
//                        t.setAnimation(topAnim);

                    }
                    else if(event.values[0] >= (maxvalue/6) && event.values[0] < (2 * maxvalue/6))
                    {
                        imgView.setImageResource(R.drawable.bg_sky);
//                        t.setText("Day");
//                        t.setAnimation(topAnim);

                    }
                    else if(event.values[0] >= (2 * maxvalue/6) && event.values[0] <= maxvalue)
                    {
                        imgView.setImageResource(R.drawable.bg_skymorning);
//                        t.setText("Morning");
//                        t.setAnimation(topAnim);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManagerAccelerometer.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

        /**Creating Accelerometer event listener**/
        accelerometerListener = new SensorEventListener() {
            @Override

            public void onSensorChanged(SensorEvent event) {

                changePos(event);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[1]> 0.5f){

                    img_UFO.setRotation(45);
                }
                else if (sensorEvent.values[1] <-0.5f) {

                    img_UFO.setRotation(-45);
                }
                else if(sensorEvent.values[1]<= 0.5f && sensorEvent.values[1] >=-0.5f )
                {

                    img_UFO.setRotation(0);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };






        /**Proximity sensor val**/
        bird1=(ImageView)findViewById(R.id.bird1);
        bird2=(ImageView)findViewById(R.id.bird2);
        bird4=(ImageView)findViewById(R.id.bird4);
        birdWidth = bird1.getWidth();
        bird1.setY(-200);
        bird2.setY(-200);
        bird4.setY(-200);

        animateImage(bird1,0,delay/2);
        animateImage(bird2,1,delay/3);
        animateImage(bird4,2,delay);

        sensorManagerProximity = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY)!=null)
        {
            proximity = sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        else
        {
            Toast.makeText(this, "The device has no Proximity Sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        proximityListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent event) {
                float distance = event.values[0];
                if(distance <=2)
                {
                    delay=2000;
                }else
                {
                    delay = 5000;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManagerProximity.registerListener(proximityListener, proximity, sensorManagerProximity.SENSOR_DELAY_NORMAL);
        sensorManagerAccelerometer.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(gyroscopeEventListener, gyrpscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(lightListener,lightSensor,SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManagerProximity.unregisterListener(proximityListener);
        sensorManager.unregisterListener(gyroscopeEventListener,gyrpscopeSensor);
        sensorManager.unregisterListener(lightListener);
    }


    protected void animateImage(final ImageView bird, int birdNum, int delay)
    {

        final float bottomOfScreen = getResources().getDisplayMetrics()
                .heightPixels - (bird.getHeight() * 4);
        //bottomOfScreen is where you want to animate to
        Random  randomX = new Random();

        float randx = randomX.nextFloat();
        while(!((randx < Math.abs(birdsX[0]-birdWidth) || randx > birdsX[0]+birdWidth)&&(randx < Math.abs(birdsX[1]-birdWidth) || randx > birdsX[1]+birdWidth)&&(randx < Math.abs(birdsX[2]-birdWidth) || randx > birdsX[2]+birdWidth)))
        {
            randomX = new Random();
            randx = randomX.nextFloat();
        }
        birdsX[birdNum]= randx;
        final Random finalRandomX = randomX;
        bird.animate()
                //.translationX(randomX.nextInt(500+1))
                .translationY(bottomOfScreen)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(delay).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bird.setY(-200);
                bird.setX(finalRandomX.nextInt(800));
                bird.animate()
                        .translationY(bottomOfScreen)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(MainActivity.this.delay);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
    public void changePos(SensorEvent event) {


        linear_acceleration[0] = event.values[0] ;
        linear_acceleration[1] = event.values[1] ;
        linear_acceleration[2] = event.values[2] ;

        img_UFO.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        if (Math.abs(linear_acceleration[0]) > 0.5) {
            Log.v("the acceleration", linear_acceleration[0] + ", " + linear_acceleration[1]);
            if (x <= width - 370 && x >= 100 /*&& Math.abs(System.currentTimeMillis() - lastTimer) > 5e-3*/) {

                Log.v("the content:", " and the img location = " + x + ", " + y);
                img_UFO.setX((float) (x + linear_acceleration[0] * -3));
                img_UFO.getLocationInWindow(location);
                x = location[0];
                lastTimer = System.currentTimeMillis();

            } else if (x > width - 370) {
                img_UFO.setX(width - 380);
            } else if (x < 100) {
                img_UFO.setX(110);
            }
        }
        birdWidth = bird1.getWidth();
        birdsX[0] = bird1.getX();
        birdsX[1] = bird2.getX();
        birdsX[2] = bird4.getX();
        birdsY[0] = bird1.getY();
        birdsY[1] = bird2.getY();
        birdsY[2] = bird4.getY();
        UFO[0] = img_UFO.getX();
        UFO[1] = img_UFO.getY();
        check();
    }

    public void check(){

        if(inRangeOfX() && inRangeOfY()){
            Log.v("hobaaaaa", inRangeOfX() + " " + inRangeOfY());
        }
    }

    private boolean inRangeOfX(){

        if(( (birdsX[0]+birdWidth>UFO[0] && birdsX[0]+birdWidth < UFO[0]+UFO_width)||(birdsX[0]<UFO[0]+UFO_width && birdsX[0]>UFO[0]))
                || ((birdsX[1]+birdWidth>UFO[0] && birdsX[1]+birdWidth < UFO[0]+UFO_width)||(birdsX[1]<UFO[0]+UFO_width && birdsX[1]>UFO[0]))
                || ((birdsX[2]+birdWidth>UFO[0] && birdsX[2]+birdWidth < UFO[0]+UFO_width)||(birdsX[2]<UFO[0]+UFO_width && birdsX[2]>UFO[0]))
        )
        {
            return true;
        }
        else{
            return false;
        }
    }

    private boolean inRangeOfY(){

        if((UFO[1] <= birdsY[0] + birdWidth ) || (UFO[1] <= birdsY[1] + birdWidth)|| (UFO[1] <= birdsY[2] + birdWidth )){
            return true;
        }
        else{
            return false;
        }
    }

}



