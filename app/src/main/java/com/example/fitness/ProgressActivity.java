package com.example.fitness;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ProgressActivity  extends Fragment {

    EditText progress_year_edittext;
    EditText progress_month_edittext;
    EditText progress_day_edittext;
    Button   progress_date_input_button;
    TextView progress_day_one_textview;
    TextView progress_day_two_textview;
    TextView progress_day_three_textview;
    TextView progress_day_four_textview;
    TextView progress_day_five_textview;
    TextView progress_day_six_textview;
    TextView progress_day_seven_textview;




    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
    */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_progress, container, false);

        progress_year_edittext = view.findViewById(R.id.progress_year_edittext);
        progress_month_edittext = view.findViewById(R.id.progress_month_edittext);
        progress_day_edittext = view.findViewById(R.id.progress_day_edittext);
        progress_day_one_textview = view.findViewById(R.id.progress_day_one_textview);
        progress_day_two_textview = view.findViewById(R.id.progress_day_two_textview);
        progress_day_three_textview = view.findViewById(R.id.progress_day_three_textview);
        progress_day_four_textview = view.findViewById(R.id.progress_day_four_textview);
        progress_day_five_textview = view.findViewById(R.id.progress_day_five_textview);
        progress_day_six_textview = view.findViewById(R.id.progress_day_six_textview);
        progress_day_seven_textview = view.findViewById(R.id.progress_day_seven_textview);



        final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();



        view.findViewById(R.id.progress_date_input_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if date is empty before continuing
                if (!progress_year_edittext.getText().toString().matches("") &&
                        !progress_month_edittext.getText().toString().matches("") &&
                        !progress_day_edittext.getText().toString().matches("")
                ) {


                    int year = Integer.parseInt(progress_year_edittext.getText().toString());
                    int month = Integer.parseInt(progress_month_edittext.getText().toString());
                    int day = Integer.parseInt(progress_day_edittext.getText().toString());


                    GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                    gc.clear();
                    gc.set(year, month - 1, day + 1);
                    long left = gc.getTimeInMillis();

                    Dates date = db.datesDao().findByDate(left);
                    //progress_day_one_textview.setText(Long.toString(left));
                    DateFormat simple = new SimpleDateFormat("E MMM dd");
                    DateFormat dayformat = new SimpleDateFormat("E");
                    if (date != null) {
                        Date result = new Date(left);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_one_textview.setText(text);
                    } else {
                        Date result = new Date(left - 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_one_textview.setText(text + "No data found");
                    }


                    date = db.datesDao().findByDate(left - 86400000);
                    if (date != null) {
                        Date result = new Date(left - 86400000);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_two_textview.setText(text);
                    } else {
                        Date result = new Date(left - 1 * 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_two_textview.setText(text + "No data found");
                    }
                    date = db.datesDao().findByDate(left - 2 * 86400000);
                    if (date != null) {
                        Date result = new Date(left - 2 * 86400000);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_three_textview.setText(text);
                    } else {
                        Date result = new Date(left - 2 * 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_three_textview.setText(text + "No data found");
                    }

                    date = db.datesDao().findByDate(left - 3 * 86400000);
                    if (date != null) {
                        Date result = new Date(left - 3 * 86400000);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_four_textview.setText(text);
                    } else {
                        Date result = new Date(left - 3 * 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_four_textview.setText(text + "No data found");
                    }
                    date = db.datesDao().findByDate(left - 4 * 86400000);
                    if (date != null) {
                        Date result = new Date(left - 4 * 86400000);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_five_textview.setText(text);
                    } else {
                        Date result = new Date(left - 4 * 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_five_textview.setText(text + "No data found");
                    }
                    date = db.datesDao().findByDate(left - 5 * 86400000);
                    if (date != null) {
                        Date result = new Date(left - 5 * 86400000);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_six_textview.setText(text);
                    } else {
                        Date result = new Date(left - 5 * 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_six_textview.setText(text + "No data found");
                    }

                    date = db.datesDao().findByDate(left - 6 * 86400000);
                    if (date != null) {
                        Date result = new Date(left - 6 * 86400000);
                        String text = simple.format(result) + "\n" + Long.toString(date.calories) + " calories";

                        if (dayformat.format(result).matches("Sun")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Mon")) {
                            text = text + "\nNot a weight increase day";
                        }
                        if (dayformat.format(result).matches("Tue")) {
                            text = text + "\nSquat: " + (date.squat - date.weightincrease) + " lbs -> " + date.squat + " lbs";
                        }
                        if (dayformat.format(result).matches("Wed")) {
                            text = text + "\nOHP: " + (date.ohp - date.weightincrease) + " lbs -> " + date.ohp + " lbs";
                        }
                        if (dayformat.format(result).matches("Thu")) {
                            text = text + "\nDeadlift: " + (date.deadlift - date.weightincrease) + " lbs -> " + date.deadlift + " lbs";
                        }
                        if (dayformat.format(result).matches("Fri")) {
                            text = text + "\nBench Press: " + (date.bench - date.weightincrease) + " lbs -> " + date.bench + " lbs";
                        }
                        if (dayformat.format(result).matches("Sat")) {
                            text = text + "\nNot a weight increase day";
                        }
                        progress_day_seven_textview.setText(text);
                    } else {
                        Date result = new Date(left - 6 * 86400000);
                        String text = simple.format(result) + "\n";
                        progress_day_seven_textview.setText(text + "No data found");
                    }
                }
            }
        });




        return view;
    }


}
