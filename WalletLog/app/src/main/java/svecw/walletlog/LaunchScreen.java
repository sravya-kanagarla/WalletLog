package svecw.walletlog;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class LaunchScreen extends Activity {
    MediaPlayer mediaPlayer;
    private static int SPLASH_TIME_OUT = 2600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        mediaPlayer = MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                mediaPlayer.stop();
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
