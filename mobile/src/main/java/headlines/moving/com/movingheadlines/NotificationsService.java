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
          // Initialize Parse and listen to new headlines
          // Compare both Firebase and Parse in this case
        }
    }

}
