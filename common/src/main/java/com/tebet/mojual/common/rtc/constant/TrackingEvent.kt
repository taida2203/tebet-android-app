package com.tebet.mojual.common.rtc.constant

/**
 * Created by Mochamad Noor Syamsu on 7/30/18.
 */
object TrackingEvent {
    /**
     * intro and registration
     */
    const val SPLASH_SCREEN = "splash_pageload"
    const val LOGIN_SCREEN = "login_pageload"
    const val LOGIN_FACEBOOK = "login_facebook_pageload"
    const val LOGIN_GOOGLE_PLUS = "login_google_plus_pageload"
    const val REGISTER_FACEBOOK = "register_facebook_submit"
    const val REGISTER_GOOGLE_PLUS = "register_google_plus_submit"
    const val REGISTRATION_SCREEN = "registration_form_pageload"
    const val REGISTRATION_TIME = "registration_form_timespent"
    const val REGISTRATION_SUBMIT = "registration_form_submit"
    const val REGISTER_PHONE_NUMBER_SCREEN = "register_phone_number_pageload"
    const val REGISTER_PHONE_NUMBER_TIME = "register_phone_number_timespent"
    const val REGISTER_PHONE_NUMBER_SUBMIT = "register_phone_number_submit"
    const val REGISTER_PHONE_VERIFICATION_SCREEN = "register_phone_number_verify_pageload"
    const val REGISTER_PHONE_VERIFICATION_RESEND = "register_phone_number_verify_resend"
    const val REGISTER_PHONE_VERIFICATION_WRONG = "register_phone_number_verify_wrong"
    const val REGISTER_PHONE_VERIFICATION_SUCCESS = "register_phone_number_verify_success"
    const val REGISTER_PHONE_NUMBER_VERIFICATION_TIME = "register_phone_number_verify_timespent"
    const val REGISTRATION_FORM_PRIVATE_FB_SCREEN = "registration_form_private_fb_onload"
    const val REGISTRATION_FORM_PRIVATE_FB_SUBMIT = "registration_form_private_fb_submit"
    const val REGISTRATION_FORM_PRIVATE_FB_TIMESPENT = "registration_form_private_fb_timespent"
    const val COURSE_SELECTION_SCREEN = "course_selection_onload"
    const val COURSE_SELECTION_SUBMIT = "course_selection_submit"
    const val COURSE_SELECTION_TIMESPENT = "course_selection_timespent"

    /**
     * home screen
     */
    const val HOME_ENGLISH_SCREEN = "home_english_course_pageload"
    const val HOME_MANDARIN_SCREEN = "home_mandarin_course_pageload"
    const val HOME_JAPANESE_SCREEN = "home_japanese_course_pageload"
    const val HOME_INDONESIAN_SCREEN = "home_indonesian_course_pageload"
    const val HOME_BOOK_TRIAL_TIME = "home_click_book_trial_timespent"
    const val HOME_BOOK_TRIAL = "home_click_book_trial"

    /**
     * upcoming class screen
     */
    const val UPCOMING_CLASS_SCREEN = "upcoming_class_onload"

    /**
     * calendar screen
     */
    const val VERIFY_EMAIL = "email_verify_resend"
    const val CALENDAR_SCREEN = "calendar_pageload"
    const val CALENDAR_BOOKING_TRIAL = "book_trial_class"
    const val CALENDAR_BOOKING_TRIAL_TIME = "calendar_book_trialstudent_timespent"
    const val CALENDAR_BOOKING_PAID_TIME = "calendar_book_paidstudent_timespent"

    /**
     * teacher schedule screen
     */
    const val TEACHER_LIST_SCREEN = "teacher_list_onload"
    const val TEACHER_SCHEDULE_SCREEN = "teacher_calendar_onload"
    const val TEACHER_DETAIL_SCREEN = "teacher_detail_onload"

    /**
     * booking confirmation screen
     */
    const val BOOK_CONFIRM_SCREEN = "book_confirmation_onload"
    const val BOOK_CONFIRM_TRIAL_TIME = "book_confirmation_trialstudent_timespent"
    const val BOOK_CONFIRM_TRIAL_SUBMIT = "book_confirmation_trialstudent_submit"
    const val BOOK_CONFIRM_PAID_TIME = "book_confirmation_paidstudent_timespent"
    const val BOOK_CONFIRM_PAID_SUBMIT = "book_confirmation_paidstudent_submit"

