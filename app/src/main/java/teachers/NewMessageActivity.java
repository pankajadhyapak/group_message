package teachers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import ui.MyActivity;
import com.w3effects.groupmess.groupmess.R;

import java.util.ArrayList;
import java.util.List;


public class NewMessageActivity extends Activity {

    TextView recipients;
    Button send;
    EditText mMessage;
    EditText mSubject;
    StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        setSenderIds();

        send = (Button)findViewById(R.id.MessageSend);
        mSubject = (EditText)findViewById(R.id.SubjectField);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prepare & Send Message
                prepareMessage();

                //Back to Main Activity
                Intent home = new Intent(NewMessageActivity.this, MyActivity.class);
                startActivity(home);
            }

            private void prepareMessage() {
                mMessage = (EditText)findViewById(R.id.MessgaeBox);
                ParseObject message = new ParseObject("Message");
                message.put("sender_id", ParseUser.getCurrentUser().getObjectId());
                message.put("sender_name", ParseUser.getCurrentUser().getUsername());
                Intent n = getIntent();
                message.put("semester_ids",n.getIntegerArrayListExtra("semesterIds"));
                message.put("subject",mSubject.getText().toString() );
                message.put("message",mMessage.getText().toString() );
                message.put("read",false );

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                            // Create our Notification query
                            ParseQuery pushQuery = ParseInstallation.getQuery();
                            Intent n = getIntent();
                            pushQuery.whereContainedIn("semester",n.getIntegerArrayListExtra("semesterIds"));

                            // Send push notification to query
                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery); // Set our Installation query
                            String NotificationMessage = ParseUser.getCurrentUser().getUsername();
                            push.setMessage("New Message from "+NotificationMessage);
                            push.sendInBackground();

                            Toast.makeText(NewMessageActivity.this, "Message Sent Successfully",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(NewMessageActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });



    }

    private void setSenderIds() {
        recipients = (TextView)findViewById(R.id.recipientId);
        Intent n = getIntent();

        ArrayList<String> semids = n.getStringArrayListExtra("semesterNames");
        for (String s : semids)
        {
            sb.append(s);
            sb.append("  ");
        }

        Log.i("nsdjsn", sb.toString());
        recipients.setText(sb.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
