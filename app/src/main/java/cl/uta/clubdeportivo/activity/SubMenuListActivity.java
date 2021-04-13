package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.SubMenuAdapter;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.category.Category;
import cl.uta.clubdeportivo.api.models.menus.SubMenu;
import cl.uta.clubdeportivo.api.models.menus.SubMenuItem;
import cl.uta.clubdeportivo.data.constant.AppConstant;
import cl.uta.clubdeportivo.listeners.ListItemClickListener;
import cl.uta.clubdeportivo.utility.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */

public class SubMenuListActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private RecyclerView rvSubMenus;
    private SubMenuAdapter subMenusAdapter = null;
    private ArrayList<SubMenuItem> subMenuItemList;
    private ArrayList<Category> categoryList;
    private int mSelectedMenuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = SubMenuListActivity.this;
        mContext = mActivity.getApplicationContext();

        subMenuItemList = new ArrayList<>();
        categoryList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            categoryList = getIntent().getParcelableArrayListExtra(AppConstant.BUNDLE_KEY_CATEGORY_LIST);
            mSelectedMenuId = getIntent().getIntExtra(AppConstant.BUNDLE_KEY_MENU_ID, 0);
        }

    }

    private void initView() {
        setContentView(R.layout.activity_menu_or_home_cat_list);

        rvSubMenus = (RecyclerView) findViewById(R.id.rv_menus);
        rvSubMenus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        initLoader();

        initToolbar();
        setToolbarTitle(getString(R.string.menu_list));
        enableBackButton();

    }


    private void initFunctionality() {

        subMenusAdapter = new SubMenuAdapter(getApplicationContext(), subMenuItemList);
        rvSubMenus.setAdapter(subMenusAdapter);

        showLoader();
        loadSubMenus();

    }

    public void loadSubMenus() {
        ApiUtils.getApiInterface().getSubMenus(mSelectedMenuId).enqueue(new Callback<SubMenu>() {
            @Override
            public void onResponse(Call<SubMenu> call, Response<SubMenu> response) {
                if (response.isSuccessful()) {
                    subMenuItemList.addAll(response.body().getSubMenus());
                    subMenusAdapter.notifyDataSetChanged();
                    hideLoader();
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<SubMenu> call, Throwable t) {
                hideLoader();
                showEmptyView();

            }
        });
    }

    public void initListener() {
        subMenusAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                SubMenuItem clickedSubMenu = subMenuItemList.get(position);
                if (clickedSubMenu.getSubMenuItems().size() == 0) {
                    switch (clickedSubMenu.getObject()) {
                        case AppConstant.MENU_ITEM_CATEGORY:
                            Category category = new Category(clickedSubMenu.getObjectID(), clickedSubMenu.getTitle(), 0.0, 0.0);
                            List<Category> categories = new ArrayList<>();
                            categories.add(category);
                            ActivityUtils.getInstance().invokeSubCategoryList(mActivity, SubCategoryListActivity.class, (ArrayList) categoryList, (ArrayList) categories, false);
                            break;
                        case AppConstant.MENU_ITEM_PAGE:
                        case AppConstant.MENU_ITEM_CUSTOM:
                            ActivityUtils.getInstance().invokeCustomPostAndLink(mActivity, CustomLinkAndPageActivity.class, clickedSubMenu.getTitle(), clickedSubMenu.getUrl(), false);
                            break;
                        case AppConstant.MENU_ITEM_POST:
                            ActivityUtils.getInstance().invokePostDetails(mActivity, PostDetailsActivity.class, clickedSubMenu.getObjectID().intValue(), false);
                            break;
                        default:
                            break;
                    }

                } else {
                    ActivityUtils.getInstance().invokeSubSubMenuList(mActivity, SubSubMenuListActivity.class, (ArrayList) categoryList, clickedSubMenu, false);
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
