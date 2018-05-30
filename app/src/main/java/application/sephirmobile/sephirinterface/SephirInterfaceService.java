package application.sephirmobile.sephirinterface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface SephirInterfaceService {

    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, String> getMap);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, String> postMap, @QueryMap Map<String, String> getMap);
}
