package inc.mesa.mesanews.dep;

import android.content.Context;

import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.client.RetrofitManager;
import inc.mesa.mesanews.data.source.NewsRepository;

public class DependencyProvider {

    private static DependencyBundle bundle;

    public static void init(final Context context) {
        bundle = new DependencyBundle(context);
    }

    public static NewsRepository getNewsRepository() {
        return bundle.getNewsRepository();
    }

    public static RetrofitManager getRetrofitManager() {
        return bundle.getRetrofitManager();
    }

    public static AuthManager getAuthManager() {
        return bundle.getAuthManager();
    }
}
