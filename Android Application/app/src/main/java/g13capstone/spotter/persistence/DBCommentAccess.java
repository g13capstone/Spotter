package g13capstone.spotter.persistence;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.objects.Comment;

/**
 * Created by Vital on 2018-01-16.
 */

public class DBCommentAccess extends AsyncTask<String, String, List<Comment>> {

    private Activity activity;
    private List<Comment> commentArrayList;

    public DBCommentAccess(Activity activity){
        this.activity = activity;
    }

    @Override
    protected List<Comment> doInBackground(String... params) {

        StringBuilder data = new StringBuilder("");
        commentArrayList = new ArrayList<>();

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = rd.readLine();
            data.append(line);

            commentArrayList = getCommentJSON(data.toString());

            return commentArrayList;

        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Comment> result) {
        super.onPostExecute(result);

        ProgressBar commentProgress =(ProgressBar) activity.findViewById(R.id.commentProgress);
        TextView noComment = (TextView)activity.findViewById(R.id.emptyComment);

        commentProgress.setVisibility(View.INVISIBLE);
        noComment.setVisibility(View.INVISIBLE);

        if (result == null){
            noComment.setVisibility(View.VISIBLE);
        }
        ListView comment = (ListView)activity.findViewById(R.id.comList);
        CommentAdapter customListView = new CommentAdapter(activity, commentArrayList);
        comment.setAdapter(customListView);
    }

    private List<Comment> getCommentJSON(String result) throws JSONException{
        JSONArray jsonArray = new JSONArray(result);
        List<Comment> commentArrayList = new ArrayList<>();
        for (int i=0; i <jsonArray.length(); i++){
            Comment comment = new Comment();
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            comment.setUserName(jsonObject.getString("username"));
            comment.setLogTime(jsonObject.getString("logtime"));
            comment.setComment(jsonObject.getString("comment"));

            commentArrayList.add(comment);
        }

        return commentArrayList;
    }

}
