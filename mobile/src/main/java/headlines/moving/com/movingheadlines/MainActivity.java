package headlines.moving.com.movingheadlines;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.koushikdutta.ion.Ion;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MainActivity extends ActionBarActivity{

    public List<Headline> headlines;
    GoogleApiClient mGoogleApiClient;
    public String nodeId;
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";
    public String ACTION_KEY = "com.movingheadlines.headline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize Parse client
        // FIXME: move the keys to an external file
        Parse.initialize(this, "L2QNJrlHXLCC9K6NUGiuxoVO3v68lpZWIo4Unmpp", "Gsb5zjuY3cWJXcPalFyjdzPRjQiqZBjO7mQkezLn");
        // get paired device
        getPairedDevice();
        // populate the headlines list
        headlines = new LinkedList<Headline>();
        populate();

        // Random gif
        ImageView gifImageView = (ImageView) findViewById(R.id.gifImageView);
        Ion.with(gifImageView).load("http://www.reactiongifs.com/r/fbi.gif");

        // push random headlines to the wearables
        Button btnRandom = (Button)findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Clicked", "Notification sent to "+ nodeId);
                 getPairedDevice();
                // send a combination of GifName + TargetLink
                Headline randomHeadline = getRandomHeadline(headlines);
                sendNotification(nodeId, randomHeadline.GifName + ";" + randomHeadline.TargetLink);
                Log.d("Sent", randomHeadline.GifName+ ";" + randomHeadline.TargetLink);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populate(){
       // get headlines from Parse an populate Headlines collection
        ParseQuery<ParseObject> getAllHeadlines = ParseQuery.getQuery("Headline");
        getAllHeadlines.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                for (int i = 0; i < parseObjects.size(); i++) {
                    Headline headline = new Headline(parseObjects.get(i).get("GifName").toString(),parseObjects.get(i).get("TargetLink").toString());
                    headlines.add(headline);
                }
            }
        });
    }

    // get random headline

    public Headline getRandomHeadline(List<Headline> headlines){
         return headlines.get(randInt(0,headlines.size() -1));
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
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
                              Log.d("Error4",sendMessageResult.getStatus().getStatusMessage());
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
}