    /**
     * progress report screen
     */
    const val PROGRESS_REPORT_SCREEN = "progress_onload"
    const val PROGRESS_REPORT_TEACHER_COMMENT_SCREEN = "teacher_comment_onload"
    const val PROGRESS_REPORT_PERIODICAL_DETAIL_SCREEN = "periodical_report_onload"
    const val PROGRESS_REPORT_CHANGE_CATEGORY = "change_progress_category_dropdown"
    const val PROGRESS_REPORT_CHANGE_LEVEL = "change_progress_level_dropdown"
    const val PROGRESS_REPORT_CHAT_SUBMIT = "chat_history_progress_submit"

    /**
     * assignment screen
     */
    const val ASSIGNMENT_TAB = "assignment_page_onload"
    const val ASSIGNMENT_DO = "do_assignment_onload"
    const val ASSIGNMENT_DO_TIME = "do_assignment_timespent"
    const val ASSIGNMENT_DO_DRAFT_TIME = "do_assignment_draft_timespent"
    const val ASSIGNMENT_DO_DRAFT = "do_assignment_draft"
    const val ASSIGNMENT_SUBMIT = "do_assignment_submit"
    const val ASSIGNMENT_DRAFT_TAB = "draft_assignment_onload"
    const val ASSIGNMENT_DRAFT_DO = "do_draft_assignment_onload"
    const val ASSIGNMENT_DONE_TAB = "done_assignment_onload"

    /**
     * material screen
     */
    const val MATERIAL_SCREEN = "material_page_onload"
    const val MATERIAL_CHANGE_CATEGORY = "change_category_material"
    const val MATERIAL_CHANGE_LEVEL = "change_level_material"
    const val MATERIAL_DOWNLOAD = "download_material"
    const val MATERIAL_OPEN_FILE = "open_material"

    /**
     * profile screen
     */
    const val PROFILE_SCREEN = "profile_page_onload"
    const val PROFILE_CHANGE_SCREEN = "change_profile_onload"
    const val PROFILE_CHANGE_SUBMIT = "change_profile_submit"

    /**
     * course change
     */
    const val COURSE_CHANGE_SCREEN = "change_course_onload"
    const val COURSE_CHANGE_SUBMIT = "change_course_submit"

    /**
     * package screen
     */
    const val MY_PACKAGE_SCREEN = "my_packages_onload"
    const val MY_PACKAGE_DETAIL = "my_packages_detail_click"

    /**
     * invoice screen
     */
    const val MY_INVOICE_SCREEN = "my_invoice_onload"
    const val MY_INVOICE_DETAIL = "my_invoice_detail_click"

    /**
     * settings screen
     */
    const val SETTINGS_SCREEN = "setting_page_onload"

    /**
     * notification setting screen
     */
    const val NOTIFICATION_SCREEN = "notification_page_onload"
    const val NOTIFICATION_SETTINGS_EDIT_SCREEN = "edit_notification_onload"
    const val NOTIFICATION_SETTINGS_SUBMIT = "edit_notification_submit"
    const val NOTIFICATION_CLICK = "notification_single_open"

    /**
     * change password screen
     */
    const val CHANGE_PASSWORD_SCREEN = "change_password_onload"
    const val CHANGE_PASSWORD_ERROR = "change_password_error"
    const val CHANGE_PASSWORD_SUBMIT = "change_password_submit"

    /**
     * about screen
     */
    const val ABOUT_SCREEN = "about_squline_onload"
    const val ABOUT_TNC_CLICK = "terms_and_condition_click"

    /**
     * logout
     */
    const val LOGOUT_SUBMIT = "logout_submit"

