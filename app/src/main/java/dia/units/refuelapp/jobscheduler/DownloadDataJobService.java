package dia.units.refuelapp.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class DownloadDataJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
