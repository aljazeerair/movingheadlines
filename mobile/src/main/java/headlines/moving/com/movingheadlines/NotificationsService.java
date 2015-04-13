package headlines.moving.com.movingheadlines;

import android.app.IntentService;
import android.content.Intent;


public class NotificationsService extends IntentService {

    public NotificationsService() {
        super("NotificationsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

        }
    }

}
