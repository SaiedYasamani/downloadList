package com.example.saied.downloadlist;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huxq17.download.DownloadInfo;
import com.huxq17.download.Pump;
import java.io.File;
import java.util.List;
import static com.huxq17.download.DownloadInfo.Status.STOPPED;

public class Adapter extends RecyclerView.Adapter<Adapter.VH> {

    List<updatable> data;
    Context context;

    public Adapter(List<updatable> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.appName.setText(data.get(i).getAppId());

        if (data.get(i).getStatus() != null) {
            DownloadInfo.Status status = data.get(i).getStatus();
            switch (status) {
                case WAIT:
                    vh.button.setText("در صف دانلود");
                    vh.progressBar.setVisibility(View.VISIBLE);
                    vh.progressBar.setIndeterminate(true);
                    break;
                case FAILED:
                    vh.button.setText("تلاش مجدد");
                    vh.progressBar.setVisibility(View.GONE);
                    break;
                case PAUSED:
                    vh.button.setText("به روز رسانی");
                    vh.progressBar.setVisibility(View.GONE);
                    break;
                case PAUSING:
                    vh.button.setText("در صف دانلود");
                    vh.progressBar.setVisibility(View.VISIBLE);
                    vh.progressBar.setIndeterminate(true);
                    break;
                case RUNNING:
                    vh.button.setText("لغو");
                    vh.progressBar.setVisibility(View.VISIBLE);
                    vh.progressBar.setIndeterminate(false);
                    break;
                case STOPPED:
                    vh.button.setText("به روز رسانی");
                    vh.progressBar.setVisibility(View.GONE);
                    break;
                case FINISHED:
                    vh.button.setText("نصب");
                    vh.progressBar.setVisibility(View.GONE);
                    break;
                default:
            }
        } else {
            vh.button.setText("به روز رسانی");
            vh.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView appName;
        ProgressBar progressBar;
        Button button;

        public VH(@NonNull View itemView) {
            super(itemView);

            appName = itemView.findViewById(R.id.appName);
            progressBar = itemView.findViewById(R.id.progress);
            button = itemView.findViewById(R.id.button);
        }


        @Override
        public void onClick(View v) {

            DownloadInfo.Status status = STOPPED;
            if(data.get(getAdapterPosition()).getStatus() != null)
            {
                status = data.get(getAdapterPosition()).getStatus();
            }

            switch (status) {
                case STOPPED:
                    File file = new File(Environment.getDownloadCacheDirectory(),data.get(getAdapterPosition()).appId);
                    Pump.newRequest(data.get(getAdapterPosition()).getUrl(), file.getAbsolutePath()).submit();
                    break;
                case PAUSING:
                    break;
                case PAUSED:
                    Pump.resume(data.get(getAdapterPosition()).getDownloadInfo());
                    break;
                case WAIT:
                    break;
                case RUNNING:
                    Pump.pause(data.get(getAdapterPosition()).getDownloadInfo());
                    break;
                case FINISHED:
                    APK.with(context)
                            .from(data.get(getAdapterPosition()).getDownloadInfo().getFilePath())
                            .forceInstall();
                    break;
                case FAILED:
                    Pump.resume(data.get(getAdapterPosition()).getDownloadInfo());
                    break;
            }
        }
    }
}

