package com.uml.lexueschedule.View.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uml.lexueschedule.R;
import com.uml.lexueschedule.Data.Model.Course;
import com.uml.lexueschedule.Data.Model.Schedule;
import com.uml.lexueschedule.Util.Connect;
import com.uml.lexueschedule.Util.Editcourse;

/**
 * by 孙瑞洲， 郭晓凡
 */

public class EditCourseActivity extends AppCompatActivity {
    private Schedule mySchedule=Schedule.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar)
            actionBar.setDisplayShowTitleEnabled(false);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Intent intent=getIntent();
        final int lessonID=intent.getIntExtra("lessonID",-1);
        Log.e("tag","lessonID in editActivity"+lessonID);
        Course course=Schedule.getcoursebyid(lessonID);

        final EditText courseName=(EditText)findViewById(R.id.courseNameInEdit);
        final EditText classroom=(EditText)findViewById(R.id.classroomInEdit);
        final EditText weeks=(EditText)findViewById(R.id.WeeksInEdit);
        final EditText daySlot=(EditText)findViewById(R.id.daySlotInEdit);
        final EditText teacher=(EditText)findViewById(R.id.teacherInEdit);

        courseName.setText(course.getTitle());
        classroom.setText(course.getAddress());
        weeks.setText(course.getStartweek()+"-"+course.getEndweek());
        String dayofWeek="";
        switch (course.getWeekday())
        {
            case 1:
                dayofWeek="周一";
                break;
            case 2:
                dayofWeek="周二";
                break;
            case 3:
                dayofWeek="周三";
                break;
            case 4:
                dayofWeek="周四";
                break;
            case 5:
                dayofWeek="周五";
                break;
            case 6:
                dayofWeek="周六";
                break;
            case 7:
                dayofWeek="周日";
                break;
                default:
                break;
        }
        daySlot.setText(dayofWeek+course.getStarttime()+"-"+course.getEndtime());
        teacher.setText(course.getTeacher());

        final AppCompatActivity ac=this;
        //点击保存
        Button buttonSave=(Button)findViewById(R.id.saveEdit);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","yes0");
                //判断网络是否可用
                if(!Connect.isConnectIsNomarl(ac))
                {
                    Toast.makeText(EditCourseActivity.this,"网络无连接",Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取界面信息封装成一个新的类
                //周几上课，开始节数，结束节数，课程名，开始周，结束周，课程名，教师名
                //int wday,int stime,int etime,String ti,int sweek,int eweek,String a,String te;

                Log.e("tag","yes0.5");
                String courseNamestr= courseName.getText().toString();
                Log.e("tag","yes1"+courseNamestr);
                String classromstr=classroom.getText().toString();
                String teacherstr=teacher.getText().toString();
                String time=daySlot.getText().toString();
                String dayOfWeekstr="";
                int starttime;
                int endtime;
                try{
                    dayOfWeekstr=time.substring(0,2);
                    starttime=Integer.valueOf(time.substring(2,time.indexOf('-')));
                    endtime=Integer.valueOf(time.substring(time.indexOf('-')+1));
                }
                catch (Exception e)
                {
                    Toast.makeText(EditCourseActivity.this,"请按照\"周三1-2\"格式填写节数",Toast.LENGTH_SHORT).show();
                    return;
                }
                int dayOfWeekint=-1;
                switch (dayOfWeekstr)
                {
                    case "周一":
                        dayOfWeekint=1;
                        break;
                    case "周二":
                        dayOfWeekint=2;
                        break;
                    case "周三":
                        dayOfWeekint=3;
                        break;
                    case "周四":
                        dayOfWeekint=4;
                        break;
                    case "周五":
                        dayOfWeekint=5;
                        break;
                    case "周六":
                        dayOfWeekint=6;
                        break;
                    case "周日":
                        dayOfWeekint=7;
                        break;
                    default:
                        break;
                }
                String weeknumberstr=weeks.getText().toString();
                int startweekint=-1;
                int endweekint=-1;
                try{
                    startweekint=Integer.valueOf(weeknumberstr.substring(0,weeknumberstr.indexOf('-')));
                    endweekint=Integer.valueOf(weeknumberstr.substring(weeknumberstr.indexOf('-')+1));
                }
                catch (Exception e)
                {
                    Toast.makeText(EditCourseActivity.this,"请按照\"1-15\"格式填写周数",Toast.LENGTH_SHORT).show();
                    return;
                }
                Course newcourse=new Course(dayOfWeekint,starttime,endtime,courseNamestr,startweekint,endweekint,classromstr,teacherstr,-1);
                newcourse.print();
                Course oldcourse=Schedule.getcoursebyid(lessonID);
                Editcourse.edit(EditCourseActivity.this, oldcourse,newcourse);
                oldcourse.print();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
