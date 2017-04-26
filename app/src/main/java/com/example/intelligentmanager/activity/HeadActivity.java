package com.example.intelligentmanager.activity;

/**
 * Created by 易水柔 on 2017/3/12.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.intelligentmanager.R;

public class HeadActivity extends Activity{

    public int[] imageId = new int[] { R.drawable.girl1, R.drawable.girl2,
            R.drawable.girl3, R.drawable.girl4, R.drawable.boy1,
            R.drawable.boy3, R.drawable.boy4, R.drawable.boy5,
            R.drawable.boy }; //定义并初始化保存头像的数组
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.head);//设置Activity所使用的布局
        GridView gridView = (GridView) findViewById(R.id.gridView1);
        BaseAdapter adapter = new BaseAdapter() {

            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                if(convertView==null){
                    imageView = new ImageView(HeadActivity.this);//实例化ImageView对象
                    /********设置图片的宽和搞*********/
                    imageView.setAdjustViewBounds(true);
                    imageView.setMaxHeight(150);
                    imageView.setMaxWidth(158);

                    imageView.setPadding(5, 5, 5, 5);//设置ImageView的内边距
                }else{
                    imageView = (ImageView) convertView;
                }
                imageView.setImageResource(imageId[position]);//为ImageView设置要显示的图片
                return imageView;
            }

            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            public int getCount() {
                // TODO Auto-generated method stub
                return imageId.length;
            }
        };
        gridView.setAdapter(adapter);//将适配器与GridView相关联
        gridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent  = getIntent();//获取intent对象
                Bundle bundle = new Bundle();//实例化要传输的数据包
                bundle.putInt("imageId", imageId[position]);//显示选中后的图片
                intent.putExtras(bundle);//将数据包保存到intent中
                setResult(0x11,intent);
                finish();
            }
        });

    }
}