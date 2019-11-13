package com.hfad.myplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    private ArrayList<ListViewItem> listViewItems = new ArrayList<ListViewItem>();

    // ListViewAdapter의 생성자
    public ListViewAdapter(){

    }
    @Override
    public int getCount() {
        return listViewItems.size() ;
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
        TextView teamnumberView = (TextView) convertView.findViewById(R.id.teamnumber);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItems.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        cnameTextView.setText(listViewItem.getC_NAME());
        categoryTextView.setText(listViewItem.getCATEGORY());
        csdateTextView.setText(listViewItem.getCSDATE());
        jsdateTextView.setText(listViewItem.getJSDATE());
        jedateTextView.setText(listViewItem.getJEDATE());
        teamnumberView.setText(listViewItem.getS_MEMBER());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }
    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItems.get(position) ;
    }
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String desc, String csdate ,String jsdate ,String jedate ,String c_text, String c_num, String s_member,int member, String form,String location,int u) {
        ListViewItem item = new ListViewItem();

        item.setC_NAME(title);
        item.setCATEGORY(desc);
        item.setCSDATE(csdate);
        item.setJSDATE(jsdate);
        item.setJEDATE(jedate);
        item.setC_TEXT(c_text);
        item.setC_NUM(c_num);
        item.setS_MEMBER(s_member);
        item.setMEMBER(member);
        item.setFORM(form);
        item.setLOCATION(location);
        item.setU_NUM(u);

        listViewItems.add(item);
    }
    // 데이터를 가져오기 위한 함수
    public String getName(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getC_NAME();
    }

    public String getCategory(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getCATEGORY();
    }

    public String getCSDATE(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getCSDATE();
    }
    public String getJSDATE(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getJSDATE();
    }
    public String getJEDATE(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getJEDATE();
    }
    public String getC_TEXT(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getC_TEXT();
    }
    public String getC_NUM(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getC_NUM();
    }
    public int getMEMBER(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getMEMBER();
    }
    public String getS_MEMBER(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getS_MEMBER();
    }
    public String getFORM(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getFORM();
    }
    public int getU_NUM(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getU_NUM();
    }
    public String getLocation(int position){
        ListViewItem listViewItem = listViewItems.get(position);
        return listViewItem.getLOCATION();
    }

}
