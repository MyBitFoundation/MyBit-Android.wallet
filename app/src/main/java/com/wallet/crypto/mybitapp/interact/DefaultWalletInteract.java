package com.wallet.crypto.mybitapp.interact;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.TransactionRepositoryType;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class DefaultWalletInteract {

	private final SessionRepositoryType sessionRepository;
	private final TransactionRepositoryType transactionRepository;

	public DefaultWalletInteract(SessionRepositoryType sessionRepositoryType, TransactionRepositoryType transactionRepositoryType) {
		this.sessionRepository = sessionRepositoryType;
		this.transactionRepository = transactionRepositoryType;
	}

	public Completable set(Wallet wallet) {
		return this.transactionRepository.clearPending().andThen(this.sessionRepository.setWallet(wallet));
	}

	public Completable clear() {
		return this.transactionRepository.clearPending().andThen(this.sessionRepository.clearDefaultWallet());
	}
}
