package com.example.parthdoshi.bmi;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView ivImage,ivText;
    Animation a,b;
    Matrix matrix = new Matrix();
    Float scale = 0.5f;
    ScaleGestureDetector SGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivImage = (ImageView)findViewById(R.id.ivImage);
        ivText = (ImageView)findViewById(R.id.ivText);

        a = AnimationUtils.loadAnimation(this,R.anim.a);
        b = AnimationUtils.loadAnimation(this,R.anim.b);

        ivImage.setAnimation(a);
        ivText.setAnimation(b);

//        CODE FOR MAKING INTERACTIVE SPLASH SCREEN PRESENT IN PRIVATE REPO

}
