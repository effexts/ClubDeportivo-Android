package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import cl.uta.clubdeportivo.data.constant.AppConstant;
import cl.uta.clubdeportivo.utility.TtsEngine;
import cl.uta.clubdeportivo.webengine.WebEngine;
import cl.uta.clubdeportivo.webengine.WebListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActDeportivasDetailsActivity extends BaseActivity {

    // Variables
    private Activity mActivity;
    private Context mContext;

    // init views
    private RelativeLayout lytActDepDetailsView;
    private int clickedId;
    private ImageView imgPost;
    private TextView tvActDepTitle, tvPostAuthor, tvPostDate;
    private WebView actdep_webView;
    private ImageButton imgBtnSpeaker, imgBtnShare;
    private ActDeportivas model = null;

    // Text to speech
    private TtsEngine ttsEngine;
    private boolean isTtsPlaying = false;
    private String ttsText;

    private WebEngine webEngine;

    private boolean fromPush = false, fromAppLink = false;
    private String TAG = "ClubDeportivo: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = ActDeportivasDetailsActivity.this;
        mContext = mActivity.getApplicationContext();



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            clickedId = extras.getInt("act_id", 0);
            Log.d(TAG, "initVar: "+clickedId);
        }

    }

    private void initView() {
        setContentView(R.layout.activity_actdep_details);

        lytActDepDetailsView = (RelativeLayout) findViewById(R.id.lyt_actdep_details);
        //lytParentView.setVisibility(View.GONE);

        imgPost = findViewById(R.id.actdep_details_img);

        tvActDepTitle = findViewById(R.id.actdep_details_title_text);
        imgBtnSpeaker = findViewById(R.id.actdep_details_imgBtnSpeaker);
        imgBtnShare = findViewById(R.id.actdep_details_imgBtnShare);

        initWebEngine();


        initLoader();

        initToolbar();
        enableBackButton();

    }

    public void initWebEngine() {

        actdep_webView = findViewById(R.id.actdep_details_web_view);

        webEngine = new WebEngine(actdep_webView, mActivity);
        webEngine.initWebView();


        webEngine.initListeners(new WebListener() {
            @Override
            public void onStart() {
                initLoader();
            }

            @Override
            public void onLoaded() {
                hideLoader();
            }

            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onNetworkError() {
                showEmptyView();
            }

            @Override
            public void onPageTitle(String title) {
            }
        });
    }

    private void initFunctionality() {

        ttsEngine = new TtsEngine(mActivity);

        showLoader();
        loadActDepDetails();
    }

    public void initListener() {

        imgBtnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model != null) {
                    toggleTtsPlay();
                }
            }
        });

        imgBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, model.getLink());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
            }
        });

    }

    public void loadActDepDetails() {
        ApiUtils.getApiInterface().getActDeportivasDetails(clickedId).enqueue(new Callback<ActDeportivas>() {
            @Override
            public void onResponse(Call<ActDeportivas> call, Response<ActDeportivas> response) {
                if (response.isSuccessful()) {
                    // bind data
                    model = response.body();
                    ActDeportivas m = model;

                    // visible parent view
                    lytActDepDetailsView.setVisibility(View.VISIBLE);

                    tvActDepTitle.setText(Html.fromHtml(model.getTitleActDep().getRendered()));

                    String imgUrl = null;
                    imgUrl = model.getImagen().getGuid();

                    if (imgUrl != null) {
                        Glide.with(getApplicationContext())
                                .load(imgUrl)
                                .into(imgPost);
                    }

                    String contentText = model.getContent().getRendered();

                    ttsText = new StringBuilder(Html.fromHtml(model.getTitleActDep().getRendered())).append(AppConstant.DOT).append(Html.fromHtml(model.getContent().getRendered())).toString();

                    //actdep_webView.loadData(contentText, "text/html", "UTF-8");

                    contentText = new StringBuilder().append(AppConstant.CSS_PROPERTIES).append(contentText).toString();
                    webEngine.loadHtml(contentText);

                } else {
                    showEmptyView();
                }
            }


            @Override
            public void onFailure(Call<ActDeportivas> call, Throwable t) {
                t.printStackTrace();
                // hide common loader
                hideLoader();
                // show empty view
                showEmptyView();
            }
        });
    }

    private void toggleTtsPlay() {
        if (isTtsPlaying) {
            ttsEngine.releaseEngine();
            isTtsPlaying = false;
        } else {
            ttsEngine.startEngine(ttsText);
            isTtsPlaying = true;
        }
        toggleTtsView();
    }

    private void toggleTtsView() {
        if (isTtsPlaying) {
            imgBtnSpeaker.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker_stop));
        } else {
            imgBtnSpeaker.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                goToHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goToHome();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ttsEngine.releaseEngine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsEngine.releaseEngine();
        model = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTtsPlaying) {
            isTtsPlaying = false;
            imgBtnSpeaker.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }


    private void goToHome(){
        if(fromPush || fromAppLink) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }
}

