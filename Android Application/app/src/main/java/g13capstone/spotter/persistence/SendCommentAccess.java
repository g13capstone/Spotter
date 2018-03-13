package g13capstone.spotter.persistence;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import g13capstone.spotter.R;

public class SendCommentAccess extends AsyncTask<String, String, String>{

    @SuppressLint("StaticFieldLeak")
    private Activity activity;

    public SendCommentAccess(Activity activity){
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuilder data = new StringBuilder("");
        try{
            URL url = new URL(activity.getResources().getString(R.string.comment_send));

            // open and setup URL connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // create outputstream to write request body
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(params[0]);

            // clear stream and close
            writer.flush();
            writer.close();
            os.close();

            // get response code
            int responseCode=connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK){
                return "okay";
            }

            return "false: " + responseCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    } // end of doInBackground process

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result.equals("okay")) {
            Toast.makeText(activity, "Comment successfully submitted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Unable to send comment. Try again later.", Toast.LENGTH_SHORT).show();
        }
    } // end of onPostExecute
} // end of SendCommentAccess
