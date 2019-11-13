package com.hfad.myplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchAdapter extends BaseAdapter{
    private ArrayList<Match> listViewItems = new ArrayList<Match>();

    // ListViewAdapter의 생성자
    public MatchAdapter(){

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
            convertView = inflater.inflate(R.layout.matchlist, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView cnameTextView = (TextView) convertView.findViewById(R.id.m_name) ;
        TextView categoryTextView = (TextView) convertView.findViewById(R.id.m_date) ;
        TextView csdateTextView = (TextView) convertView.findViewById(R.id.t1) ;
        TextView jsdateTextView = (TextView) convertView.findViewById(R.id.t2);
        TextView r1 = (TextView)convertView.findViewById(R.id.t1_r);
        TextView r2 = (TextView)convertView.findViewById(R.id.t2_r);
        TextView win = (TextView)convertView.findViewById(R.id.win);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Match listViewItem = listViewItems.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        cnameTextView.setText(listViewItem.getNmae());
        categoryTextView.setText(listViewItem.getDate());
        csdateTextView.setText(listViewItem.getT1()+"  ");
        jsdateTextView.setText("  "+listViewItem.getT2());
        r1.setText(listViewItem.getR1());
        r2.setText(listViewItem.getR2());
        win.setText(listViewItem.getWin());


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
    public void addItem(String title, String desc, String csdate ,String jsdate,int g_num,String r1, String r2, String win,int to ) {
        Match item = new Match();

        item.setNmae(title);
        item.setDate(desc);
        item.setT1(csdate);
        item.setT2(jsdate);
        item.setG_num(g_num);
        item.setR1(r1);
        item.setR2(r2);
        item.setWin(win);
        item.setTo(to);

        listViewItems.add(item);
    }
    // 데이터를 가져오기 위한 함수
    public String getName(int position){
        Match listViewItem = listViewItems.get(position);
        return listViewItem.getNmae();
    }

    public String getCategory(int position){
        Match listViewItem = listViewItems.get(position);
        return listViewItem.getDate();
    }

    public String getT1(int position){
        Match listViewItem = listViewItems.get(position);
        return listViewItem.getT1();
    }
    public String getT2(int position){
        Match  listViewItem = listViewItems.get(position);
        return listViewItem.getT2();
    }


    public int getG_NUM(int position){
        Match  listViewItem = listViewItems.get(position);
        return listViewItem.getG_num();
    }
    public String getWin(int position){
        Match  listViewItem = listViewItems.get(position);
        return listViewItem.getWin();
    }

    public int getTo(int position){
        Match  listViewItem = listViewItems.get(position);
        return listViewItem.getTo();
    }
}