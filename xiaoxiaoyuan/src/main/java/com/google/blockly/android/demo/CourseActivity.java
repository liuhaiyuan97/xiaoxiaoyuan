package com.google.blockly.android.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.blockly.android.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseActivity extends AppCompatActivity {
    private ImageView ivReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        ivReturn=findViewById(R.id.iv_return);
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //初始化listview
        initListView();
    }

    private void initListView() {
        List<Map<String, Object>> listData = getDataList();
        CustomAdapter adapter = new CustomAdapter(this,
                R.layout.layout_course_lesson, listData);
        ListView listView = findViewById(R.id.lv_lesson);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent();
                intent.setClass(CourseActivity.this,VideoActivity.class);
                if (i == 0){
                    intent.putExtra("url","http://xiaoxiaoyuan.oss-cn-beijing.aliyuncs.com/Video/%E5%B0%8F%E5%B0%8F%E7%8C%BF%EF%BC%881%EF%BC%89.mp4");
                }else if (i==1){
                    intent.putExtra("url","http://xiaoxiaoyuan.oss-cn-beijing.aliyuncs.com/Video/%E5%B0%8F%E5%B0%8F%E7%8C%BF%EF%BC%882%EF%BC%89.mp4");
                }else if (i==2){
                    intent.putExtra("url","http://xiaoxiaoyuan.oss-cn-beijing.aliyuncs.com/Video/%E5%B0%8F%E5%B0%8F%E7%8C%BF%EF%BC%883%EF%BC%89.mp4");
                }else if (i==3){
                    intent.putExtra("url","http://xiaoxiaoyuan.oss-cn-beijing.aliyuncs.com/Video/%E5%B0%8F%E5%B0%8F%E7%8C%BF%EF%BC%884%EF%BC%89.mp4");
                }else {
                    intent.putExtra("url","http");
                }
                startActivityForResult(intent,1);
            }
        });
    }


    private class CustomAdapter extends BaseAdapter {
        private Context context;
        private int itemLayoutID;
        private List<Map<String, Object>> data;

        public CustomAdapter(Context context,
                             int itemLayoutID,
                             List<Map<String, Object>> data) {
            this.context = context;
            this.itemLayoutID = itemLayoutID;
            this.data = data;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(itemLayoutID, null);
            }
            ImageView iv_lesson = convertView.findViewById(R.id.iv_lesson);
            TextView tv_title = convertView.findViewById(R.id.tv_title_lesson);
            TextView tv_athor = convertView.findViewById(R.id.tv_athor_lesson);
            TextView tv_time = convertView.findViewById(R.id.tv_time_lesson);

            Map<String, Object> map = data.get(position);
            iv_lesson.setImageResource((int)map.get("image"));
            tv_title.setText((String)map.get("title"));
            tv_athor.setText((String)map.get("athor"));
            tv_time.setText((String)map.get("time"));

            return convertView;
        }
    }
    private List<Map<String,Object>> getDataList() {
        Map<String,Object> map1 = new HashMap<>();
        map1.put("image",R.mipmap.lesson1);
        map1.put("title","初识小小猿");
        map1.put("athor","先玩个游戏才是正经事");
        map1.put("time","2019年1月");

        Map<String,Object> map2 = new HashMap<>();
        map2.put("image",R.mipmap.lesson2);
        map2.put("title","小小猿基础");
        map2.put("athor","初识逻辑，掌握循环");
        map2.put("time","2019年2月");

        Map<String,Object> map3 = new HashMap<>();
        map3.put("image",R.mipmap.lesson3);
        map3.put("title","小小猿进阶");
        map3.put("athor","数学加文本，变量应用更方便");
        map3.put("time","2019年3月");

        Map<String,Object> map4 = new HashMap<>();
        map4.put("image",R.mipmap.lesson4);
        map4.put("title","小小猿高阶");
        map4.put("athor","数组带你体会程序之道");
        map4.put("time","2019年4月");

        Map<String,Object> map5 = new HashMap<>();
        map5.put("image",R.mipmap.lesson5);
        map5.put("title","小小猿实战");
        map5.put("athor","升级刷boss，冲击天梯榜");
        map5.put("time","未完待续");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);

        return list;
    }
}

