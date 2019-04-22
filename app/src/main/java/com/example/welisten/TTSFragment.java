package com.example.welisten;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class TTSFragment extends Fragment {
    private EditText editText;
    private Button convertBtn, selectFileBtn;
    private TextToSpeech mTextToSpeech;
    private Spinner spinner;
    private TextView fileText;
    public static int PICK_FILE = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText = getActivity().findViewById(R.id.editText);
        convertBtn = getActivity().findViewById(R.id.convertBtn);
        selectFileBtn = getActivity().findViewById(R.id.selectFileBtn);
        fileText = getActivity().findViewById(R.id.fileText);
        fileText.setText("");
        spinner = getActivity().findViewById(R.id.langSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.languages, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedLanguage = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                selectedLanguage = "English(US)";
            }
        });

        mTextToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    mTextToSpeech.setLanguage(Locale.US);
                }
                else Toast.makeText(getActivity().getApplicationContext(), "TTS initialization failed", Toast.LENGTH_SHORT).show();
            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                DBHandler dbHandler = new DBHandler(getActivity().getApplicationContext(), null, null, 1);
                InputHistory inputHistory = new InputHistory(input, new Date().toString());
                dbHandler.addHandler(inputHistory);
                mTextToSpeech.speak(input, TextToSpeech.QUEUE_FLUSH,null, this.hashCode() + "");
            }
        });

        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("text/plain");
                startActivityForResult(i, PICK_FILE);


            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String fileContent = readTextFile(uri);
                mTextToSpeech.speak(fileContent, TextToSpeech.QUEUE_FLUSH,null, this.hashCode() + "");
                fileText.setText(fileContent);
                Toast.makeText(getContext(), fileContent, Toast.LENGTH_LONG).show();
            } else {
                Log.i(TAG, data.toString());
            }
        }
    }
    private String readTextFile(Uri uri){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
