package com.wallet.crypto.mybitapp.service;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.wallet.crypto.mybitapp.entity.Balance;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.TokenInfo;
import com.wallet.crypto.mybitapp.entity.exception.ApiErrorException;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;

import java.math.BigDecimal;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.wallet.crypto.mybitapp.C.ErrorCode.UNKNOWN;

public class EthplorerTokenService implements TokenExplorerClientType {

    private EthplorerApiClient ethplorerApiClient;
    private final OkHttpClient httpClient;
    private final Gson gson;

    public EthplorerTokenService(
            OkHttpClient httpClient,
            Gson gson,
            SessionRepositoryType sessionRepository) {

        this.httpClient = httpClient;
        this.gson = gson;
        buildApiClient(sessionRepository.getNetwork().backendUrl);
    }

    private void buildApiClient(String baseUrl) {
        ethplorerApiClient = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(EthplorerApiClient.class);
    }

    @Override
    public Observable<Token[]> fetch(String walletAddress) {
        return ethplorerApiClient.fetchTokens(walletAddress)
                .lift(apiError())
                .map(r -> {
                    if (r.tokens == null) {
                        return new Token[0];
                    } else {
                        int len = r.tokens.length;
                        Token[] tokens = new Token[len];
                        for (int i = 0; i < len; ++i) {
                            RemoteToken remoteToken = r.tokens[i];
                            tokens[i] = new Token(remoteToken.tokenInfo, new Balance(remoteToken.balance, BigDecimal.ZERO, BigDecimal.ZERO));
                        }
                        return tokens;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private static @NonNull
    ApiErrorOperator apiError() {
        return new ApiErrorOperator();
    }

    public interface EthplorerApiClient {
        @GET("/tokens")
        Observable<Response<EthplorerResponse>> fetchTokens(@Query("address") String address);
    }

    private static class RemoteToken {
        @SerializedName("contract")
        public TokenInfo tokenInfo;

        @SerializedName("balance")
        public BigDecimal balance;
    }

    private static class EthplorerResponse {
        @SerializedName("docs")
        RemoteToken[] tokens;
        EthplorerError error;
    }

    private static class EthplorerError {
        int code;
        String message;
    }

    private final static class ApiErrorOperator implements ObservableOperator<EthplorerResponse, Response<EthplorerResponse>> {

        @Override
        public Observer<? super Response<EthplorerResponse>> apply(Observer<? super EthplorerResponse> observer) throws Exception {
            return new DisposableObserver<Response<EthplorerResponse>>() {
                @Override
                public void onNext(Response<EthplorerResponse> response) {
                    EthplorerResponse body = response.body();
                    if (body != null && body.error == null) {
                        observer.onNext(body);
                        observer.onComplete();
                    } else {
                        if (body != null) {
                            observer.onError(new ApiErrorException(new ErrorEnvelope(body.error.code, body.error.message)));
                        } else {
                            observer.onError(new ApiErrorException(new ErrorEnvelope(UNKNOWN, "Service not available")));
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }

                @Override
                public void onComplete() {
                    observer.onComplete();
                }
            };
        }
    }
}
