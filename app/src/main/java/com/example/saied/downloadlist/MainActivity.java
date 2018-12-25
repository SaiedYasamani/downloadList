package com.example.saied.downloadlist;

import android.os.Environment;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.huxq17.download.DownloadInfo;
import com.huxq17.download.Pump;
import com.huxq17.download.message.DownloadObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;
    RecyclerView recyclerView;
    Adapter adapter;

    List<updatable> updatables = new ArrayList<>();
    List<updatable> CurrentUpdatables;
    int updating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                File file = new File(Environment.getDownloadCacheDirectory(),updatables.get(updating).appId);
                Pump.subscribe(downloadObserverBulk);
                Pump.newRequest(updatables.get(updating).url,file.getAbsolutePath()).submit();
                updatables.get(updating).setStatus(DownloadInfo.Status.RUNNING);

                for (int i = 1; i < updatables.size(); i++) {
                    updatables.get(i).setStatus(DownloadInfo.Status.WAIT);
                }
            }
        });

        updatable updatable1 = new updatable("app1","http://xiazai.3733.com/pojie/game/podsctjpjb.apk",null,null);
        updatable updatable2 = new updatable("app2","https://file.izuiyou.com/download/package/zuiyou.apk?from=ixiaochuan",null,null);
        updatable updatable3 = new updatable("app2","http://v.nq6.com/xinqu.apk",null,null);
        updatable updatable4 = new updatable("app2","http://wap.apk.anzhi.com/data4/apk/201810/24/e2cd3e0aded695c8fb7edcc508e3fd1b_37132000.apk",null,null);

        updatables.add(updatable1);
        updatables.add(updatable2);
        updatables.add(updatable3);
        updatables.add(updatable4);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CurrentUpdatables = new ArrayList<>();
        CurrentUpdatables = updatables;
        adapter = new Adapter(CurrentUpdatables,this);
        recyclerView.setAdapter(adapter);
    }

    DownloadObserver downloadObserver = new DownloadObserver() {
        @Override
        public void onProgress(int progress) {

            for (updatable u: CurrentUpdatables)
            {
                if(u.getUrl() == getDownloadInfo().getUrl())
                {
                    u.setDownloadInfo(getDownloadInfo());
                    u.setStatus(getStatus());
                }
            }
        }

        @Override
        public boolean filter(DownloadInfo downloadInfo) {
            return super.filter(downloadInfo);
        }

        @Override
        public void onSuccess()
        {
            super.onSuccess();
            APK.with(MainActivity.this)
                    .from(getDownloadInfo().getFilePath())
                    .forceInstall();

            for (updatable u: CurrentUpdatables)
            {
                if(u.getUrl() == getDownloadInfo().getUrl())
                {
                    updatables.remove(u);
                    Pump.getDownloadingList().remove(getDownloadInfo());
                }
            }
        }

        @Override
        public void onFailed()
        {
            super.onFailed();
        }
    };

    DownloadObserver downloadObserverBulk = new DownloadObserver() {
        @Override
        public void onProgress(int progress) {

            for (updatable u: CurrentUpdatables)
            {
                if(u.getUrl() == getDownloadInfo().getUrl())
                {
                    u.setDownloadInfo(getDownloadInfo());
                    u.setStatus(getStatus());
                }
            }
        }

        @Override
        public boolean filter(DownloadInfo downloadInfo) {
            return super.filter(downloadInfo);
        }

        @Override
        public void onSuccess() {
            super.onSuccess();

            super.onSuccess();
            APK.with(MainActivity.this)
                    .from(getDownloadInfo().getFilePath())
                    .forceInstall();

            for (updatable u: CurrentUpdatables)
            {
                if(u.getUrl() == getDownloadInfo().getUrl())
                {
                    updatables.remove(u);
                    Pump.getDownloadingList().remove(getDownloadInfo());
                }
            }

            if(updating < updatables.size())
            {
                File file = new File(Environment.getDownloadCacheDirectory(),updatables.get(updating++).appId);
                Pump.newRequest(updatables.get(updating++).url,file.getAbsolutePath()).submit();
                updatables.get(updating++).setStatus(DownloadInfo.Status.RUNNING);
            }
            else
            {
                Pump.unSubscribe(downloadObserverBulk);
                CurrentUpdatables.clear();
                updatables.clear();
            }
        }

        @Override
        public void onFailed() {
            super.onFailed();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        downloadObserver.enable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadObserver.disable();
    }
}
