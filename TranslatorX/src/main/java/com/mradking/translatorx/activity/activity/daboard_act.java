

package com.mradking.translatorx.activity.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mradking.translatorx.R;
import com.mradking.translatorx.activity.interf.tras_result_call;
import com.mradking.translatorx.activity.utility.TransX;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class daboard_act extends AppCompatActivity {

        EditText et_1;
        TextView txt, txt_lan_1, txt_lan_2;
        TextToSpeech textToSpeech;
        ClipboardManager clipboard;
        Dialog dialog;
        private ImageView micButton, sp_1, sp_2, img_swap, cp_1,cp_2;
        private static final int REQUEST_CODE_SPEECH_INPUT = 1;
        String result_st,input_st;

        int count = 0;

        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            setContentView(R.layout.dasboard);






                et_1 = findViewById(R.id.et_1);
                txt = findViewById(R.id.txt);
                txt_lan_1 = findViewById(R.id.txt_lan_1);
                txt_lan_2 = findViewById(R.id.txt_lan_2);
                sp_1 = findViewById(R.id.sp_1);
                sp_2 = findViewById(R.id.sp_2);
                cp_1 = findViewById(R.id.cp_1);
               cp_2 = findViewById(R.id.cp_1);
                img_swap = findViewById(R.id.img_swap);
                micButton = findViewById(R.id.mic);
                clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                dialog = new Dialog(daboard_act.this, android.R.style.Theme_Dialog);


                txt_lan_1.setText(getString(R.string.first));
                txt_lan_2.setText(getString(R.string.second));

                img_swap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                if (count == 0) {

                                        count = 1;
                                        txt_lan_1.setText(getString(R.string.second));
                                        txt_lan_2.setText(getString(R.string.first));

                                    et_1.setText(result_st);
                                    txt.setText(input_st);

                                } else if (count == 1) {

                                        count = 0;

                                        txt_lan_1.setText(getString(R.string.first));
                                        txt_lan_2.setText(getString(R.string.second));

                                    et_1.setText(input_st);
                                    txt.setText(result_st);


                                }


                        }
                });


            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    // Check if initialization was successful
                    if (status == TextToSpeech.SUCCESS) {
                        // Check if the language is available
                        int result = textToSpeech.isLanguageAvailable(Locale.US);

                        if (result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            // Language is available, set it
                            textToSpeech.setLanguage(Locale.US);
                        } else {
                            // Language is not available, handle the case accordingly
                            Toast.makeText(getApplicationContext(), "Language not available for Text-to-Speech", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Initialization failed, handle the case accordingly
                        Toast.makeText(getApplicationContext(), "Text-to-Speech initialization failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            sp_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                textToSpeech.speak(et_1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                        }
                });


                sp_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                textToSpeech.speak(txt.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                        }
                });


                et_1.setOnKeyListener(new View.OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                                // If the event is a key-down event on the "enter" button
                                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                        // Perform action on key press


                                        TransX translate = new TransX();


                                        if (count == 0) {

                                                translate.TranslateX("en", "hi", et_1.getText().toString()
                                                        , new tras_result_call() {
                                                                @Override
                                                                public void susess(String messagae) {
                                                                        txt.setText(messagae);

                                                                        input_st=et_1.getText().toString();
                                                                        result_st=messagae;

                                                                }

                                                                @Override
                                                                public void failed(String error) {

                                                                }
                                                        });


                                        } else if (count == 1) {
                                                translate.TranslateX("hi", "en", et_1.getText().toString()
                                                        , new tras_result_call() {
                                                                @Override
                                                                public void susess(String messagae) {
                                                                        txt.setText(messagae);

                                                                }

                                                                @Override
                                                                public void failed(String error) {

                                                                }
                                                        });

                                        }

                                        return true;
                                }
                                return false;
                        }
                });


                micButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(count==0){
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");  // Set the desired language code here
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                                try {
                                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }else if(count==1){
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi");  // Set the desired language code here
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                                try {
                                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }

                        }
                });


                cp_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

// Create a new ClipData with the text to be copied
                                String text = et_1.getText().toString();
                                ClipData clipData = ClipData.newPlainText("Label", text);
                                clipboard.setPrimaryClip(clipData);
                                Toast.makeText(daboard_act.this, "text copied to clipboard", Toast.LENGTH_SHORT).show();

                        }
                });

            cp_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

// Create a new ClipData with the text to be copied
                    String text = et_1.getText().toString();
                    ClipData clipData = ClipData.newPlainText("Label", text);
                    clipboard.setPrimaryClip(clipData);
                    Toast.makeText(daboard_act.this, "text copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode,
                                        @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
                        if (resultCode == RESULT_OK && data != null) {
                                ArrayList<String> result = data.getStringArrayListExtra(
                                        RecognizerIntent.EXTRA_RESULTS);
                                et_1.setText(Objects.requireNonNull(result).get(0));


                                TransX translate = new TransX();


                                if (count == 0) {

                                        translate.TranslateX("en", "hi", et_1.getText().toString()
                                                , new tras_result_call() {
                                                        @Override
                                                        public void susess(String messagae) {
                                                                txt.setText(messagae);

                                                        }

                                                        @Override
                                                        public void failed(String error) {

                                                        }
                                                });


                                } else if (count == 1) {
                                        translate.TranslateX("hi", "en", et_1.getText().toString()
                                                , new tras_result_call() {
                                                        @Override
                                                        public void susess(String messagae) {
                                                                txt.setText(messagae);

                                                        }

                                                        @Override
                                                        public void failed(String error) {

                                                        }
                                                });

                                }
                        }
                }
        }

}