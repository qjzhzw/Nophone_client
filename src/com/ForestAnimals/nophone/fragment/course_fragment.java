package com.ForestAnimals.nophone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.course.course_table;

/**
 * Created by MyWorld on 2016/6/12.
 * 森林树的页面
 */
public class course_fragment extends Fragment {

    private EditText editText_course_number;
    private EditText editText_course_password;
    private Button button_course_nextstep;

    public void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_layout, container,
                false);


        //声明ID集合
        editText_course_number = (EditText) view.findViewById(R.id.editText_course_number);
        editText_course_password = (EditText) view.findViewById(R.id.editText_course_password);
        button_course_nextstep = (Button) view.findViewById(R.id.button_course_nextstep);

        button_course_nextstep.setOnClickListener(listener);

        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            Intent intent = new Intent();
            switch (button.getId()) {
                case R.id.button_course_nextstep:
                    //跳转到课程表界面
                    if (editText_course_number.getText().toString().length() == 0) {
                        Toast.makeText(getActivity(), getString(R.string.no_number), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_course_password.getText().toString().length() == 0) {
                        Toast.makeText(getActivity(),  getString(R.string.no_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else {
                        intent.setClass(getActivity(), course_table.class);
                        startActivity(intent);
                        break;
                    }
            }
        }
    };

}
