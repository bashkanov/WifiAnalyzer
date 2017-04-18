package com.bashkanov.wifianalyzator;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WifiViewHolder> {

    private List<Wifi> mWifis;
    private static final String TAG = "myLogs";
    private int mExpandedPosition = -1;
    private OnCardViewClickListener onCardViewClickListener;

    public class WifiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mCardView;
        private TextView ssid;
        private TextView wifiFrequency;
        private TextView wifiChannel;
        private TextView signalPower;
        private TextView mMacAdress;
        private RelativeLayout mRelativeLayout;
        private ImageView imgExpand;
        private View mBtnChart, mBtnSubscribe, mBtnNotify;
        private View root;
        public boolean isExpanded;
        public int position;

        WifiViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            mCardView = (CardView)itemView.findViewById(R.id.card_view);
            ssid = (TextView)itemView.findViewById(R.id.ssid);
            wifiFrequency = (TextView)itemView.findViewById(R.id.wifi_frequency);
            wifiChannel = (TextView)itemView.findViewById(R.id.wifi_channel);
            signalPower = (TextView)itemView.findViewById(R.id.signal_power);
            mMacAdress = (TextView)itemView.findViewById(R.id.mac);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            imgExpand = (ImageView)itemView.findViewById(R.id.icon_expand);

            mBtnChart = itemView.findViewById(R.id.btn_chart);
            mBtnSubscribe = itemView.findViewById(R.id.btn_subscribe);
            mBtnNotify = itemView.findViewById(R.id.btn_notify);
            mBtnChart.setOnClickListener(this);
            mBtnSubscribe.setOnClickListener(this);
            mBtnNotify.setOnClickListener(this);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.card_view:
                    Log.d(TAG, "Pressed cardview position - " + position);

                    if (isExpanded) mExpandedPosition = -1;
                    else mExpandedPosition = position;
                    //TODO: RV Animation
                    //TransitionManager.beginDelayedTransition(recyclerView);
                    notifyDataSetChanged();
                    break;

                case R.id.btn_chart:
                    onCardViewClickListener.OnChartClicked((Wifi)root.getTag());
                    //Log.d(TAG, "Pressed button btn_chart on cardview " + position);
                    break;

                case R.id.btn_subscribe:
                    onCardViewClickListener.OnSubscribeClicked((Wifi)root.getTag());
                    //Log.d(TAG, "Pressed button btn_subscribe on cardview " + position);
                    break;

                case R.id.btn_notify:
                    onCardViewClickListener.OnNotifyClicked((Wifi)root.getTag());
                    //Log.d(TAG, "Pressed button btn_notify on cardview " + position);
                    break;
            }
        }
    }

    public void setOnCardViewClickListener(OnCardViewClickListener onCardViewClickListener) {
        this.onCardViewClickListener = onCardViewClickListener;
    }

    public interface OnCardViewClickListener{
        void OnSubscribeClicked(Wifi wifi);
        void OnChartClicked(Wifi wifi);
        void OnNotifyClicked(Wifi wifi);
    }

    RecyclerViewAdapter(List<Wifi> wifiList){
        this.mWifis = wifiList;
    }

/*    public void swap(List<Wifi> datas){
        if (this.mWifis != null) {
            this.mWifis.clear();
            this.mWifis.addAll(datas);
        } else {
            this.mWifis = datas;
        }
        notifyDataSetChanged();
    }*/

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        WifiViewHolder pvh = new WifiViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(WifiViewHolder wifiViewHolder, final int position) {
        wifiViewHolder.root.setTag(mWifis.get(position));

        wifiViewHolder.ssid.setText(mWifis.get(position).getSsid());

        if (mWifis.get(position).getFrequency() >= 2412 || mWifis.get(position).getFrequency() <= 2484 ){
            wifiViewHolder.wifiFrequency.setText("2.4G");
            setChannel(wifiViewHolder,position);
        }else if (mWifis.get(position).getFrequency() >= 5150 || mWifis.get(position).getFrequency() <= 5805 ){
            wifiViewHolder.wifiFrequency.setText("5G");
            wifiViewHolder.wifiChannel.setText("channel " + getChannel5g(position));
        }

        wifiViewHolder.signalPower.setText(Integer.toString(mWifis.get(position).getSignalPower()) + "dB");
        wifiViewHolder.mMacAdress.setText(mWifis.get(position).getBssid());
        wifiViewHolder.position = position;
        wifiViewHolder.isExpanded = position == mExpandedPosition;
        Log.d(TAG, "ViewHolderPosition - " + position + "; IsExpanded - " + wifiViewHolder.isExpanded);


        if (wifiViewHolder.isExpanded) {
            wifiViewHolder.mRelativeLayout.setVisibility(View.VISIBLE);
            wifiViewHolder.imgExpand.setBackgroundResource(R.drawable.ic_expand_less_48px);
        } else {
            wifiViewHolder.mRelativeLayout.setVisibility(View.GONE);
            wifiViewHolder.imgExpand.setBackgroundResource(R.drawable.ic_expand_more_48px);
        }
        wifiViewHolder.itemView.setActivated(wifiViewHolder.isExpanded);
    }

    @Override
    public int getItemCount() {
        return mWifis.size();
    }

    public void setChannel(WifiViewHolder wifiViewHolder, int position){
        switch (mWifis.get(position).getFrequency()){
            case 2412:
                wifiViewHolder.wifiChannel.setText("channel " + "1");
                break;
            case 2417:
                wifiViewHolder.wifiChannel.setText("channel " + "2");
                break;
            case 2422:
                wifiViewHolder.wifiChannel.setText("channel " + "3");
                break;
            case 2427:
                wifiViewHolder.wifiChannel.setText("channel " + "4");
                break;
            case 2432:
                wifiViewHolder.wifiChannel.setText("channel " + "5");
                break;
            case 2437:
                wifiViewHolder.wifiChannel.setText("channel " + "6");
                break;
            case 2442:
                wifiViewHolder.wifiChannel.setText("channel " + "7");
                break;
            case 2447:
                wifiViewHolder.wifiChannel.setText("channel " + "8");
                break;
            case 2452:
                wifiViewHolder.wifiChannel.setText("channel " + "9");
                break;
            case 2457:
                wifiViewHolder.wifiChannel.setText("channel " + "10");
                break;
            case 2462:
                wifiViewHolder.wifiChannel.setText("channel " + "11");
                break;
            case 2467:
                wifiViewHolder.wifiChannel.setText("channel " + "12");
                break;
            case 2472:
                wifiViewHolder.wifiChannel.setText("channel " + "13");
                break;
            case 2484:
                wifiViewHolder.wifiChannel.setText("channel " + "14");
                break;
        }
    }
    public int getChannel5g(int position){
        int N = (mWifis.get(position).getFrequency()-5000)/5;
        return N;
    }
}
