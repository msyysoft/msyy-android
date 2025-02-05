package g.support.app.alistener;

import androidx.appcompat.app.AppCompatActivity;

public interface ActivityFinishListener {
    void whenActivityBeforeFinish(AppCompatActivity activity);

    void whenActivityAfterFinish(AppCompatActivity activity);
}