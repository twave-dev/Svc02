package com.twave.svc02.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * TODO 03-03 : FireBase JobService 를 상속받은 클래스를 생성한다.
 */

public class CounterFirebaseJobService extends JobService{

    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        // TODO 03-05 : 백그라운드에서 실행할 작업을 생성한다.
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context context = CounterFirebaseJobService.this;
                CounterTasks.executeTask(context, CounterTasks.ACTION_INC_SCH_COUNT);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };

        // 백그라운드 작업 실행
        mBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
