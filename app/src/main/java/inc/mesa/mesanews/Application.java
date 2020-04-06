package inc.mesa.mesanews;

import com.facebook.stetho.Stetho;

import inc.mesa.mesanews.dep.DependencyProvider;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        DependencyProvider.init(this);
    }
}
