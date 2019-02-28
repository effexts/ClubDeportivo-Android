package cl.uta.clubdeportivo.api.params;


public class HttpParams {

    // aqui va el hosting
    public static final String BASE_URL = "http://clubdeportivo.uta.cl/";

    public static final String API_TEXT_PER_PAGE = "per_page";
    public static final String API_TEXT_PAGE = "page";
    public static final String API_TEXT_CATEGORIES = "categories";
    public static final String API_RECENT_ITEM_PER_PAGE = "20";
    public static final String API_MAXIMUM_ITEM_PER_PAGE = "10";
    public static final String API_TEXT_ID = "id";
    public static final String API_EMBED = "_embed=true";
    public static final String API_FEATURED = "sticky=true";
    public static final String API_POST = "post";
    public static final String API_PARENT = "parent";
    public static final String API_TEXT_SEARCH = "search";
    public static final String API_ACT_ID = "id";
    public static final String API_FIREBASE_UID = "uid";

    public static final String API_CATEGORIES = "wp-json/wp/v2/categories?page=1&";
    public static final String API_FEATURED_POSTS = "wp-json/wp/v2/posts?per_page=" + API_RECENT_ITEM_PER_PAGE + "&" + API_EMBED + "&" + API_FEATURED;
    public static final String API_RECENT_POSTS = "wp-json/wp/v2/posts?per_page=" + API_RECENT_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_CATEGORISED_ALL_POST = "wp-json/wp/v2/posts?per_page=" + API_MAXIMUM_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_CATEGORISED_TOP_POST = "wp-json/wp/v2/posts?per_page=" + API_MAXIMUM_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_SEARCHED_POSTS = "wp-json/wp/v2/posts?per_page=" + API_MAXIMUM_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_POST_DETAILS = "wp-json/wp/v2/posts/{" + API_TEXT_ID + "}?" + "&" + API_EMBED;
    public static final String API_MENUS = "wp-json/wp-api-menus/v2/menus/";
    public static final String API_SUB_MENUS = "wp-json/wp-api-menus/v2/menus/{" + API_TEXT_ID + "}";
    public static final String API_APP_USERS = "wp-json/wp/v2/appuser/";
    public static final String API_WP_TOKEN = "/wp-json/jwt-auth/v1/token";
    public static final String API_GET_ACTDEPS = "/wp-json/wp/v2/actividades?per_page=50";
    public static final String API_GET_ACTDEPS_DETAILS = "/wp-json/wp/v2/actividades/{"+ API_ACT_ID +"}";
    public static final String API_GET_MISACT_DEP = "/wp-json/wp/v2/appuser";



    public static final String HEADER_TOTAL_ITEM = "x-wp-total";
    public static final String HEADER_TOTAL_PAGE = "x-wp-totalpages";


}
