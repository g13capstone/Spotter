package g13capstone.spotter.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.persistence.DBCommentAccess;
import g13capstone.spotter.persistence.HintAdapter;
import g13capstone.spotter.persistence.SendCommentAccess;

public class CommentActivity extends AppCompatActivity {

    private Spinner commentSpinner;
    private Spinner lotSpinner;
    private Button commentButton;
    private EditText userNameField;
    private EditText descriptionField;
    private ListView comList;
    private String[] lotIDArray;
    private String lotInput;
    private int lotInputPos;
    private HintAdapter commentAdapter;
    private ProgressBar commentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle bundle = this.getIntent().getExtras();
        String activity = bundle.getString("activity");

        getIDs();

        String []commentArray = getResources().getStringArray(R.array.comment_options_array);
        String []lotArray = getResources().getStringArray(R.array.lot_options_array);
        lotIDArray = getResources().getStringArray(R.array.lot_id_array);

        List<String> commentArrayList = Arrays.asList(commentArray);
        List<String> lotArrayList = Arrays.asList(lotArray);
        List<String> lotIDArrayList = Arrays.asList(lotIDArray);

        Button lotButton = (Button) findViewById(R.id.lotButton);

        // Create an ArrayAdapter using the string array and a default spinner layout
        commentAdapter = new HintAdapter(this, commentArrayList , R.layout.spinner_item);
        HintAdapter lotAdapter = new HintAdapter(this, lotArrayList, R.layout.spinner_item);

        //Specify the layout to use when the list of choices appear
        commentAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        lotAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        // Apply the adapter to the spinner
        commentSpinner.setAdapter(commentAdapter);
        lotSpinner.setAdapter(lotAdapter);

        commentSpinner.setSelection(commentAdapter.getCount());

        // Create the page according to the activity caller
        // From Main: default empty page
        // From Lot: load comments of the lot
        if (activity.equals("Main"))
            lotSpinner.setSelection(lotAdapter.getCount());
        else if (activity.equals("Lot")){
            String activityLot = bundle.getString("lotID");
            int spinnerSelect = 0;

            spinnerSelect = lotIDArrayList.indexOf(activityLot);

            lotSpinner.setSelection(spinnerSelect);
            new DBCommentAccess(CommentActivity.this).execute(getString(R.string.comment_link)+activityLot);

            commentProgress.setVisibility(View.VISIBLE);
            commentButton.setVisibility(View.VISIBLE);
            commentSpinner.setVisibility(View.VISIBLE);
            userNameField.setVisibility(View.VISIBLE);
            comList.setVisibility(View.VISIBLE);
            descriptionField.setVisibility(View.VISIBLE);
        }

        lotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotInput = lotSpinner.getSelectedItem().toString();
                lotInputPos = lotSpinner.getSelectedItemPosition();

                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if (!lotInput.equals("Select lot")) {
                    new DBCommentAccess(CommentActivity.this).execute(getString(R.string.comment_link)+lotIDArray[lotInputPos]);

                    commentProgress.setVisibility(View.VISIBLE);
                    commentButton.setVisibility(View.VISIBLE);
                    commentSpinner.setVisibility(View.VISIBLE);
                    userNameField.setVisibility(View.VISIBLE);
                    comList.setVisibility(View.VISIBLE);
                    descriptionField.setVisibility(View.VISIBLE);
                }
            }
        }); //end of lotButton listener

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values of layour components
                String comment = commentSpinner.getSelectedItem().toString().trim();
                String username = userNameField.getText().toString().trim();
                String descript = descriptionField.getText().toString();
                lotInputPos = lotSpinner.getSelectedItemPosition();
                boolean valid = false;

                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                valid = checkInput(comment, username);

                if (valid) {
                    commentSpinner.setSelection(commentAdapter.getCount());
                    StringBuilder query = new StringBuilder();
                    query.append("lot_id=");
                    query.append(lotIDArray[lotInputPos]);
                    query.append("&username=");
                    query.append(android.net.Uri.encode(username, "UTF-8"));
                    query.append("&comment=");
                    query.append(android.net.Uri.encode(comment, "UTF-8"));

                    if (!descript.equals(getResources().getString(R.string.enter_description)) && !descript.equals("")) {
                        query.append(android.net.Uri.encode(": ", "UTF-8"));
                        query.append(android.net.Uri.encode(descript, "UTF-8"));
                    }

                    // send comment
                    new SendCommentAccess((Activity) v.getContext()).execute(query.toString());

                    // reload comments for updating the page
                    commentProgress.setVisibility(View.VISIBLE);
                    new DBCommentAccess(CommentActivity.this).execute(getString(R.string.comment_link) + lotIDArray[lotInputPos]);

                }
            }
        }); // end of commentButton listener
    } // end of onCreate

    // check for mandatory fields
    public boolean checkInput(String comment, String username){
        boolean validInput = false;

        if (comment.equals(getResources().getString(R.string.default_comment))){
            Toast.makeText(this, "Please select a comment", Toast.LENGTH_SHORT).show();
        } else if (username.equals(getResources().getString(R.string.user_name_text)) || username.equals("")) {
            Toast.makeText(this, "Please input a username", Toast.LENGTH_SHORT).show();
        } else  {
            validInput = true;
        }

        return validInput;
    } // end of checkInput

    // initialize layout objects
    public void getIDs(){
        commentSpinner = (Spinner) findViewById(R.id.commentSpinner);
        lotSpinner = (Spinner) findViewById(R.id.lotSpinner);
        userNameField = (EditText) findViewById(R.id.userName);
        comList = (ListView)findViewById(R.id.comList);
        commentButton = (Button)findViewById(R.id.addComment);
        commentProgress =(ProgressBar) findViewById(R.id.commentProgress);
        descriptionField = (EditText) findViewById(R.id.commentDesc);
    } // end of getIDs
} // end of CommentActivity
