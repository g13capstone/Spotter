package g13capstone.spotter.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import g13capstone.spotter.R;

public class SplashScreen extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tv = (TextView) findViewById(R.id.textView);
        iv = (ImageView) findViewById(R.id.imageView);
        Animation splashanim = AnimationUtils.loadAnimation(this, R.anim.splashanim);
        tv.startAnimation(splashanim);
        iv.startAnimation(splashanim);
        final Intent toMain = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            public void run() {
                try{
                    sleep(1750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(toMain);
                    finish();
                }
            }
        };
        timer.start();
    }
}
