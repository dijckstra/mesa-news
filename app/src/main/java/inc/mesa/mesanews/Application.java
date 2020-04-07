package inc.mesa.mesanews;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;

import inc.mesa.mesanews.dep.DependencyProvider;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppEventsLogger.activateApp(this);
        Stetho.initializeWithDefaults(this);
        DependencyProvider.init(this);
    }
}
