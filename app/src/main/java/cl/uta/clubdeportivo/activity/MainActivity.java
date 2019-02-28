package cl.uta.clubdeportivo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.adapters.FeaturedPagerAdapter;
import cl.uta.clubdeportivo.adapters.HomeSelectableCategoryAdapter;
import cl.uta.clubdeportivo.adapters.MenuCommonAdapter;
import cl.uta.clubdeportivo.adapters.PostsAdapter;
import cl.uta.clubdeportivo.api.http.ApiUtils;
import cl.uta.clubdeportivo.api.models.category.Category;
import cl.uta.clubdeportivo.api.models.menus.AnyMenu;
import cl.uta.clubdeportivo.api.models.menus.MainMenu;
import cl.uta.clubdeportivo.api.models.menus.SubMenu;
import cl.uta.clubdeportivo.api.models.menus.SubMenuItem;
import cl.uta.clubdeportivo.api.models.posts.post.Post;
import cl.uta.clubdeportivo.data.constant.AppConstant;
import cl.uta.clubdeportivo.data.sqlite.NotificationDbController;
import cl.uta.clubdeportivo.data.sqlite.SelectableCatDbController;
import cl.uta.clubdeportivo.listeners.ListItemClickListener;
import cl.uta.clubdeportivo.models.NotificationModel;
import cl.uta.clubdeportivo.models.SelectableCategoryModel;
import cl.uta.clubdeportivo.utility.ActivityUtils;
import cl.uta.clubdeportivo.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    // Variables
    private Activity mActivity;
    private Context mContext;
    private int perPageCount = 5;
    private int currentCategoryIndex = 0;

    // Views
    private ImageButton imgBtnSearch;
    private ScrollView lytContent;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout rlTopRecentPost, rlNotificationView, rlSelectableCategory;
    private TextView tvFeaturedSeeMore, tvRecentSeeMore;


    // List utilities

    // menu list
    private List<MainMenu> mainMenuList;
    private List<SubMenuItem> subMenuItems;
    private List<AnyMenu> anyMenuList;
    private MenuCommonAdapter menusAdapter = null;
    private RecyclerView rvMenus;

    // featured list
    private List<Post> featuredPostList;
    private RelativeLayout mFeaturedLayout;
    private ViewPager pagerFeaturedPost;
    private FeaturedPagerAdapter featuredPostAdapter = null;

    // recent list
    private Post firstPost = null;
    private List<Post> recentPostList;
    private RecyclerView postsRecyclerView;
    private PostsAdapter recentPostAdapter = null;
    private GridLayoutManager mLayoutManager;
    private ProgressBar pbSectionLoader, pbSelectableCatLoader;

    // seleccionar lista de categorias
    private List<SelectableCategoryModel> selectableCategoryList;
    private List<List<Post>> categoryWisePostList;
    private HomeSelectableCategoryAdapter selectableCatAdapter = null;
    private RecyclerView rvSelectableCategories;
    private SelectableCatDbController selectableCatDbController;

    // lista de categorias
    private List<Category> categoryList;
    private List<Category> categoryListZeroParent;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initVar();
        initView();
        loadData();
        initListener();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                }
                else {
                    ActivityUtils.getInstance().invokeActivity(mActivity, LoginActivity.class, false);
                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthListener != null ){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //unregister broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newNotificationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter(AppConstant.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).registerReceiver(newNotificationReceiver, intentFilter);

        initNotification();

    }

    // received new broadcast
    private BroadcastReceiver newNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            initNotification();
        }
    };

    @Override
    public void onBackPressed() {
        AppUtils.tapToExit(mActivity);
    }

    private void initVar() {
        mActivity = MainActivity.this;
        mContext = getApplicationContext();

        // menu list
        mainMenuList = new ArrayList<>();
        subMenuItems = new ArrayList<>();
        anyMenuList = new ArrayList<>();

        // featured list
        featuredPostList = new ArrayList<>();

        // recent list
        recentPostList = new ArrayList<>();

        // selectable categories list
        selectableCategoryList = new ArrayList<>();
        categoryWisePostList = new ArrayList<>();

        // categories list
        categoryList = new ArrayList<>();
        categoryListZeroParent = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

    }

    private void initView() {
        setContentView(R.layout.activity_main);

        rlNotificationView = (RelativeLayout) findViewById(R.id.notificationView);
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
        lytContent = (ScrollView) findViewById(R.id.content_layout);


        rvMenus = (RecyclerView) findViewById(R.id.rvMenus);
        rvMenus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mFeaturedLayout = (RelativeLayout) findViewById(R.id.lytPagerContainer);
        pagerFeaturedPost = (ViewPager) findViewById(R.id.pagerFeaturedPost);
        tvFeaturedSeeMore = (TextView) findViewById(R.id.txt_see_more_featured);


        rlTopRecentPost = (RelativeLayout) findViewById(R.id.rl_top);
        postsRecyclerView = (RecyclerView) findViewById(R.id.rvPosts);
        mLayoutManager = new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false);
        postsRecyclerView.setLayoutManager(mLayoutManager);
        pbSectionLoader = (ProgressBar) findViewById(R.id.pbSectionLoader);
        pbSelectableCatLoader = (ProgressBar) findViewById(R.id.pbSelectableCatLoader);
        tvRecentSeeMore = (TextView) findViewById(R.id.txt_see_more_recent);


        rlSelectableCategory = (RelativeLayout) findViewById(R.id.lytSelectableCatContainer);
        rvSelectableCategories = (RecyclerView) findViewById(R.id.rvSelectableCategories);
        rvSelectableCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);


        // menus adapter
        menusAdapter = new MenuCommonAdapter(mActivity, (ArrayList) anyMenuList);
        rvMenus.setAdapter(menusAdapter);

        // recent post adapter
        recentPostAdapter = new PostsAdapter(mActivity, (ArrayList) recentPostList);
        postsRecyclerView.setAdapter(recentPostAdapter);

        // selectable categories adapter
        selectableCatAdapter = new HomeSelectableCategoryAdapter(mContext, mActivity, (ArrayList) selectableCategoryList, (ArrayList) categoryWisePostList);
        rvSelectableCategories.setAdapter(selectableCatAdapter);



        initToolbar();
        initDrawer(true);
        initLoader();

    }


    private void loadData() {

        showLoader();
        loadMenus();
        loadFeaturedPosts();
        loadRecentPosts();
        loadSelectableCatWisePosts();

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    public void loadSelectableCatWisePosts() {
        currentCategoryIndex = 0;
        categoryWisePostList.clear();

        // selectable Category Database Controller
        if (selectableCatDbController == null) {
            selectableCatDbController = new SelectableCatDbController(mContext);
        }
        selectableCategoryList.clear();
        selectableCategoryList.addAll(selectableCatDbController.getAllData());


        if (!selectableCategoryList.isEmpty()) {
            loadCategoryWisePosts(selectableCategoryList.get(currentCategoryIndex).getCategoryId());
        } else {
            pbSelectableCatLoader.setVisibility(View.GONE);
        }

    }

    private void initListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lytContent.setVisibility(View.GONE);
                loadData();
            }
        });

        //notification layout click listener
        rlNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdThenActivity(NotificationActivity.class, false);
            }
        });

        // Search button click listener
        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdThenActivity(SearchActivity.class, false);
            }
        });


        // Menu list item click listener
        menusAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                if (anyMenuList.get(position).isMainMenu()) {
                    int clickedItemId = anyMenuList.get(position).getID().intValue();
                    ActivityUtils.getInstance().invokeSubMenuList(mActivity, SubMenuListActivity.class, (ArrayList) categoryList, clickedItemId, false);
                } else {
                    SubMenuItem clickedSubMenu = subMenuItems.get(position);

                    /* If sub menu has child it will go sub sub menu
                     * else it redirects to relevant areas like category, page, custom or post
                     */
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
            }
        });

        //featured pager adapter listener
        pagerFeaturedPost.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });

        tvFeaturedSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().invokePostList(mActivity, PostListActivity.class, AppConstant.DEFAULT_CATEGORY_ID, getString(R.string.featured_posts), true, false);
            }
        });


        // Recent post large item click listener

        rlTopRecentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstPost != null) {
                    int firstPostId = firstPost.getID().intValue();
                    ActivityUtils.getInstance().invokePostDetails(mActivity, PostDetailsActivity.class, firstPostId, false);
                }
            }
        });

        // Recent post list item click listener
        recentPostAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                int id = view.getId();
                int clickedPostId = recentPostList.get(position).getID().intValue();

                if (id == R.id.card_view_top) {
                    ActivityUtils.getInstance().invokePostDetails(mActivity, PostDetailsActivity.class, clickedPostId, false);
                }
            }
        });

        tvRecentSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().invokePostList(mActivity, PostListActivity.class, AppConstant.DEFAULT_CATEGORY_ID, getString(R.string.recent_posts), false, false);
            }
        });

        // Selectable categories list item click listener
        selectableCatAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                SelectableCategoryModel clickedSelectableCategory = selectableCategoryList.get(position);
                switch (view.getId()) {
                    case R.id.txt_see_more_category:
                        ActivityUtils.getInstance().invokePostList(mActivity, PostListActivity.class, clickedSelectableCategory.getCategoryId(), clickedSelectableCategory.getCategoryName(), false, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }


    //================= Cargar menu ================//

    public void loadMenus() {

        ApiUtils.getApiInterface().getMenus().enqueue(new Callback<List<MainMenu>>() {
            @Override
            public void onResponse(Call<List<MainMenu>> call, Response<List<MainMenu>> response) {
                if (response.isSuccessful()) {

                    if (!mainMenuList.isEmpty() || !anyMenuList.isEmpty()) {
                        mainMenuList.clear();
                        anyMenuList.clear();
                    }
                    mainMenuList.addAll(response.body());

                    //===== if multiple main menus are available
                    if (mainMenuList.size() > 1) {
                        for (MainMenu mainMenu : mainMenuList) {
                            AnyMenu anyMenu = new AnyMenu(mainMenu.getID(), mainMenu.getName(), true);
                            anyMenuList.add(anyMenu);
                        }
                        menusAdapter.notifyDataSetChanged();
                    } else {
                        // for single main menu but has child(s)
                        loadSubmenu();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MainMenu>> call, Throwable t) {
                showEmptyView();
                t.printStackTrace();
            }
        });

    }

    //================= Load sub menus ================//

    public void loadSubmenu() {
        // get first menu id
        if (!mainMenuList.isEmpty()) {
            int menuID = mainMenuList.get(0).getID().intValue();
            ApiUtils.getApiInterface().getSubMenus(menuID).enqueue(new Callback<SubMenu>() {
                @Override
                public void onResponse(Call<SubMenu> call, Response<SubMenu> response) {
                    if (response.isSuccessful()) {

                        if (!subMenuItems.isEmpty() || !anyMenuList.isEmpty()) {
                            subMenuItems.clear();
                            anyMenuList.clear();
                        }
                        subMenuItems.addAll(response.body().getSubMenus());

                        for (SubMenuItem subMenuItem : subMenuItems) {
                            AnyMenu anyMenu = new AnyMenu(subMenuItem.getID(), subMenuItem.getTitle(), false);
                            anyMenuList.add(anyMenu);
                        }
                        menusAdapter.notifyDataSetChanged();

                    }
                    // hide common loader
                    hideLoader();
                }

                @Override
                public void onFailure(Call<SubMenu> call, Throwable t) {
                    t.printStackTrace();

                    // hide common loader
                    hideLoader();
                    // show empty view
                    showEmptyView();
                }
            });
        }

    }

    //================= Cargar publicaiones destacadas ================//

    public void loadFeaturedPosts() {

        ApiUtils.getApiInterface().getFeaturedPosts(AppConstant.DEFAULT_PAGE).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    lytContent.setVisibility(View.VISIBLE);

                    if (!featuredPostList.isEmpty()) {
                        featuredPostList.clear();
                    }
                    featuredPostList.addAll(response.body());

                    if (featuredPostList.size() > 0) {
                        mFeaturedLayout.setVisibility(View.VISIBLE);
                    }

                    // featured post pager adapter
                    featuredPostAdapter = new FeaturedPagerAdapter(mActivity, featuredPostList);
                    pagerFeaturedPost.setAdapter(featuredPostAdapter);

                    //featured post item click listener
                    featuredPostAdapter.setItemClickListener(new ListItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            int clickedPostId = featuredPostList.get(position).getID().intValue();
                            ActivityUtils.getInstance().invokePostDetails(mActivity, PostDetailsActivity.class, clickedPostId, false);
                        }
                    });

                }
                hideLoader();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
            }
        });
    }

    //================= Cargar publicaciones recientes ================//

    public void loadRecentPosts() {

        ApiUtils.getApiInterface().getRecentPosts(AppConstant.DEFAULT_PAGE).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    if (!recentPostList.isEmpty()) {
                        recentPostList.clear();
                    }

                    recentPostList.addAll(response.body());

                    if (recentPostList.size() > 0) {

                        /*
                        *just uncomment this section to show the big image view at the top of recent posts.
                        *no more further modification needed
                        */

                        /*

                        rlTopRecentPost.setVisibility(View.VISIBLE);

                        firstPost = recentPostList.get(0);
                        recentPostList.remove(0);
                        tvRecentTop.setText(Html.fromHtml(firstPost.getTitle().getRendered()));

                        String imgUrl = null;
                        if (firstPost.getEmbedded().getWpFeaturedMedias().size() >= 1) {
                            imgUrl = firstPost.getEmbedded().getWpFeaturedMedias().get(0).getMediaDetails().getSizes().getFullSize().getSourceUrl();
                        }

                        if (imgUrl != null) {
                            Glide.with(mActivity)
                                    .load(imgUrl)
                                    .into(imgRecentTop);
                        }

                        */

                        //<!----------  commenting section ends here ------------>

                        recentPostAdapter.notifyDataSetChanged();

                    }
                } else {
                    showEmptyView();
                }
                // hide section loader
                pbSectionLoader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showEmptyView();
                t.printStackTrace();
            }
        });
    }

    //=============== Load Category wise posts =============//

    public void loadCategoryWisePosts(int selectedCategoryId) {
        ApiUtils.getApiInterface().getPostsByCategory(AppConstant.DEFAULT_PAGE, selectedCategoryId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> postList = new ArrayList<>();
                    postList.addAll(response.body());
                    categoryWisePostList.add(postList);
                    currentCategoryIndex++;
                    if (currentCategoryIndex < selectableCategoryList.size()) {
                        loadCategoryWisePosts(selectableCategoryList.get(currentCategoryIndex).getCategoryId());
                    } else {
                        selectableCatAdapter.notifyDataSetChanged();
                        rvSelectableCategories.setVisibility(View.VISIBLE);
                        pbSelectableCatLoader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                pbSelectableCatLoader.setVisibility(View.GONE);
            }
        });
    }


    public void initNotification() {
        NotificationDbController notificationDbController = new NotificationDbController(mContext);
        TextView notificationCount = findViewById(R.id.notificationCount);
        notificationCount.setVisibility(View.VISIBLE);
        notificationCount.setVisibility(View.INVISIBLE);

        ArrayList<NotificationModel> notiArrayList = notificationDbController.getUnreadData();

        if (notiArrayList != null && !notiArrayList.isEmpty()) {
            int totalUnread = notiArrayList.size();
            if (totalUnread > 0) {
                notificationCount.setVisibility(View.VISIBLE);
                notificationCount.setText(String.valueOf(totalUnread));
            } else {
                notificationCount.setVisibility(View.INVISIBLE);
            }
        }

    }

}
