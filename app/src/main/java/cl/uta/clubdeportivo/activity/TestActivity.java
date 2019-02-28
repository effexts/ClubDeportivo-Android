package cl.uta.clubdeportivo.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import cl.uta.clubdeportivo.api.models.appusers.AppUserBody;
import cl.uta.clubdeportivo.api.models.appusers.AppUsers;
import cl.uta.clubdeportivo.api.models.appusers.TokenWP;
import cl.uta.clubdeportivo.api.models.appusers.TokenWPBody;
import retrofit2.Call;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    String TAG = "Test RETROFIT";

    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            Response<List<ActDeportivas>> resToken = ApiUtils.getApiInterface().getAllActDeportivas().execute();

            if (resToken.isSuccessful()) {
                Log.d(TAG, "onCreate: "+resToken.body());
            }
            else {
                Log.d(TAG, "onelse: " + resToken.body());
                Log.d(TAG, "onelse: " + resToken.errorBody().string());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
