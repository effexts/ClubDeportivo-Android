package cl.uta.clubdeportivo.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.appusers.AppUserBody;
import cl.uta.clubdeportivo.api.models.appusers.AppUsers;
import cl.uta.clubdeportivo.api.models.appusers.TokenWP;
import cl.uta.clubdeportivo.data.preference.AppPreference;
import cl.uta.clubdeportivo.utility.ActivityUtils;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private static final String TAG = "RegistroActivity";
    private EditText inputRegEmail;
    private EditText inputRegPassword;
    private EditText inputRegNombres;
    private Button btnRegistro;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    TokenWP tokenWP;
    private String token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuarios);
        mAuth = FirebaseAuth.getInstance();
        inputRegEmail = findViewById(R.id.input_reg_email);
        inputRegPassword = findViewById(R.id.input_reg_password);
        inputRegNombres = findViewById(R.id.input_reg_name);
        btnRegistro = findViewById(R.id.btn_signup);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    createAccount(
                            inputRegEmail.getText().toString(),
                            inputRegPassword.getText().toString(),
                            inputRegNombres.getText().toString()
                    );
                }

            }
        });


    }

    private void createAccount(final String email, String password, final String names) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //task.getResult().getUser().getUid();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "onComplete: " + user.getUid());
                            //updateUI(user);
                            registerInWp(email, names);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });


        hideProgressDialog();
    }

    private void registerInWp(String email, String names) {
        Log.d(TAG, "registerInWp: " + mAuth.getCurrentUser());
        if (mAuth.getCurrentUser() != null) {

            try {
                Response<TokenWP> resToken = ApiUtils.getApiInterface().getToken("AppClubDeportivo", "AppClubDeportiv").execute();
                if (resToken.isSuccessful()) {
                    token = resToken.body().getToken();
                    AppPreference.getInstance(this).setString("TOKEN", token);
                    Log.d(TAG, "registerInWp:" + token);
                } else {
                    Log.d(TAG, "Falló al obtener token:" + resToken.message());
                    registerInWp(email, names);
                }

                if (token != null) {
                    Log.d(TAG, "token != null");
                    AppUserBody appUserBody = new AppUserBody(
                            "publish",
                            names,
                            mAuth.getCurrentUser().getUid(),
                            names,
                            email
                    );
                    Response<AppUsers> resAppUsers = ApiUtils.getApiInterface().addUser2("Bearer " + token, appUserBody).execute();
                    if (resAppUsers.isSuccessful()) {
                        Log.d(TAG, "Registro creado correctamente en wp: " + resAppUsers.body().toString());
                        ActivityUtils.getInstance().invokeActivity(RegistroActivity.this, MainActivity.class, true);
                    } else {
                        Log.d(TAG, "registerInWp: " + resAppUsers.raw());
                    }
                } else {
                    registerInWp(email, names);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = inputRegEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputRegEmail.setError("Requerido.");
            valid = false;
        } else {
            inputRegEmail.setError(null);
        }

        String password = inputRegPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputRegPassword.setError("Requerido.");
            valid = false;
        } else {
            inputRegPassword.setError(null);
        }

        String nombres = inputRegNombres.getText().toString();
        if (TextUtils.isEmpty(nombres)) {
            inputRegNombres.setError("Requerido");
            valid = false;
        } else {
            inputRegNombres.setError(null);
        }

        return valid;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
