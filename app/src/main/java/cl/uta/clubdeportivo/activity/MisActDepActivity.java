package cl.uta.clubdeportivo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.ActDepListAdapter;
import cl.uta.clubdeportivo.adapters.ActDeportivasAdapter;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import cl.uta.clubdeportivo.api.models.appusers.TokenWP;
import cl.uta.clubdeportivo.listeners.ListItemClickListener;
import cl.uta.clubdeportivo.utility.DialogUtils;
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
    private List<ActDeportivas>  actDeportivasList;
    private ActDeportivasAdapter actDepAdapter;
    private RecyclerView rvActividades;
    private String idWp;
    private String token;

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
        actDeportivasList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_mis_act_deportivas);
        bottomLayout = findViewById(R.id.rv_misActDep_load);

        rvMisActDeportivas = findViewById(R.id.rvMisActividades);
        //pbMisActividades = findViewById(R.id.pbMisActDeportivasSectionLoader);
        mGridLayoutManager = new GridLayoutManager(mActivity, 1, RecyclerView.VERTICAL, false);
        rvMisActDeportivas.setLayoutManager(mGridLayoutManager);
        rlMisActividadesNoInscritas = findViewById(R.id.rlNoActDepInscritas);

        floatingActionButton = findViewById(R.id.fabMisActDep);

        initLoader();
        rlMisActividadesNoInscritas.setVisibility(View.GONE);
        initToolbar();
        setToolbarTitle("Mis Actividades Deportivas");
        getToolBar().setTitleTextColor(Color.parseColor("#FFFFFF"));
        enableBackButton();
    }

    private void initFunctionality() {
        actDepAdapter = new ActDeportivasAdapter(mActivity, (ArrayList<ActDeportivas>) actDeportivasList);
        misActDeportivasAdapter = new ActDeportivasAdapter(mActivity, (ArrayList<ActDeportivas>) misActDeportivasList);
        rvMisActDeportivas.setAdapter(misActDeportivasAdapter);

        showLoader();
        loadMisActividades();
        loadActDep();

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
                                lista_talleres = jsonObject.get("lista_talleres").toString();//.getAsString();
                                Log.d(TAG, "lista_talleres antes: " + lista_talleres);
                                lista_talleres = lista_talleres.replace("[", "").replace("]","");
                                Log.d(TAG, "lista_talleres despues: " + lista_talleres);
                                idWp = jsonObject.get("id").getAsString();
                                listTalleres = new ArrayList<>(Arrays.asList(lista_talleres.split(",")));

                            }
                        }
                        /**  API Request based on lista_talleres */
                        if (lista_talleres.equals("false") || lista_talleres.isEmpty() || listTalleres.isEmpty()) {
                            // No hay ningun taller inscrito
                            Log.d(TAG, "lista_talleres falso/vacio: " + lista_talleres);
                            rlMisActividadesNoInscritas.setVisibility(View.VISIBLE);

                        } else {
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

    private void loadActDep() {
        ApiUtils.getApiInterface().getAllActDeportivas().enqueue(new Callback<List<ActDeportivas>>() {
            @Override
            public void onResponse(Call<List<ActDeportivas>> call, Response<List<ActDeportivas>> response) {
                if (response.isSuccessful()) {
                    actDeportivasList.addAll(response.body());
                    actDepAdapter.notifyDataSetChanged();
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


    private void initListener() {

//        final ArrayList lista_act = new ArrayList<>();
//        for (int i = 0; i < actDeportivasList.size(); i++) {
//            lista_act.add(actDeportivasList.get(i).getId());
//            lista_act.add(actDeportivasList.get(i).getNombreActividaddeportiva());
//
//        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: test fabFAB!");

                final Dialog dialog = new Dialog(MisActDepActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
                ListView lv = view.findViewById(R.id.custom_list);
                ActDepListAdapter listAdapter = new ActDepListAdapter(MisActDepActivity.this, actDeportivasList);
                lv.setAdapter(listAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemClick: lista id "+ ((ActDeportivas) parent.getItemAtPosition(position)).getId());
                        if(listTalleres.isEmpty()) listTalleres = new ArrayList<>();
                        if (!listTalleres.contains(((ActDeportivas) parent.getItemAtPosition(position)).getId().toString())) {
                            Log.d(TAG, "onItemClick: if lista talleres  ");
                            listTalleres.add(((ActDeportivas) parent.getItemAtPosition(position)).getId().toString());
                            
                        }
                        boolean existe = false;
                        for (int i = 0; i < misActDeportivasList.size(); i++) {
                            Log.d(TAG, "existe : "  + ((ActDeportivas) parent.getItemAtPosition(position)).getId().equals(misActDeportivasList.get(i).getId()));
                            existe = ((ActDeportivas) parent.getItemAtPosition(position)).getId().equals(misActDeportivasList.get(i).getId());
                            if (existe) break;
                        }
                        if (!existe) {
                            misActDeportivasList.add(((ActDeportivas) parent.getItemAtPosition(position)));
                            Log.d(TAG, "No existe: Agrega ");
                        }

                        postListaTalleres(listTalleres);
                        dialog.dismiss();
                    }
                });

                dialog.setTitle("Elegir Actividad a inscribir");
                dialog.setContentView(view);


                dialog.show();
                //builder.show();
            }
        });

        misActDeportivasAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(final int position, View view) {

                android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);

                alertDialogBuilder.setTitle("Eliminar Actividad");
                alertDialogBuilder.setMessage(R.string.delete_notify_act);
                alertDialogBuilder.setCancelable(true);


                alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "onClick: popup ");
                        dialog.cancel();
                        removeMisAct(position);
                    }
                });



                alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                alertDialogBuilder.create().show();
