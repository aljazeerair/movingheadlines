package headlines.moving.com.movingheadlines;


import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import android.content.Context;

public class WearListenerService extends WearableListenerService {

    private GoogleApiClient mGoogleApiClient;
        @Override
        public void onMessageReceived(MessageEvent messageEvent){
             Log.d("Message",messageEvent.getPath());

            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
            Intent intent = new Intent();
            intent.setAction("com.movingheadlines.MAIN");
            // specify which gif to be loaded
            Log.d("Message",messageEvent.getPath());
            // split the message into GifName + TargetLink
            String message = messageEvent.getPath();
            String gifName = message.split(";")[0];
            String targetLink = message.split(";")[1];
            intent.putExtra("GIFNAME", gifName);
            intent.putExtra("TARGETLINK", targetLink);
             //
            getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

}
