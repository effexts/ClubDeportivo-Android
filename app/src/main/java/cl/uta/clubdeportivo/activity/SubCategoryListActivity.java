package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.CategoryPagerAdapter;
import cl.uta.clubdeportivo.api.models.category.Category;
import cl.uta.clubdeportivo.data.constant.AppConstant;

import java.util.ArrayList;
import java.util.List;


/**

 */

public class SubCategoryListActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private ViewPager mViewPager;
    private CategoryPagerAdapter categoryAdapter;
    private TabLayout tabLayout;

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
        mActivity = SubCategoryListActivity.this;
        mContext = mActivity.getApplicationContext();

        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            categoryList = getIntent().getParcelableArrayListExtra(AppConstant.BUNDLE_KEY_CATEGORY_LIST);
            subCategoryList = getIntent().getParcelableArrayListExtra(AppConstant.BUNDLE_KEY_SUB_CATEGORY_LIST);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        initToolbar();
        setToolbarTitle(getString(R.string.category_list));
        enableBackButton();

        initLoader();

    }


    private void initFunctionality() {

        categoryAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), (ArrayList) categoryList, (ArrayList) subCategoryList);
        mViewPager.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(mViewPager);

        hideLoader();

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
