package com.global.travel.telecom.app.base;

public interface BaseView {

    void showToast(String message);
    void showProgressBar();
    void hideProgressBar();
    void onFailure();

    void onSuccess(String method, Object response );
    void onServerError(String method, String errorMessage);

}
