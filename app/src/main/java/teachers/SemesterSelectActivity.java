package teachers;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.w3effects.groupmess.groupmess.R;

import java.util.ArrayList;


public class SemesterSelectActivity extends ListActivity {

    protected MenuItem mSendMenuItem;
    ArrayList<String> semesterNames = new ArrayList<String>();
    String[] semesters = new String[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_select);

        // Allow Teacher to choose multiple semester in a list view
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        semesters[0] = "First Semester";
        semesters[1] = "Second Semester";
        semesters[2] = "Third Semester";
        semesters[3] = "Fourth Semester";
        semesters[4] = "Fifth Semester";
        semesters[5] = "Sixth Semester";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getListView().getContext(),
                android.R.layout.simple_list_item_checked,
                semesters);
        setListAdapter(adapter);

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

            Intent newMessage = new Intent(SemesterSelectActivity.this, NewMessageActivity.class);
            newMessage.putExtra("semesterIds", getSemesterIds());
            newMessage.putExtra("semesterNames", semesterNames);
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

    protected ArrayList<Integer> getSemesterIds(){
        ArrayList<Integer> semesterIds = new ArrayList<Integer>();

        for(int i=0;i<getListView().getCount();i++){
            if(getListView().isItemChecked(i)){
                semesterIds.add(i);
                semesterNames.add(semesters[i]);
            }
        }

        Log.i("SemesterIds", semesterIds.toString());

        return semesterIds;
    }


}














