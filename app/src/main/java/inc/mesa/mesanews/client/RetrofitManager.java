package inc.mesa.mesanews.client;

import androidx.annotation.Nullable;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String BASE_URL = "https://mesa-news-api.herokuapp.com/v1/client/";
    private static RetrofitManager INSTANCE;

    private RetrofitManager() {}

    public static RetrofitManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitManager();
        }

        return INSTANCE;
    }

    public Retrofit getClientInstance(@Nullable final Converter.Factory factory) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL);

        if (factory != null) {
            builder.addConverterFactory(factory);
        } else {
            builder.addConverterFactory(GsonConverterFactory.create());
        }

        return builder.build();
    }

}
