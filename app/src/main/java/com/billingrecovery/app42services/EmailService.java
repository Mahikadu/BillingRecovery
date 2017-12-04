package com.billingrecovery.app42services;

import android.content.Context;

import com.billingrecovery.libs.AsyncApp42ServiceApi;

public class EmailService {

    private AsyncApp42ServiceApi asyncService;

    public EmailService(Context context) {
        asyncService = AsyncApp42ServiceApi.instance(context);
    }

    public com.shephertz.app42.paas.sdk.android.email.EmailService getEmailService() {
        return asyncService.getEmailService();
    }
}