    /**
     * buy credit screen
     */
    const val BUY_CREDIT_SCREEN = "buy_credit_page_onload"
    const val BUY_CREDIT_DETAIL = "buy_credit_detail_click"
    const val BUY_CREDIT_PURCHASE = "buy_credit_purchase_click"
    const val BUY_CREDIT_PAYMENT_METHOD_SCREEN = "buy_credit_payment_method_onload"
    const val BUY_CREDIT_VOUCHER_SUCCESS = "buy_credit_voucher_success"
    const val BUY_CREDIT_VOUCHER_FAILED = "buy_credit_voucher_failed"
    const val BUY_CREDIT_CC_CONFIRM = "buy_credit_payment_creditcard_confirm"
    const val BUY_CREDIT_BCA_TF_CONFIRM = "buy_credit_payment_bcatransfer_confirm"
    const val BUY_CREDIT_MANDIRI_TF_CONFIRM = "buy_credit_payment_mandiritransfer_confirm"
    const val BUY_CREDIT_MANDIRI_CLICKPAY_CONFIRM = "buy_credit_mandiri_clickpay_confirm"
    const val BUY_CREDIT_PAYPAL_CONFIRM = "buy_credit_paypal_confirm"
    const val BUY_CREDIT_ANDROID_PAY_CONFIRM = "buy_credit_androidpay_confirm"
    const val BUY_CREDIT_FINISH_CHECKOUT = "buy_credit_finish_checkout"

    /**
     * squcorner screen
     */
    const val SQUCORNER_OPEN_DETAIL_TIME = "squcorner_page_to_detail_timespent"
    const val SQUCORNER_OPEN_DETAIL = "squcorner_detail_page_onload"
    const val SQUCORNER_PARTICIPANT_JOIN_TIME = "squcorner_participant_join_timespent"
    const val SQUCORNER_PARTICIPANT_JOIN = "squcorner_participant_join"
    const val SQUCORNER_VIEWER_JOIN_TIME = "squcorner_viewer_join_timespent"
    const val SQUCORNER_VIEWER_JOIN = "squcorner_viewer_join"

    /**
     * video call screen
     */
    const val RTC_ENTER_CLASS = "enter_privateclass_rtc"
    const val RTC_EXIT_CLASS = "exit_privateclass_rtc"
    const val RTC_FULL_SCREEN = "fullscreen_privateclass_rtc"
    const val RTC_TURN_OFF_CAM = "turnoff_cam_privateclass_rtc"
    const val RTC_TURN_ON_CAM = "turnon_can_privateclass_rtc"
    const val RTC_TURN_OFF_MIC = "turnoff_mic_privateclass_rtc"
    const val RTC_TURN_ON_MIC = "turnon_mic_privateclass_rtc"
    const val RTC_CLASS_DURATION = "privateclass_timespent:"

    /**
     * corner screen
     */
    const val CORNER_CLASS_DURATION = "cornerclass_timespent:"
    const val CORNER_ENTER_CLASS = "enter_cornerclass"
    const val CORNER_EXIT_CLASS = "exit_cornerclass"
    const val CORNER_FULL_SCREEN = "fullscreen_cornerclass"
    
    /**
     * cakap live
     */
    const val NONACTIVE_STUDENT_TAP_ALL_CAKAP_LIVE = "nonactive_student_tap_view_all_cakap_live"
    const val ACTIVE_STUDENT_TAP_ALL_CAKAP_LIVE = "active_student_tap_view_all_cakap_live"
    const val NONACTIVE_STUDENT_TAP_TEACHER_DETAIL_CAKAP_LIVE = "nonactive_tap_teacher_detail_cakap_live"
    const val ACTIVE_STUDENT_TAP_TEACHER_DETAIL_CAKAP_LIVE = "active_student_tap_teacher_detail_cakap_live"
    
    fun NONACTIVE_STUDENT_TAP_AUDIO(course : String?, topic : String?) : String = "nonactive_student_tap_audio_${course?.replace("\\s".toRegex(), "_")}_${topic?.replace("\\s".toRegex(), "_")}"
    fun ACTIVE_STUDENT_TAP_AUDIO(course : String?, topic : String?) : String = "active_student_tap_audio_${course?.replace("\\s".toRegex(), "_")}_${topic?.replace("\\s".toRegex(), "_")}"
    fun NONACTIVE_STUDENT_SHARE(course : String, topic : String) : String = "nonactive_student_share_${course}_$topic"
    fun ACTIVE_STUDENT_SHARE(course : String, topic : String) : String = "active_student_share_${course}_$topic"
}