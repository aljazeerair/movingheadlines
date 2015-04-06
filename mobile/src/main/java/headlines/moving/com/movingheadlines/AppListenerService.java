package headlines.moving.com.movingheadlines;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class AppListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent){

        String urlToNavigate = messageEvent.getPath();
        Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToNavigate));
        startActivity(openBrowser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}