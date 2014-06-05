package my.app.free.musicloader;

import android.content.Context;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by loki on 2014. 6. 5..
 */
public class MultiStateButton extends ImageButton {

    private ArrayList<Integer> _resources;
    private int _currentState = 0;

    public MultiStateButton(Context context, ArrayList<Integer> resources) {
        super(context);
        _resources = resources;

        this.setBackgroundResource(_resources.get(0));
    }

    public void Next() {
        _currentState++;
        if (_currentState == _resources.size())
            _currentState = 0;

        this.setBackgroundResource(_resources.get(_currentState));
    }

    public int GetCurrentState() {
        return _currentState;
    }
}
