package com.wallet.crypto.mybitapp.service;

import com.google.gson.Gson;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class BlockExplorerClient implements BlockExplorerClientType {

    private final OkHttpClient httpClient;
    private final Gson gson;

    private EtherScanApiClient etherScanApiClient;

    public BlockExplorerClient(
            OkHttpClient httpClient,
            Gson gson,
            SessionRepositoryType sessionRepository) {
        this.httpClient = httpClient;
        this.gson = gson;
        buildApiClient(sessionRepository.getNetwork().backendUrl);
    }

    private void buildApiClient(String baseUrl) {
        etherScanApiClient = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(EtherScanApiClient.class);
    }

    @Override
    public Observable<Transaction[]> fetchTransactions(Session session, int page) {
        return etherScanApiClient
                .fetchTransactions(session.wallet.address, page)
                .lift(apiError(gson))
                .map(t -> t.docs);
    }

    private static @NonNull
    <T> ApiErrorOperator<T> apiError(Gson gson) {
        return new ApiErrorOperator<>(gson);
    }

    private interface EtherScanApiClient {
        @GET("/transactions?limit=25")
        Observable<Response<EtherScanResponse>> fetchTransactions(@Query("address") String address, @Query("page") int page);
    }

    private final static class EtherScanResponse {
        Transaction[] docs;
    }

    private final static class ApiErrorOperator<T> implements ObservableOperator<T, Response<T>> {

        private final Gson gson;

        public ApiErrorOperator(Gson gson) {
            this.gson = gson;
        }

        @Override
        public Observer<? super retrofit2.Response<T>> apply(Observer<? super T> observer) throws Exception {
            return new DisposableObserver<Response<T>>() {
                @Override
                public void onNext(Response<T> response) {
                    observer.onNext(response.body());
                    observer.onComplete();
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
