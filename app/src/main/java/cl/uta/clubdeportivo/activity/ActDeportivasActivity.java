package cl.uta.clubdeportivo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.ActDeportivasAdapter;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import cl.uta.clubdeportivo.data.constant.AppConstant;
import cl.uta.clubdeportivo.listeners.ListItemClickListener;
import cl.uta.clubdeportivo.utility.ActivityUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActDeportivasActivity extends BaseActivity {

    private ActDeportivasActivity mActivity;
    private Context mContext;
    private List<ActDeportivas> actDeportivasList;
    private RelativeLayout bottomLayout;
    private RecyclerView rvActividades;
    private GridLayoutManager mLayoutManager;
    private ActDeportivasAdapter actDepAdapter;
    private String TAG = "Actividades Deportivas";
    private boolean userScrolled = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private ProgressBar pbActividades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
        implementScrollListener();

    }


    private void initVar() {
        mActivity = ActDeportivasActivity.this;
        mContext = mActivity.getApplicationContext();

        actDeportivasList = new ArrayList<>();

    }

    private void initView() {
        setContentView(R.layout.activity_act_deportivas);
        bottomLayout = findViewById(R.id.rv_actdep_load);
        rvActividades = findViewById(R.id.rvActividades);
        pbActividades = findViewById(R.id.pbActSectionLoader);
        mLayoutManager = new GridLayoutManager(mActivity, 1, GridLayoutManager.VERTICAL, false);
        rvActividades.setLayoutManager(mLayoutManager);

        initLoader();
        initToolbar();
        setToolbarTitle(getString(R.string.actividades_deportivas));
        getToolBar().setTitleTextColor(Color.parseColor("#FFFFFF"));
        enableBackButton();


    }

    private void initFunctionality() {
        actDepAdapter = new ActDeportivasAdapter(mActivity, (ArrayList<ActDeportivas>) actDeportivasList);
        rvActividades.setAdapter(actDepAdapter);

        showLoader();

        loadActDep();


    }

    private void loadActDep() {
        ApiUtils.getApiInterface().getAllActDeportivas().enqueue(new Callback<List<ActDeportivas>>() {
            @Override
            public void onResponse(Call<List<ActDeportivas>> call, Response<List<ActDeportivas>> response) {
                if (response.isSuccessful()) {
                    loadActDeportivas(response);
                    hideLoader();
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<List<ActDeportivas>> call, Throwable t) {
                t.printStackTrace();
                hideLoader();
                showEmptyView();

            }
        });
    }

    private void loadActDeportivas(Response<List<ActDeportivas>> response) {
        actDeportivasList.addAll(response.body());
        if (actDeportivasList.size()>0) {
            actDepAdapter.notifyDataSetChanged();
            hideLoader();
            pbActividades.setVisibility(View.GONE);
        } else {
            showEmptyView();
        }
    }

    private void initListener() {
        actDepAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                int clickedIdAct = actDeportivasList.get(position).getId().intValue();
                switch (view.getId()) {
                    case R.id.card_view_actividades:
                        Log.d(TAG, "onItemClick: ID " + clickedIdAct);
                        Intent intent = new Intent(mContext, ActDeportivasDetailsActivity.class);
                        intent.putExtra("act_id", clickedIdAct);
                        mActivity.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void implementScrollListener() {
        rvActividades.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (userScrolled && (visibleItemCount+pastVisibleItems) == totalItemCount ) {
                    userScrolled=false;
                }
            }
        });
    }



}
