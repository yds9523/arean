package com.hfad.myplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchViewAdapter extends BaseAdapter {
    private ArrayList<SearchViewItem> searchViewItems = new ArrayList<SearchViewItem>();

    // ListViewAdapter의 생성자
    public SearchViewAdapter(){

    }
    @Override
    public int getCount() {
        return searchViewItems.size() ;
    }
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_search, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView cnameTextView = (TextView) convertView.findViewById(R.id.c_name) ;
        TextView categoryTextView = (TextView) convertView.findViewById(R.id.category) ;
        TextView csdateTextView = (TextView) convertView.findViewById(R.id.csdate) ;
        TextView jsdateTextView = (TextView) convertView.findViewById(R.id.jsdate);
        TextView jedateTextView = (TextView) convertView.findViewById(R.id.jedate);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchViewItem searchViewItem = searchViewItems.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        cnameTextView.setText(searchViewItem.getC_NAME());
        categoryTextView.setText(searchViewItem.getCATEGORY());
        csdateTextView.setText(searchViewItem.getCSDATE());
        jsdateTextView.setText(searchViewItem.getJSDATE());
        jedateTextView.setText(searchViewItem.getJEDATE());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }
    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) { return searchViewItems.get(position) ; }
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String desc, String csdate, String jsdate, String jedate) {
        SearchViewItem item = new SearchViewItem();

        item.setC_NAME(title);
        item.setCATEGORY(desc);
        item.setCSDATE(csdate);
        item.setJSDATE(jsdate);
        item.setJEDATE(jedate);

        searchViewItems.add(item);
    }

}
