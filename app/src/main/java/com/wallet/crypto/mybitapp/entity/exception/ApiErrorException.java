package com.wallet.crypto.mybitapp.entity.exception;

import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;

public class ApiErrorException extends Exception {
    private final ErrorEnvelope errorEnvelope;

    public ApiErrorException(ErrorEnvelope errorEnvelope) {
        super(errorEnvelope.message);

        this.errorEnvelope = errorEnvelope;
    }
}
