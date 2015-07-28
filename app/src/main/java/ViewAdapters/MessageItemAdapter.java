package ViewAdapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.w3effects.groupmess.groupmess.R;

import java.util.Date;
import java.util.List;

import ui.SentFragment;

/**
 * Created by pankaj on 08/10/14.
 */
public class MessageItemAdapter extends ArrayAdapter{
    private int mLastPosition;

    protected  Context mContext;
    protected List<ParseObject> mMessages;

    public MessageItemAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.messageitem,messages);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){

        convertView = LayoutInflater.from(mContext).inflate(R.layout.messageitem, null);

        holder = new ViewHolder();


            holder.mBigView = (TextView)convertView.findViewById(R.id.bigSenderName);
            holder.mMessageLabel = (TextView)convertView.findViewById(R.id.messageLabel);
            holder.mDateStamp = (TextView)convertView.findViewById(R.id.timeStampLabel);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ParseObject message = mMessages.get(position);

        // Set the message time
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(message.getCreatedAt().getTime(),now,DateUtils.SECOND_IN_MILLIS).toString();
        holder.mDateStamp.setText(convertedDate);

        //Set Senders first letter
        if(mContext.getClass().equals(SentFragment.class)){
            holder.mBigView.setBackgroundResource(R.color.sent_big_label_color);
        }else{
            holder.mBigView.setBackgroundResource(R.color.inbox_big_label_color);

        }
        String firstLetter = message.getString("sender_name").substring(0,1).toUpperCase();
        holder.mBigView.setText(firstLetter);

        //Set Senders label
        holder.mMessageLabel.setText(Makeexcerpt(message.getString("sender_name") +" - " + message.getString("subject")));

        /**
         *  Animate List View like a card Stack
         *
         *  By Pankaj Adhyapak
         */
        float initialTranslation = (mLastPosition <= position ? 500f : -500f);

        convertView.setTranslationY(initialTranslation);
        convertView.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        mLastPosition = position;

        //return View
        return convertView;
    }

    private String Makeexcerpt(String excerpt) {
        if(excerpt.length() > 23){
            return excerpt.substring(0,23)+ "....";
        }
        return excerpt ;
    }

    private static class ViewHolder{
        TextView mBigView;
        TextView mMessageLabel;
        TextView mDateStamp;
    }
}

