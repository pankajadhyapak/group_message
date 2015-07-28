package students;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.w3effects.groupmess.groupmess.R;

import java.util.ArrayList;
import java.util.List;


public class TeacherSelectActivity extends ListActivity {
    List<ParseObject> mTeachers;
    protected MenuItem mSendMenuItem;
    ArrayList<String> TeacherNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_teacher_select);
        setProgressBarIndeterminateVisibility(true);

        // Allow user to choose multiple  in a list view
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
        query.whereEqualTo("type", "teacher");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> teachers, ParseException e) {
                setProgressBarIndeterminateVisibility(false);

                if(e == null){
                    mTeachers = teachers;
                    Log.i("teachers", mTeachers.toString());
                    String[] teacherNames  = new String[mTeachers.size()];
                    int i =0;
                    for(ParseObject message: mTeachers){
                        teacherNames[i] = message.getString("username");
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_checked,
                            teacherNames
                    );
                    setListAdapter(adapter);
                }else {
                    Toast.makeText(TeacherSelectActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.semester_select, menu);
        mSendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_send_now) {

            Intent newMessage = new Intent(TeacherSelectActivity.this, StudentNewMessage.class);
            newMessage.putExtra("TeacherIds", getTeacherIds());
            newMessage.putExtra("TeacherNames", TeacherNames);

            startActivity(newMessage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(l.getCheckedItemCount() > 0){
            mSendMenuItem.setVisible(true);
        }else{
            mSendMenuItem.setVisible(false);
        }

    }

    protected ArrayList<String> getTeacherIds(){

        ArrayList<String> TeacherIds = new ArrayList<String>();

        for(int i=0;i<getListView().getCount();i++){
            if(getListView().isItemChecked(i)){
                TeacherIds.add(mTeachers.get(i).getObjectId());
                TeacherNames.add(mTeachers.get(i).get("first_name").toString());

            }
        }

        Log.i("TeacherIds", TeacherIds.toString());

        return TeacherIds;
    }



}
