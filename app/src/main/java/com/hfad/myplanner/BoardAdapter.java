package com.hfad.myplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

        public class BoardAdapter extends BaseAdapter {
            private ArrayList<ListViewBoard> listViewItems = new ArrayList<ListViewBoard>();

            // ListViewAdapter의 생성자
            public BoardAdapter() {

            }

            @Override
            public int getCount() {
                return listViewItems.size();
            }

            // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final int pos = position;
                final Context context = parent.getContext();

                // "listview_item" Layout을 inflate하여 convertView 참조 획득.
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_board, parent, false);
                }

                // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
                TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
                TextView writer = (TextView)convertView.findViewById(R.id.writer);
                TextView time = (TextView)convertView.findViewById(R.id.writetime);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewBoard listViewItem = listViewItems.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getB_TITLE());
        writer.setText(listViewItem.getID());
        time.setText(listViewItem.getB_DATE());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int b_num, String title, String text,String ID, String date) {
        ListViewBoard item = new ListViewBoard();

        item.setB_NUM(b_num);
        item.setB_TITLE(title);
        item.setB_TEXT(text);
        item.setID(ID);
        item.setB_DATE(date);

        listViewItems.add(item);
    }

    public int getB_NUM(int position){
        ListViewBoard listViewboard = listViewItems.get(position);
        return listViewboard.getB_NUM();
    }
    public String getB_TITLE(int position){
        ListViewBoard listViewboard = listViewItems.get(position);
        return listViewboard.getB_TITLE();
    }
    public String getB_TEXT(int position){
        ListViewBoard listViewboard = listViewItems.get(position);
        return listViewboard.getB_TEXT();
    }

    public String getID(int position){
                ListViewBoard listViewboard = listViewItems.get(position);
                return listViewboard.getID();
            }
            public String getB_DATE(int position){
                ListViewBoard listViewboard = listViewItems.get(position);
                return listViewboard.getB_DATE();
            }
}
