package headlines.moving.com.movingheadlines;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import gif.decoder.*;


public class MainActivity extends Activity {

    GoogleApiClient mGoogleApiClient;
    public String nodeId;
    public String GIFNAME;
    public String TARGETLINK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Load the gif from Drawables
                        SurfaceView gifSurfaceView = (SurfaceView) findViewById(R.id.gifSurfaceView);
                        GifRun gif = new GifRun();
                        // get the gif to be loaded
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null){
                            GIFNAME = bundle.getString("GIFNAME");
                            TARGETLINK = bundle.getString("TARGETLINK");
                            Resources resources = getApplicationContext().getResources();
                            final int resourceId = resources.getIdentifier(GIFNAME, "drawable",
                                    getApplicationContext().getPackageName());
                            gif.LoadGiff(gifSurfaceView,getApplicationContext(),resourceId);
                        }

                        Button btnOpenOnPhone = (Button) findViewById(R.id.btnOpenOnPhone);
                        btnOpenOnPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getPairedDevice();
                                sendNotification(nodeId,TARGETLINK);

                            }
                        });

                    }
                });

            }
        });

    }

    private void getPairedDevice(){

      new Thread(new Runnable() {
          @Override
          public void run() {
              mGoogleApiClient = getGoogleApiClient(getApplicationContext());
              mGoogleApiClient.blockingConnect(300, TimeUnit.MILLISECONDS);
              NodeApi.GetConnectedNodesResult result =
                      Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
              List<Node> nodes = result.getNodes();
              if (nodes.size() > 0) {
                  nodeId = nodes.get(0).getId();

                  Log.d("Node",nodeId);
              }
              mGoogleApiClient.disconnect();

          }
      }).start();

     }


    private void sendNotification(final String deviceId, final String Message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient = getGoogleApiClient(getApplicationContext());
                mGoogleApiClient.blockingConnect(300, TimeUnit.MILLISECONDS);

                if (deviceId != null){

                    Wearable.MessageApi.sendMessage(mGoogleApiClient,deviceId,Message, null).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()){
                                Log.d("Error",sendMessageResult.getStatus().getStatusMessage());
                            }
                        }
                    });
                }

                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    private GoogleApiClient getGoogleApiClient(Context context){
        return
                new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
    }

    private Thread[] getAllThreads(){

        Set<Thread> currentThreads = Thread.getAllStackTraces().keySet();
        return currentThreads.toArray(new Thread[currentThreads.size()]);
    }


}
