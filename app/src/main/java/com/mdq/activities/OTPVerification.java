package com.mdq.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdq.ViewModel.ValidateOTPResponseViewModel;
import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;
import com.mdq.interfaces.ViewResponseInterface.ValidateOTPResponseInterface;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityOtpverificationBinding;
import com.mdq.pojo.jsonresponse.ErrorBody;
import com.mdq.pojo.jsonresponse.GenerateValidateOTPResponseModel;
import com.mdq.utils.PreferenceManager;


public class OTPVerification extends AppCompatActivity implements ValidateOTPResponseInterface {

    ActivityOtpverificationBinding activityOtpverificationBinding;
    PreferenceManager preferenceManager;
    ValidateOTPResponseViewModel validateOTPResponseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtpverificationBinding = ActivityOtpverificationBinding.inflate(getLayoutInflater());
        setContentView(activityOtpverificationBinding.getRoot());

        validateOTPResponseViewModel=new ValidateOTPResponseViewModel(this,this);
        //set click
        click();
        keyBoard();

        activityOtpverificationBinding.num.setText("+91 " + getPreferenceManager().getPrefMobile());
    }

    private void keyBoard() {

        activityOtpverificationBinding.editthree.requestFocus();
        activityOtpverificationBinding.editthree.setRawInputType(InputType.TYPE_NULL);
        activityOtpverificationBinding.editthree.setFocusable(true);
        activityOtpverificationBinding.editfour.setRawInputType(InputType.TYPE_NULL);
        activityOtpverificationBinding.editfour.setFocusable(true);
        activityOtpverificationBinding.editfive.setRawInputType(InputType.TYPE_NULL);
        activityOtpverificationBinding.editfive.setFocusable(true);
        activityOtpverificationBinding.editsix.setRawInputType(InputType.TYPE_NULL);
        activityOtpverificationBinding.editsix.setFocusable(true);




        activityOtpverificationBinding.editthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpverificationBinding.editthree.getText().length() == 1) {
                    activityOtpverificationBinding.editfour.requestFocus();
                    activityOtpverificationBinding.editthree.setTextColor(getResources().getColor(R.color.black));
                } else {
                    activityOtpverificationBinding.editthree.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        activityOtpverificationBinding.editfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpverificationBinding.editfour.getText().length() == 1) {
                    activityOtpverificationBinding.editfive.requestFocus();
                    activityOtpverificationBinding.editfour.setTextColor(getResources().getColor(R.color.black));

                } else {
                    activityOtpverificationBinding.editthree.requestFocus();
                    activityOtpverificationBinding.editfour.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        activityOtpverificationBinding.editfive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (activityOtpverificationBinding.editfive.getText().length() == 1) {
                    activityOtpverificationBinding.editsix.requestFocus();
                    activityOtpverificationBinding.editfive.setTextColor(getResources().getColor(R.color.black));

                } else {
                    activityOtpverificationBinding.editfour.requestFocus();
                    activityOtpverificationBinding.editfive.setTextColor(getResources().getColor(R.color.black));

                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        activityOtpverificationBinding.editsix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (activityOtpverificationBinding.editsix.getText().length() == 1) {
                    activityOtpverificationBinding.editsix.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    activityOtpverificationBinding.editfive.requestFocus();
                    activityOtpverificationBinding.editsix.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        //for numbers
        activityOtpverificationBinding.one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("1");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("1");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("1");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("1");
                }
            }
        });
        activityOtpverificationBinding.two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("2");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("2");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("2");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("2");
                }
            }
        });
        activityOtpverificationBinding.three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("3");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("3");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("3");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("3");
                }
            }
        });
        activityOtpverificationBinding.four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("4");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("4");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("4");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("4");
                }
            }
        });
        activityOtpverificationBinding.five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("5");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("5");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("5");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("5");
                }
            }
        });
        activityOtpverificationBinding.six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("6");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("6");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("6");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("6");
                }
            }
        });
        activityOtpverificationBinding.seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("7");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("7");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("7");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("7");
                }
            }
        });
        activityOtpverificationBinding.eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("8");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("8");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("8");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("8");
                }
            }
        });
        activityOtpverificationBinding.nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("9");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("9");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("9");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("9");
                }
            }
        });
        activityOtpverificationBinding.zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    activityOtpverificationBinding.editthree.setText("0");
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    activityOtpverificationBinding.editfour.setText("0");
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    activityOtpverificationBinding.editfive.setText("0");
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    activityOtpverificationBinding.editsix.setText("0");
                }
            }
        });

        //backspace
        activityOtpverificationBinding.backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityOtpverificationBinding.editthree.hasFocus()) {
                    if(activityOtpverificationBinding.editthree.length()==1){
                        activityOtpverificationBinding.editthree.setText("");
                    }
                } else if (activityOtpverificationBinding.editfour.hasFocus()) {
                    if(activityOtpverificationBinding.editfour.length()==1) {
                        activityOtpverificationBinding.editfour.setText("");
                        activityOtpverificationBinding.editthree.requestFocus();
                    } else {
                        activityOtpverificationBinding.editthree.setText("");
                        activityOtpverificationBinding.editthree.requestFocus();
                    }
                } else if (activityOtpverificationBinding.editfive.hasFocus()) {
                    if(activityOtpverificationBinding.editfive.length()==1){
                        activityOtpverificationBinding.editfive.setText("");
                        activityOtpverificationBinding.editfour.requestFocus();}
                    else {
                        activityOtpverificationBinding.editfour.setText("");
                        activityOtpverificationBinding.editfour.requestFocus();
                    }
                } else if (activityOtpverificationBinding.editsix.hasFocus()) {
                    if(activityOtpverificationBinding.editsix.length()==1){
                        activityOtpverificationBinding.editsix.setText("");
                        activityOtpverificationBinding.editfive.requestFocus();}
                    else {
                        activityOtpverificationBinding.editfive.setText("");
                        activityOtpverificationBinding.editfive.requestFocus();
                    }
                }
            }
        });
    }

    private void click() {


        activityOtpverificationBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        activityOtpverificationBinding.Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP=activityOtpverificationBinding.editthree.getText().toString().trim()+
                        activityOtpverificationBinding.editfour.getText().toString().trim()+
                        activityOtpverificationBinding.editfive.getText().toString().trim()+
                        activityOtpverificationBinding.editsix.getText().toString().trim();
                        if(OTP.length()==4){

                            validateOTPResponseViewModel.setMobile(getPreferenceManager().getPrefMobile());
                            validateOTPResponseViewModel.setOtp_number(OTP);
                            validateOTPResponseViewModel.generateValidationOTPRequest();

                        }else{
                            Toast.makeText(OTPVerification.this, "Enter Correct OTP!", Toast.LENGTH_SHORT).show();
                            return;
                        }
            }
        });
    }

    /**
     * @return
     * @brief initializing the preferenceManager from shared preference for local use in this activity
     */
    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager.getInstance();
            preferenceManager.initialize(getApplicationContext());
        }
        return preferenceManager;
    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, String errorMessage) {

    }

    @Override
    public void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage) {

    }

    @Override
    public void generateValidateOTPProcessed(GenerateValidateOTPResponseModel generateValidateOTPResponseModel) {

        if(generateValidateOTPResponseModel.message.equals("OTP number verified successfully")) {
            startActivity(new Intent(OTPVerification.this, RegisterActivity.class));
        }else {
            Toast.makeText(this, "OTP is wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailure(ErrorBody errorBody, int statusCode) {

    }
}