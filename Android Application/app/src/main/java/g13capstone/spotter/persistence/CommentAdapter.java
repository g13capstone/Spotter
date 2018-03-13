package g13capstone.spotter.persistence;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.objects.Comment;

class CommentAdapter extends ArrayAdapter<Comment>{

    private List<Comment> commentArray;
    private Activity context;

    CommentAdapter(Activity context, List<Comment> commentArray){
        super(context, R.layout.comment_model, commentArray);

        this.commentArray = commentArray;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder;
        if (r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.comment_model,null, true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }

        else {
            viewHolder= (ViewHolder) r.getTag();
        }

        Comment comment = commentArray.get(position);

        viewHolder.lotComment.setText(comment.getComment());
        viewHolder.userName.setText(comment.getUserName());
        viewHolder.logTime.setText(comment.getLogTime());
        return r;
    }

    class ViewHolder{
        TextView lotComment;
        TextView userName;
        TextView logTime;

        ViewHolder(View v){
            lotComment = (TextView)v.findViewById(R.id.comment);
            userName = (TextView)v.findViewById(R.id.userName);
            logTime = (TextView)v.findViewById(R.id.timeLog);
        }
    }
}
