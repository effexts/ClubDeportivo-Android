package cl.uta.clubdeportivo.utility;

import android.app.Activity;
import android.content.Intent;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.activity.NotificationDetailsActivity;
import cl.uta.clubdeportivo.api.models.category.Category;
import cl.uta.clubdeportivo.api.models.menus.SubMenuItem;
import cl.uta.clubdeportivo.data.constant.AppConstant;

import java.util.ArrayList;


/**

 */
public class ActivityUtils {

    private static ActivityUtils sActivityUtils = null;

    public static ActivityUtils getInstance() {
        if (sActivityUtils == null) {
            sActivityUtils = new ActivityUtils();
        }
        return sActivityUtils;
    }

    public void invokeActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeSubCategoryList(Activity activity, Class<?> tClass, ArrayList<Category> allCategoryList, ArrayList<Category> allSubCategoryList, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putParcelableArrayListExtra(AppConstant.BUNDLE_KEY_CATEGORY_LIST, (ArrayList) allCategoryList);
        intent.putParcelableArrayListExtra(AppConstant.BUNDLE_KEY_SUB_CATEGORY_LIST, (ArrayList) allSubCategoryList);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeSubMenuList(Activity activity, Class<?> tClass, ArrayList<Category> allCategoryList, int clickedPosition, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putParcelableArrayListExtra(AppConstant.BUNDLE_KEY_CATEGORY_LIST, (ArrayList) allCategoryList);
        intent.putExtra(AppConstant.BUNDLE_KEY_MENU_ID, clickedPosition);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }


    public void invokeSubSubMenuList(Activity activity, Class<?> tClass, ArrayList<Category> allCategoryList, SubMenuItem clickedSubMenu, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putParcelableArrayListExtra(AppConstant.BUNDLE_KEY_CATEGORY_LIST, (ArrayList) allCategoryList);
        intent.putExtra(AppConstant.BUNDLE_KEY_SUB_MENU, clickedSubMenu);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokePostDetails(Activity activity, Class<?> tClass, int clickedPosition, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_POST_ID, clickedPosition);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokePostDetailsForAppLink(Activity activity, Class<?> tClass, int clickedPosition, boolean fromAppLink, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_POST_ID, clickedPosition);
        intent.putExtra(AppConstant.BUNDLE_FROM_APP_LINK, fromAppLink);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeCustomPostAndLink(Activity activity, Class<?> tClass, String pageTitle, String pageUrl, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_TITLE, pageTitle);
        intent.putExtra(AppConstant.BUNDLE_KEY_URL, pageUrl);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokePostList(Activity activity, Class<?> tClass, int categoryId, String categoryTitle, boolean isFromFeatured, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_CATEGORY_ID, categoryId);
        intent.putExtra(AppConstant.BUNDLE_KEY_CATEGORY_TITLE, categoryTitle);
        intent.putExtra(AppConstant.BUNDLE_KEY_FEATURED, isFromFeatured);
        activity.startActivityForResult(intent, AppConstant.REQUEST_CODE_COMMENT);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeNotificationDetails(Activity activity, String title, String message, String postId){
        Intent intent = new Intent(activity, NotificationDetailsActivity.class);
        intent.putExtra(AppConstant.BUNDLE_KEY_TITLE, title);
        intent.putExtra(AppConstant.BUNDLE_KEY_MESSAGE, message);
        intent.putExtra(AppConstant.BUNDLE_KEY_POST_ID, postId);
        activity.startActivity(intent);
    }

    public void invokeLeftToRightActivityAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);
    }

}
