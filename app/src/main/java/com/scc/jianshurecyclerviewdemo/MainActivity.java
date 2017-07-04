package com.scc.jianshurecyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scc.jianshurecyclerviewdemo.bean.DatasBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Map<Integer, List<DatasBean>> datas = new HashMap<>();//模拟服务器返回数据
    private List<DatasBean> list = new ArrayList<>();//adapter数据源
    private Map<Integer, String> keys = new HashMap<>();//存放所有key的位置和内容
    private List<String> mKeys = new ArrayList<>(); //所有key的集合
    private int lastVisibleItem;                    //recyclerView最后一个item的位置
    private LinearLayoutManager layoutManager;      //recyclerView布局管理器
    private int nextPage = 1;                       //当前页码
    private int jj = 0;                             //临时计数
    private int flagI = 0;                          //刷新标记
    private RvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        requestData(1);      //请求数据(摸你一下)

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mAdapter.getStatus(2);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    nextPage++;
                    requestData(nextPage);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 摸你请求数据
     */
    private void requestData(int nextPage) {
        final TreeMap<String, Object> map = new TreeMap<>(new descendComparator());
        String msg = "\n" +
                "{\n" +
                "  \"code\": 0,\n" +
                "  \"msg\": \"success\",\n" +
                "  \"data\": {\n" +
                "    \"totalCount\": \"15\",\n" +
                "    \"list\": {\n" +
                "      \"王者荣耀\": [\n" +
                "        {\n" +
                "          \"name\": \"李元芳\",\n" +
                "          \"sex\": \"男\",\n" +
                "          \"price\": \"13888\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"狄仁杰\",\n" +
                "          \"sex\": \"男\",\n" +
                "          \"price\": \"8888\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"刘备\",\n" +
                "          \"sex\": \"男\",\n" +
                "          \"price\": \"13888\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"英雄联盟\": [\n" +
                "        {\n" +
                "          \"name\": \"格雷福斯\",\n" +
                "          \"sex\": \"男\",\n" +
                "          \"price\": \"6300\"\n" +
                "        },\n" +
                "\t{\n" +
                "          \"name\": \"卡牌大师\",\n" +
                "          \"sex\": \"男\",\n" +
                "          \"price\": \"4800\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"王者农药\": [\n" +
                "        {\n" +
                "          \"name\": \"关羽\",\n" +
                "          \"sex\": \"男\",\n" +
                "          \"price\": \"18888\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"不知火舞\",\n" +
                "          \"sex\": \"女\",\n" +
                "          \"price\": \"不详\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            JSONObject object = new JSONObject(msg);
            if (object.getInt("code") == 0) {
                if (!object.getJSONObject("data").getString("totalCount").equals("0")) {
                    Iterator in = object.getJSONObject("data").getJSONObject("list").keys();
                    while (in.hasNext()) {
                        String key = in.next() + "";
                        map.put(key, object.getJSONObject("data").getJSONObject("list").get(key));
                    }
                    Set<Map.Entry<String, Object>> entryseSet = map.entrySet();
                    for (Map.Entry<String, Object> entry : entryseSet) {
                        System.out.println(entry.getKey() + "," + entry.getValue());
                        mKeys.add(entry.getKey()); //添加每个key
                        JSONArray arr = object.getJSONObject("data").getJSONObject("list").getJSONArray(entry.getKey());
                        List<DatasBean> sss = new ArrayList<DatasBean>();
                        for (int i = 0; i < arr.length(); i++) {
                            //得到每个value中的bean类并添加
                            Gson g = new Gson();
                            DatasBean datasBean = g.fromJson(arr.getJSONObject(i).toString(), DatasBean.class);
                            sss.add(datasBean);
                        }
                        datas.put(jj, sss);
                        jj++;
                    }
                    if (nextPage == 1) {
                        for (int i = 0; i < datas.size(); i++) {
                            keys.put(list.size(), mKeys.get(i));
                            for (int j = 0; j < datas.get(i).size(); j++) {
                                list.add(datas.get(i).get(j));
                            }
                        }
                        flagI = datas.size();
                    } else {
                        for (int i = flagI; i < datas.size(); i++) {
                            keys.put(list.size(), mKeys.get(i));
                            for (int j = 0; j < datas.get(i).size(); j++) {
                                list.add(datas.get(i).get(j));
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "没有更多了...", Toast.LENGTH_SHORT).show();
            }
            mAdapter = new RvAdapter(list, this);
            layoutManager = new LinearLayoutManager(this);
            FloatingItemDecoration floatingItemDecoration = new FloatingItemDecoration(this, getResources().getColor(R.color.colorAccent), 1, 0);
            floatingItemDecoration.setKeys(keys);
            floatingItemDecoration.setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
            mRecyclerView.addItemDecoration(floatingItemDecoration);
            //设置布局管理器
            keys.clear();
            layoutManager.setOrientation(1);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);
            if (nextPage != 1) {
                MoveToPosition(layoutManager,mRecyclerView,mAdapter.getItemCount()-10);
                mAdapter.getStatus(1);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    class descendComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            String i1 = (String) o1;
            String i2 = (String) o2;
            return -i1.compareTo(i2);
        }
    }
    public  void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }
}
