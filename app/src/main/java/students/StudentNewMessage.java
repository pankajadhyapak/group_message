package students;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.w3effects.groupmess.groupmess.R;

import java.util.ArrayList;

import teachers.SemesterSelectActivity;
import ui.MyActivity;


public class StudentNewMessage extends Activity {

    private static final int PICKFILE_RESULT_CODE = 1;
    TextView recipients;
    Button send;
    EditText mMessage;
    EditText mSubject;
    protected MenuItem mSendMenuItem;

    StringBuilder sb = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_new_message);

        setSenderIds();

        //send = (Button)findViewById(R.id.MessageSend);
        mSubject = (EditText)findViewById(R.id.SubjectField);

//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendMessgae();
//
//            }
//
//
//
//
//        });



    }
    private void sendMessgae() {
        // Prepare Message
        prepareMessage();
        //Send Message
        //Back to Main Activity
        Intent home = new Intent(StudentNewMessage.this, MyActivity.class);
        startActivity(home);
    }

    private void prepareMessage() {
        mMessage = (EditText)findViewById(R.id.MessgaeBox);
        ParseObject message = new ParseObject("MessageTeachers");
        message.put("sender_id", ParseUser.getCurrentUser().getObjectId());
        message.put("sender_name", ParseUser.getCurrentUser().getUsername());
        Intent n = getIntent();
        message.put("teachers_ids",n.getStringArrayListExtra("TeacherIds"));
        message.put("subject",mSubject.getText().toString() );
        message.put("message",mMessage.getText().toString() );

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    // Create our Notification query
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    Intent n = getIntent();
                    pushQuery.whereContainedIn("userId",n.getIntegerArrayListExtra("TeacherIds"));

                    // Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery); // Set our Installation query
                    String NotificationMessage = ParseUser.getCurrentUser().getUsername();
                    push.setMessage("New Message from "+NotificationMessage);
                    push.sendInBackground();
                    Toast.makeText(StudentNewMessage.this, "Message Sent Successfully", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(StudentNewMessage.this, e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void setSenderIds() {
        recipients = (TextView)findViewById(R.id.recipientId);
        Intent n = getIntent();

        ArrayList<String> semids = n.getStringArrayListExtra("TeacherNames");
        for (String s : semids)
        {
            sb.append(s);
            sb.append("  ");
        }

        recipients.setText(sb.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_new_message, menu);
        mSendMenuItem = menu.getItem(1);
        mMessage = (EditText)findViewById(R.id.MessgaeBox);
        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable message) {
                if(message.length() > 0){
                    mSendMenuItem.setVisible(true);
                }else {
                    mSendMenuItem.setVisible(false);
                }
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_attach:
                Intent chooseAttachment = new Intent(Intent.ACTION_GET_CONTENT);
                chooseAttachment.setType("file/*");
                startActivityForResult(chooseAttachment, PICKFILE_RESULT_CODE);
                break;
            case R.id.action_Msend:
                sendMessgae();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    TextView f = (TextView)findViewById(R.id.attachedFile);
                    f.setText(FilePath);
                }
                break;

        }
    }

}
