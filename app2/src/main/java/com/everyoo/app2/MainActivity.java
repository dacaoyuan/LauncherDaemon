package com.everyoo.app2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private List<ResolveInfo> mApps;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gv_view);

        packageManager = this.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //来获取一个关于App信息（ResolveInfo）的集合，这个ResolveInfo对象中就包含了应用程序的程序名、包名、入口类名等信息。
        mApps = packageManager.queryIntentActivities(mainIntent, 0);

        gridView.setAdapter(new gridAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo info = mApps.get(position);
                String pkg = info.activityInfo.packageName;
                String cls = info.activityInfo.name;

                ComponentName componentName = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });

    }


    class gridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mApps.size();
        }

        @Override
        public Object getItem(int position) {
            return mApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_apps, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.item_ic);
                holder.textView = (TextView) convertView.findViewById(R.id.item_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ResolveInfo info = mApps.get(position);
            holder.imageView.setImageDrawable(info.activityInfo.loadIcon(packageManager));
            String appLabel = (String) info.loadLabel((packageManager));
            holder.textView.setText(appLabel);
            return convertView;
        }
    }

    class ViewHolder {

        public ImageView imageView;
        public TextView textView;

    }


}
