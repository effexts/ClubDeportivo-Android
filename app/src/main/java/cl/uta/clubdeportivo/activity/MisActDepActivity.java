package cl.uta.clubdeportivo.activity;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.ActDeportivasAdapter;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisActDepActivity extends BaseActivity {
    private MisActDepActivity mActivity;
    private Context mContext;
    private List<ActDeportivas> misActDeportivasList;
    private RelativeLayout bottomLayout;
    private RecyclerView rvMisActDeportivas;
    private GridLayoutManager mGridLayoutManager;
    private ActDeportivasAdapter misActDeportivasAdapter;
    private String TAG = "MisActividades";
    //private ProgressBar pbMisActividades;
    private boolean userScrolled = true;
    private int visibleItemCount, pastVisibleItems, totalItemCount;
    private String lista_talleres = "";
    List<String> listTalleres;
    private RelativeLayout rlMisActividadesNoInscritas;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initVar();
        initView();
        initFunctionality();
        initListener();
        implementScrollListener();

    }


    private void initVar() {
        mActivity = MisActDepActivity.this;
        mContext = mActivity.getApplicationContext();
        misActDeportivasList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_mis_act_deportivas);
        bottomLayout = findViewById(R.id.rv_misActDep_load);
        rvMisActDeportivas = findViewById(R.id.rvMisActividades);
        //pbMisActividades = findViewById(R.id.pbMisActDeportivasSectionLoader);
        mGridLayoutManager = new GridLayoutManager(mActivity, 1, GridLayoutManager.VERTICAL, false);
        rvMisActDeportivas.setLayoutManager(mGridLayoutManager);
        rlMisActividadesNoInscritas = findViewById(R.id.rlNoActDepInscritas);
        floatingActionButton = findViewById(R.id.fabMisActDep);

        initLoader();
        initToolbar();
        setToolbarTitle("Mis Actividades Deportivas");
        getToolBar().setTitleTextColor(Color.parseColor("#FFFFFF"));
        enableBackButton();
    }

    private void initFunctionality() {
        misActDeportivasAdapter = new ActDeportivasAdapter(mActivity, (ArrayList<ActDeportivas>) misActDeportivasList);
        rvMisActDeportivas.setAdapter(misActDeportivasAdapter);

        showLoader();
        loadMisActividades();

    }

    private void loadMisActividades() {


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            ApiUtils.getApiInterface().getMisActividades("user_id", uid).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if (response.isSuccessful()) {
                        JsonArray jsonArray = response.body().getAsJsonArray();
                        if (jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                lista_talleres = jsonObject.get("lista_talleres").getAsString();
                                listTalleres = new ArrayList<>(Arrays.asList(lista_talleres.split(",")));

                            }
                        }
                        /**  API Request based on lista_talleres */
                        if (lista_talleres.equals("false") || lista_talleres.isEmpty()) {
                            // No hay ningun taller inscrito
                            Log.d(TAG, "lista_talleres falso/vacio: " + lista_talleres);

                        } else {
                            lista_talleres.replace(lista_talleres, "[").replace(lista_talleres, "]");
                            Log.d(TAG, "lista_talleres: " + lista_talleres);
                            for (int i = 0; i < listTalleres.size(); i++) {
                                Log.d(TAG, "arrayTalleres "+ listTalleres.get(i));
                                try {
                                    ApiUtils.getApiInterface().getActDeportivasDetails(Integer.parseInt(listTalleres.get(i))).enqueue(new Callback<ActDeportivas>() {
                                        @Override
                                        public void onResponse(Call<ActDeportivas> call, Response<ActDeportivas> response) {
                                            if (response.isSuccessful()) {
                                                rlMisActividadesNoInscritas.setVisibility(View.GONE);
                                                misActDeportivasList.add(response.body());
                                                misActDeportivasAdapter.notifyDataSetChanged();
                                            }
                                            else {
                                                showEmptyView();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ActDeportivas> call, Throwable t) {

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d(TAG, "onFailure: error al obtener información.");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        hideLoader();

    }


    private void initListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: test fabFAB!");
                MaterialDialog materialDialog = new MaterialDialog(mContext);
                RecyclerView recyclerView = new RecyclerView(mContext);
                recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1, GridLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(misActDeportivasAdapter);
                materialDialog.setContentView(recyclerView);
                materialDialog.show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void implementScrollListener() {
        rvMisActDeportivas.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount = mGridLayoutManager.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                pastVisibleItems = mGridLayoutManager.findFirstVisibleItemPosition();

                if (userScrolled && (visibleItemCount + pastVisibleItems) == totalItemCount) {
                    userScrolled = false;
                }
            }
        });
    }


}
