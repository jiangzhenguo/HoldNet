package com.jhon.code.holdnet.view;

        import android.content.Context;
        import android.util.AttributeSet;

        import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * creater : Jhon
 * time : 2018/12/28 0028
 */
public class LoopLayoutManager extends LinearLayoutManager {
    public LoopLayoutManager(Context context) {
        super(context);
    }

    public LoopLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LoopLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



}
