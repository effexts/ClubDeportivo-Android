package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.CategoryPagerAdapter;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.category.Category;
import cl.uta.clubdeportivo.api.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */

public class CategoryListActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private ViewPager mViewPager;
    private CategoryPagerAdapter mCategoryPagerAdapter;
    private TabLayout tabLayout;

    private int mPerPage = 5;
    private List<Category> categoryList;
    private List<Category> subCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
    }

    private void initVar() {
        mActivity = CategoryListActivity.this;
        mContext = mActivity.getApplicationContext();

        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        initLoader();

        initToolbar();
        setToolbarTitle(getString(R.string.category_list));
        enableBackButton();

    }

    private void initFunctionality() {

        mCategoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), (ArrayList) categoryList, (ArrayList) subCategoryList);
        mViewPager.setAdapter(mCategoryPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        showLoader();
        loadCategories();

    }

    public void loadCategories() {
        ApiUtils.getApiInterface().getCategories(mPerPage).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {

                    int totalPages = Integer.parseInt(response.headers().get(HttpParams.HEADER_TOTAL_PAGE));

                    if (totalPages > 1) {
                        mPerPage = mPerPage * totalPages;
                        loadCategories();

                    } else {
                        categoryList.addAll(response.body());
                        for (Category category : categoryList) {
                            if (category.getParent().intValue() == 0) {
                                subCategoryList.add(category);
                            }
                        }

                        mCategoryPagerAdapter.notifyDataSetChanged();

                    }

                    hideLoader();
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

                hideLoader();
                showEmptyView();
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
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
