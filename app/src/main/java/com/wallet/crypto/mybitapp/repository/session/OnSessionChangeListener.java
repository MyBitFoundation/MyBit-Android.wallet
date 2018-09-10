package com.wallet.crypto.mybitapp.repository.session;

import com.wallet.crypto.mybitapp.entity.Session;

/**
 * Created by Dasha on 20.06.2018
 */
public interface OnSessionChangeListener {
    void onSessionChanged(Session session);
}
