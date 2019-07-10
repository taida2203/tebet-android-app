package com.tebet.mojual.view;

import androidx.appcompat.app.AppCompatActivity;

public class LockActivity extends AppCompatActivity {
//    public static final int CHECK_PIN = 0;
//    public static final int ADD_PIN = 1;
//    public static final int VERIFY_PIN = 2;
//
////    int screenMode = CHECK_PIN;
////    String tempPin;
////    int retryCount = 0;
////
////    @BindView(R.id.tv_error)
////    TextView tvError;
////
////    @BindView(R.id.btn_close)
////    Button btnClose;
////
////    @BindView(R.id.warning)
////    View tvWarning;
////
////    @BindView(R.id.et_code)
////    EditText etCode;
////
////    @BindView(R.id.app_bar)
////    Toolbar toolbar;
////
////    @BindViews({R.id.et_code_1, R.id.et_code_2, R.id.et_code_3, R.id.et_code_4})
////    List<TextView> etCodes;
////
////    @OnClick({R.id.et_code_1, R.id.et_code_2, R.id.et_code_3, R.id.et_code_4})
////    public void requestFocus() {
////        if (etCode.requestFocus()) {
////            etCode.clearFocus();
////        }
////        for (int i = 0; i < etCodes.size(); i++) {
////            etCodes.get(i).clearFocus();
////        }
////        etCode.requestFocus();
////        showSoftKeyboard(etCode);
////    }
////
////    @OnClick(R.id.btn_close)
////    protected void close() {
////        finish();
////        if (screenMode == CHECK_PIN) {
//////            PreferenceUtils.saveString(MasbroConstant.PIN_CODE, "");
//////            Intent intent = new Intent(LockActivity.this, SupportActivity.class);
//////            intent.putExtra(MasbroConstant.EXTRA_FORCE_LOGOUT, true);
//////            startActivity(intent);
////        }
////    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        LanguageUtil.Companion.getInstance().updateLanguage(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lock);
//        ButterKnife.bind(this);
//
//        setSupportActionBar(toolbar);
//
////        screenMode = getIntent().getIntExtra(MasbroConstant.PIN_TYPE_INPUT, CHECK_PIN);
//
//        if (screenMode == ADD_PIN || screenMode == VERIFY_PIN) {
//            btnClose.setText(R.string.general_button_cancel);
//            tvWarning.setVisibility(View.GONE);
//            if (toolbar != null) {
////                toolbar.setTitle(R.string.pin_set);
//            }
//        } else {
////            btnClose.setText(R.string.button_logout);
//            tvWarning.setVisibility(View.VISIBLE);
//            if (toolbar != null) {
////                toolbar.setTitle(R.string.pin_enter);
//            }
//        }
//
//        etCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String inputtedCode = "";
//                try {
//                    inputtedCode = etCode.getText().toString();
//                } catch (Exception e) {
//                    Timber.e(e);
//                }
//                for (int i = 0; i < inputtedCode.length(); i++) {
//                    etCodes.get(i).setText(inputtedCode.substring(i, i + 1));
//                }
//                for (int i = inputtedCode.length(); i < 4; i++) {
//                    etCodes.get(i).setText("");
//                }
//                if (inputtedCode.length() >= 4) {
//                    String pin = etCode.getText().toString();
//                    switch (screenMode) {
//                        case CHECK_PIN:
//                            if (toolbar != null) {
////                                toolbar.setTitle(R.string.pin_enter);
//                            }
////                            String currentCode = PreferenceUtils.getString(MasbroConstant.PIN_CODE, tempPin);
////                            if (pin.equals(currentCode)) {
////                                if (isLoggedIn()) {
////                                    finish();
////                                    startPermissionActivity();
////                                } else {
////                                    navigateToLogin();
////                                }
////                            } else {
////                                retryCount++;
////                                if (retryCount > 5) {
////                                    resetInputPin();
//////                                    tvError.setText(R.string.pin_message_not_matched);
////                                    btnClose.performClick();
////                                } else {
////                                    resetInputPin();
//////                                    tvError.setText(R.string.pin_message_wrong);
////                                }
////                            }
//                            break;
//                        case ADD_PIN:
//                            if (toolbar != null) {
////                                toolbar.setTitle(R.string.pin_confirm);
//                            }
//                            tempPin = pin;
//                            screenMode = VERIFY_PIN;
//                            resetInputPin();
//                            break;
//                        case VERIFY_PIN:
//                            if (pin.equals(tempPin)) {
////                                PreferenceUtils.saveString(MasbroConstant.PIN_CODE, tempPin);
//                                setResult(RESULT_OK);
//                                finish();
//                            } else {
//                                resetInputPin();
////                                tvError.setText(R.string.pin_message_not_matched);
//                            }
//                            break;
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * Shows the soft keyboard
//     */
//    public void showSoftKeyboard(View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        view.requestFocus();
//        inputMethodManager.showSoftInput(view, 0);
//    }
//
//
////    private boolean isLoggedIn() {
////        User user = (User) PreferenceUtils.getObject(MasbroConstant.TEMP_USER_PROFILE, User.class);
////        return user != null;
//
////    }
//
//    private void navigateToLogin() {
//        finish();
////        startActivity(new Intent(this, SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onBackPressed() {
//        // Lock back press
//    }
//
//    public void startPermissionActivity() {
////        if (!Utility.getInstance().isPermissionValid()) {
////            startActivity(new Intent(this, PermissionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
////        }
//    }
//
//    private void resetInputPin() {
//        tvError.setText("");
//        etCode.setText("");
//    }
}
