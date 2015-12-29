package com.cloudfoyo.magazine;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SearchFragment extends Fragment {


    private EditText editText;
    private ListView listView;

    private InputMethodManager   imanager;
    public SearchFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = (EditText)view.findViewById(R.id.search);
        imanager =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {

                    editText.clearFocus();
                    String t = editText.getText().toString().trim();
                    if(t.length() > 0)
                        doSearch(editText.getText().toString());

                }

                return false;
            }
        });

        listView = (ListView)view.findViewById(R.id.listview);


    }


    public void doSearch(String q)
    {
        Toast.makeText(getActivity(), q, Toast.LENGTH_SHORT).show();
    }

}