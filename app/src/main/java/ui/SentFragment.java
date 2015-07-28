package ui;


import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.w3effects.groupmess.groupmess.R;

import java.util.List;

import ViewAdapters.MessageItemAdapter;

/**
 * Created by pankaj on 04/10/14.
 */
public class SentFragment extends ListFragment {
    ParseUser currentUser = ParseUser.getCurrentUser();
    List<ParseObject> mMessages;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sent, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(currentUser != null) {
                    if (currentUser.get("type").toString().equals("student")) {
                        getSentItemsForStudents();
                    }else {
                        getSentItemsForTeachers();
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(currentUser != null) {

            if (currentUser.get("type").toString().equals("student")) {
                getSentItemsForStudents();
            }else {
                getSentItemsForTeachers();
            }
        }
    }

    private void getSentItemsForStudents() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentSemester = prefs.getString("currentSemester", "1");
        Log.i("currentSemester", currentSemester);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("MessageTeachers");
        query.whereEqualTo("sender_id", currentUser.getObjectId());
        query.addDescendingOrder("createdAt");


        getActivity().setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                Log.e("dnsdsnd", messages.toString());
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if(e == null){
                    getActivity().setProgressBarIndeterminateVisibility(false);

                    //messages found
                    Log.e("dnsdsnd", "message found");
                    mMessages = messages;

                    String[] usernames  = new String[mMessages.size()];
                    int i =0;
                    for(ParseObject message: mMessages){
                        String subject = message.getString("subject");
                        usernames[i] = message.getString("sender_name") +" - " + subject;
                        i++;
                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                            getListView().getContext(),
//                            android.R.layout.simple_list_item_1,
//                            usernames
//                    );
                    MessageItemAdapter adapter = new MessageItemAdapter(getListView().getContext(),mMessages);

                    setListAdapter(adapter);
                }else{
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void getSentItemsForTeachers() {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Message");
        query.whereEqualTo("sender_id", currentUser.getObjectId());
        query.addDescendingOrder("createdAt");

        getActivity().setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                Log.e("dnsdsnd", messages.toString());
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if(e == null){
                    getActivity().setProgressBarIndeterminateVisibility(false);

                    //messages found
                    Log.e("dnsdsnd", "message found");
                    mMessages = messages;

                    String[] usernames  = new String[mMessages.size()];
                    int i =0;
                    for(ParseObject message: mMessages){
                        String subject = message.getString("subject");
                        usernames[i] = message.getString("sender_name") +" - " + subject;
                        i++;
                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                            getListView().getContext(),
//                            android.R.layout.simple_list_item_1,
//                            usernames
//                    );
                    MessageItemAdapter adapter = new MessageItemAdapter(getListView().getContext(),mMessages);

                    setListAdapter(adapter);
                }else{
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        Log.i("clicked item ", message.getObjectId().toString());

        if(currentUser.get("type").equals("teacher")){
            Intent ViewMessages = new Intent(getActivity(), ViewMessageInbox.class);
            ViewMessages.putExtra("objectId",message.getObjectId().toString() );
            startActivity(ViewMessages);
        }else {

            Intent ViewMessages = new Intent(getActivity(), ViewMessageSent.class);
            ViewMessages.putExtra("objectId", message.getObjectId().toString());
            startActivity(ViewMessages);
        }
    }
//use it later
    private String Makeexcerpt(String excerpt) {
        if(excerpt.length() > 20){
            return excerpt.substring(0,20)+ "....";
        }
        return excerpt ;
    }
}
