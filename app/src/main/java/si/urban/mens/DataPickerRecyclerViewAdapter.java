package si.urban.mens;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DataPickerRecyclerViewAdapter extends RecyclerView.Adapter<DataPickerRecyclerViewAdapter.ViewHolder> {

    private List<String> ids;
    private List<LocalDateTime> dateTimes;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ArrayList<Boolean> selected;

    // data is passed into the constructor
    DataPickerRecyclerViewAdapter(Context context, List<String> ids,List<LocalDateTime> dateTimes) {
        this.mInflater = LayoutInflater.from(context);
        this.ids = ids;
        this.dateTimes = dateTimes;
        this.selected = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            selected.add(false);
        }
    }

    public ArrayList<Integer> removeSelected(){
        Iterator<String> idIterator = ids.iterator();
        Iterator<LocalDateTime> dateIterator = dateTimes.iterator();
        Iterator<Boolean> selectedIterator = selected.iterator();
        ArrayList<Integer> deleted = new ArrayList<>();
        int i = 0;
        while (selectedIterator.hasNext()){
            dateIterator.next();
            idIterator.next();
            if(selectedIterator.next()){
                deleted.add(i);
                selectedIterator.remove();
                idIterator.remove();
                dateIterator.remove();
            }
            i++;
        }
        return deleted;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_data_picker, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = ids.get(position);
        String dateTime = dateTimes.get(position).toString();
        holder.measuremetnIdtv.setText(id);
        holder.datetv.setText(dateTime);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return ids.size();
    }

    void updateSelected(int pos, boolean state){
        selected.set(pos,state);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView measuremetnIdtv;
        TextView datetv;
        CheckBox selectcb;

        ViewHolder(View itemView) {
            super(itemView);
            measuremetnIdtv = itemView.findViewById(R.id.measuremetnIdtv);
            selectcb = itemView.findViewById(R.id.selectcb);
            datetv = itemView.findViewById(R.id.datetv);
            selectcb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(v);
                }
            });
            itemView.setOnClickListener(this);
        }

        public void setSelected(View view){
            updateSelected(this.getAdapterPosition(),selectcb.isChecked());
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return ids.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