//                removeMisAct(position);

            }

            private void removeMisAct(int position) {
                int clickedIdAct = misActDeportivasList.get(position).getId().intValue();
                Log.d(TAG, "onItemClick clickedIdAct: " + clickedIdAct);
                Log.d(TAG, "onItemClick: "+android.text.TextUtils.join(",",listTalleres));
                if (listTalleres.contains(String.valueOf(clickedIdAct))) {
                    for (int i = 0; i<misActDeportivasList.size(); i++) {
                        Log.d(TAG, "onItemClick: getid" + misActDeportivasList.get(i).getId());
                        if (clickedIdAct==misActDeportivasList.get(i).getId()) {
                            Log.d(TAG, "onItemClick: isequal");
                            misActDeportivasList.remove(position);
                        }
                    }
                    listTalleres.remove(String.valueOf(clickedIdAct));

                }
                Log.d(TAG, "onItemClick: " + misActDeportivasList);
                postListaTalleres(listTalleres);
            }
        });

    }

    private void postListaTalleres(final List<String> listTalleres) {
        String lista_taller_post = android.text.TextUtils.join(",",listTalleres);
        lista_taller_post = "[".concat(lista_taller_post).concat("]");
        Log.d(TAG, "onItemClick: lista_taller_post"+lista_taller_post);
        Response<TokenWP> resToken = null;
        try {
            resToken = ApiUtils.getApiInterface().getToken("AppClubDeportivo", "AppClubDeportiv").execute();
            if (resToken.isSuccessful()) {
                token = resToken.body().getToken();
                Log.d(TAG, "onItemClick token: "+token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApiUtils.getApiInterface().postListaTalleres("Bearer "+token, idWp, "{ \"lista_talleres\": "+lista_taller_post+"}").enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: "+response.raw());
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: success");
                    misActDeportivasAdapter.notifyDataSetChanged();

                    if (listTalleres.isEmpty()) {
                        rvMisActDeportivas.setVisibility(View.GONE);
                        rlMisActividadesNoInscritas.setVisibility(View.VISIBLE);
                    } else {
                        rvMisActDeportivas.setVisibility(View.VISIBLE);
                        rlMisActividadesNoInscritas.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

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
