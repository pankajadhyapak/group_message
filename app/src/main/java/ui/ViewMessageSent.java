package ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.w3effects.groupmess.groupmess.R;


public class ViewMessageSent extends Activity {

    TextView sender;
    TextView BigSenderAlpha;

    TextView subject;
    TextView Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_view_message);

        Intent view = getIntent();
        String oId = view.getStringExtra("objectId");
        Log.e("Eeeede", oId);
        BigSenderAlpha = (TextView)findViewById(R.id.bigSenderName);
        sender = (TextView)findViewById(R.id.byName);
        Message = (TextView)findViewById(R.id.DisplayMessage);
        subject = (TextView)findViewById(R.id.messageLabel);
        setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("MessageTeachers");
        query.getInBackground(oId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject message, ParseException e) {
                setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    BigSenderAlpha.setText(message.getString("sender_name").substring(0,1).toUpperCase());
                    subject.setText(message.getString("subject"));
                    sender.setText("From " +message.getString("sender_name"));
                    Message.setText(message.getString("message"));
                    message.put("read", true);
                    message.saveInBackground();
                } else {
                    Toast.makeText(ViewMessageSent.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
