package cl.uta.clubdeportivo.api.http;

import com.google.gson.JsonElement;

import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import cl.uta.clubdeportivo.api.models.appusers.AppUserBody;
import cl.uta.clubdeportivo.api.models.appusers.AppUsers;
import cl.uta.clubdeportivo.api.models.appusers.AppUsersTalleres;
import cl.uta.clubdeportivo.api.models.appusers.TokenWP;
import cl.uta.clubdeportivo.api.models.appusers.TokenWPBody;
import cl.uta.clubdeportivo.api.models.category.Category;
import cl.uta.clubdeportivo.api.models.menus.MainMenu;
import cl.uta.clubdeportivo.api.models.menus.SubMenu;
import cl.uta.clubdeportivo.api.models.posts.post.Post;
import cl.uta.clubdeportivo.api.models.posts.post.PostDetails;
import cl.uta.clubdeportivo.api.params.HttpParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**

 */

public interface ApiInterface {

    @GET(HttpParams.API_CATEGORIES)
    Call<List<Category>> getCategories(@Query(HttpParams.API_TEXT_PER_PAGE) int pageCount);

    @GET(HttpParams.API_FEATURED_POSTS)
    Call<List<Post>> getFeaturedPosts(@Query(HttpParams.API_TEXT_PAGE) int pageCount);

    @GET(HttpParams.API_RECENT_POSTS)
    Call<List<Post>> getRecentPosts(@Query(HttpParams.API_TEXT_PAGE) int pageCount);

    @GET(HttpParams.API_CATEGORISED_ALL_POST)
    Call<List<Post>> getPostsByCategory(@Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_CATEGORIES) int categoryId);

    @GET(HttpParams.API_POST_DETAILS)
    Call<PostDetails> getPostDetails(@Path(HttpParams.API_TEXT_ID) int postId);

    @GET(HttpParams.API_MENUS)
    Call<List<MainMenu>> getMenus();

    @GET(HttpParams.API_SUB_MENUS)
    Call<SubMenu> getSubMenus(@Path(HttpParams.API_TEXT_ID) int subMenuID);

    @GET(HttpParams.API_SEARCHED_POSTS)
    Call<List<Post>> getSearchedPosts(@Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_SEARCH) String searchText);

    @GET(HttpParams.API_APP_USERS)
    Call<List<AppUsers>> getAppUsers(@QueryName String... filters);


    @FormUrlEncoded
    @POST(HttpParams.API_WP_TOKEN)
    Call<TokenWP> getToken(@Field("username") String user, @Field("password") String passwd);

    //@Headers({ "Content-Type: application/json;charset=UTF-8"})

    @POST(HttpParams.API_APP_USERS)
    @Headers({ "Content-Type: application/json"})
    Call<AppUsers> addUser2(@Header("Authorization") String token, @Body AppUserBody appUserBody);

    @FormUrlEncoded
    @Headers({ "Content-Type: application/json"})
    @POST(HttpParams.API_APP_USERS)
    Call<AppUsers> addUser(@Header("Authorization") String token,
                           @Field("status") String status,
                           @Field("title") String title,
                           @Field("user_id") String user_id,
                           @Field("nombres") String nombres,
                           @Field("correo") String correo
    );

    @GET(HttpParams.API_GET_ACTDEPS)
    Call<List<ActDeportivas>> getAllActDeportivas();

    @GET(HttpParams.API_GET_ACTDEPS_DETAILS)
    Call<ActDeportivas> getActDeportivasDetails(@Path(HttpParams.API_ACT_ID) int actDepID );

    @GET(HttpParams.API_GET_MISACT_DEP)
    Call<JsonElement> getMisActividades(@Query("filter[meta_key]") String user_id, @Query("filter[meta_value]") String uid );

    @POST(HttpParams.API_POST_MISACT_DEP)
    @Headers({ "Content-Type: application/json"})
    Call<JsonElement> postListaTalleres(@Header("Authorization") String token, @Path(HttpParams.API_USER_ID) String user_id, @Body String lista_talleres);
}
