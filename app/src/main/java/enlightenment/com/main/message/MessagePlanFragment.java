package enlightenment.com.main.message;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.R;
import enlightenment.com.main.ItemViewHolder;
import enlightenment.com.operationBean.PlanBean;

/**
 * Created by admin on 2018/5/27.
 */

public class MessagePlanFragment extends Fragment implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener {

    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.fragment_message_plan_recycler)
    RecyclerView mPlanRecycler;
    @BindView(R.id.fragment_message_current_plan_linear)
    LinearLayout mPlanCurrentLinear;

    RecyclerView.Adapter mPlanAdapter;

    private ArrayList<PlanBean> mCurrentPlanBean = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_plan, container, false);
        ButterKnife.bind(this, view);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        init();
        initLinear();
        initRecycler();
        return view;
    }

    private void initLinear() {
        mCurrentPlanBean.add(new PlanBean());

        if (mCurrentPlanBean != null && mCurrentPlanBean.size() > 0) {
            TextView textView = ItemViewHolder.createTextView(getActivity(),
                    56, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setText("当前计划");
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mPlanCurrentLinear.addView(textView);
        }

        for (PlanBean planBean : mCurrentPlanBean) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_message_current_plan, mPlanCurrentLinear,false);
            mPlanCurrentLinear.addView(view);
        }

    }

    private void initRecycler() {
        mPlanAdapter = new RecyclerView.Adapter() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                RecyclerView.ViewHolder holder = null;
                if (viewType == 0) {
                    View view = LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_message_plan_top, parent, false);
                    holder = new PlanTopViewHolder(view);
                } else {
                    View view = LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_message_plan, parent, false);
                    holder = new PlanViewHolder(view);
                }
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
        mPlanRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlanRecycler.setAdapter(mPlanAdapter);
        //mPlanRecycler.addItemDecoration(divider);
    }

    private void init() {
        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        schemes.add(getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        schemes.add(getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        schemes.add(getSchemeCalendar(year, month, 10, 0xFFdf1356, "议"));
        schemes.add(getSchemeCalendar(year, month, 11, 0xFFedc56d, "记"));
        schemes.add(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        schemes.add(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        schemes.add(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        schemes.add(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        schemes.add(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        mCalendarView.setSchemeDate(schemes);
        mCalendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar, boolean isClick) {
                List<Calendar.Scheme> schemeList = calendar.getSchemes();
                if (schemeList != null && schemeList.size() > 0)
                    for (Calendar.Scheme scheme : schemeList)
                        Toast.makeText(getActivity(), scheme.getScheme() + "------" + scheme.getOther(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(1, color, text, "json");
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        calendar.addScheme(1, color, text, "json");
        calendar.addScheme(1, color, text, "json");
        return calendar;
    }


    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onYearChange(int year) {

    }

    public class PlanTopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_message_plan_top_name)
        TextView mPlanTopName;
        @BindView(R.id.item_message_plan_top_icon)
        ImageView mPlanIcon;
        @BindView(R.id.item_message_plan_top_tool)
        TextView mPlanTool;

        public PlanTopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class PlanCurrentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_message_current_plan_top_name)
        TextView mCurrentPlanTopName;
        @BindView(R.id.item_message_current_plan_time)
        TextView mCurrentPlanTopTime;
        @BindView(R.id.item_message_current_plan_icon)
        ImageView mCurrentPlanIcon;
        @BindView(R.id.item_message_current_plan_name)
        TextView mCurrentPlanName;
        @BindView(R.id.item_message_current_plan_content)
        TextView mCurrentPlanContent;
        @BindView(R.id.item_message_current_plan_draw)
        ImageView mCurrentPlanDraw;

        public PlanCurrentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_message_plan_icon)
        ImageView mPlanIcon;
        @BindView(R.id.item_message_plan_name)
        TextView mPlanName;
        @BindView(R.id.item_message_plan_content)
        TextView mPlanContent;
        @BindView(R.id.item_message_plan_tool)
        TextView mPlanTool;

        public PlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
