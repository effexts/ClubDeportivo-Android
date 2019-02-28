package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.utility.ActivityUtils;
import cl.uta.clubdeportivo.utility.AppUtils;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private RelativeLayout rootLayout;

    // Constants
    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initVariable();
        initView();

        initSubscribe();

    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = SplashActivity.this;
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        rootLayout = (RelativeLayout) findViewById(R.id.splashBody);
    }

    private void initFunctionality() {
        if (AppUtils.isNetworkAvailable(mContext)) {
            rootLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.getInstance().invokeActivity(mActivity, LoginActivity.class, true);
                }
            }, SPLASH_DURATION);
        } else {
            AppUtils.noInternetWarning(rootLayout, mContext);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }

    private void initSubscribe() {
        FirebaseMessaging.getInstance().subscribeToTopic("noticias")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscrito al tema Noticias";
                        if (!task.isSuccessful()) {
                            msg = "falló al subscribir";
                        }
                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

