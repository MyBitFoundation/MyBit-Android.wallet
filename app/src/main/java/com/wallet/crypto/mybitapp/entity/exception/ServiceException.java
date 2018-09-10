package com.wallet.crypto.mybitapp.entity.exception;

import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;

public class ServiceException extends Exception {
	public final ErrorEnvelope error;

	public ServiceException(String message) {
		super(message);

		error = new ErrorEnvelope(message);
	}
}
