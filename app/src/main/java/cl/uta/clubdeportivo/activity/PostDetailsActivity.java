package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Html;
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
import cl.uta.clubdeportivo.api.models.posts.post.PostDetails;
import cl.uta.clubdeportivo.data.constant.AppConstant;
import cl.uta.clubdeportivo.data.sqlite.FavouriteDbController;
import cl.uta.clubdeportivo.models.FavouriteModel;
import cl.uta.clubdeportivo.utility.AppUtils;
import cl.uta.clubdeportivo.utility.TtsEngine;
import cl.uta.clubdeportivo.webengine.WebEngine;
import cl.uta.clubdeportivo.webengine.WebListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */

public class PostDetailsActivity extends BaseActivity {

    // Variables
    private Activity mActivity;
    private Context mContext;

    // init views
    private RelativeLayout lytPostDetailsView;
    private int clickedPostId;
    private ImageView imgPost;
    private TextView tvPostTitle, tvPostAuthor, tvPostDate;
    private WebView webView;
    private ImageButton imgBtnSpeaker, imgBtnShare;
    private PostDetails model = null;

    // Favourites view
    private List<FavouriteModel> favouriteList;
    private FavouriteDbController favouriteDbController;
    private boolean isFavourite = false;

    // Text to speech
    private TtsEngine ttsEngine;
    private boolean isTtsPlaying = false;
    private String ttsText;

    private WebEngine webEngine;

    private boolean fromPush = false, fromAppLink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = PostDetailsActivity.this;
        mContext = mActivity.getApplicationContext();

        // Favourites view
        favouriteList = new ArrayList<>();



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            clickedPostId = extras.getInt(AppConstant.BUNDLE_KEY_POST_ID, 0);
            if(extras.containsKey(AppConstant.BUNDLE_FROM_PUSH)) {
                fromPush = extras.getBoolean(AppConstant.BUNDLE_FROM_PUSH);
            }
            if(extras.containsKey(AppConstant.BUNDLE_FROM_APP_LINK)) {
                fromAppLink = extras.getBoolean(AppConstant.BUNDLE_FROM_APP_LINK);
            }
        }

    }

    private void initView() {
        setContentView(R.layout.activity_post_details);

        lytPostDetailsView = (RelativeLayout) findViewById(R.id.lyt_post_details);
        //lytParentView.setVisibility(View.GONE);

        imgPost = (ImageView) findViewById(R.id.post_img);
        tvPostTitle = (TextView) findViewById(R.id.title_text);
        tvPostAuthor = (TextView) findViewById(R.id.post_author);
        tvPostDate = (TextView) findViewById(R.id.date_text);
        imgBtnSpeaker = (ImageButton) findViewById(R.id.imgBtnSpeaker);
        imgBtnShare = (ImageButton) findViewById(R.id.imgBtnShare);

        initWebEngine();


        initLoader();

        initToolbar();
        enableBackButton();

    }

    public void initWebEngine() {

        webView = (WebView) findViewById(R.id.web_view);

        webEngine = new WebEngine(webView, mActivity);
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

        favouriteDbController = new FavouriteDbController(mContext);
        favouriteList.addAll(favouriteDbController.getAllData());
        for (int i = 0; i < favouriteList.size(); i++) {
            if (favouriteList.get(i).getPostId() == clickedPostId) {
                isFavourite = true;
                break;
            }
        }

        ttsEngine = new TtsEngine(mActivity);

        showLoader();
        loadPostDetails();
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
                    sendIntent.putExtra(Intent.EXTRA_TEXT, model.getPageUrl());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
            }
        });

    }

    public void loadPostDetails() {
        ApiUtils.getApiInterface().getPostDetails(clickedPostId).enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                if (response.isSuccessful()) {
                    // bind data
                    model = response.body();
                    PostDetails m = model;

                    // visible parent view
                    lytPostDetailsView.setVisibility(View.VISIBLE);

                    tvPostTitle.setText(Html.fromHtml(model.getTitle().getRendered()));

                    String imgUrl = null;
                    if (model.getEmbedded().getWpFeaturedMedias().size() > 0) {
                        if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails() != null) {
                            if (model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl() != null) {
                                imgUrl = model.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                            }
                        }
                    }

                    if (imgUrl != null) {
                        Glide.with(getApplicationContext())
                                .load(imgUrl)
                                .into(imgPost);
                    }

                    String author = null;
                    if (model.getEmbedded().getAuthors().size() >= 1) {
                        author = model.getEmbedded().getAuthors().get(0).getName();
                    }

                    if (author == null) {
                        author = getString(R.string.admin);
                    }
                    tvPostAuthor.setText(Html.fromHtml(author));

                    String oldDate = model.getOldDate();
                    String newDate = AppUtils.getFormattedDate(oldDate);

                    if (newDate != null) {
                        tvPostDate.setText(Html.fromHtml(newDate));
                    }

                    String contentText = model.getContent().getRendered();

                    ttsText = new StringBuilder(Html.fromHtml(model.getTitle().getRendered())).append(AppConstant.DOT).append(Html.fromHtml(model.getContent().getRendered())).toString();

                    //webView.loadData(contentText, "text/html", "UTF-8");

                    contentText = new StringBuilder().append(AppConstant.CSS_PROPERTIES).append(contentText).toString();
                    webEngine.loadHtml(contentText);

                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }


    private void goToHome(){
        if(fromPush || fromAppLink) {
            Intent intent = new Intent(PostDetailsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }
}
