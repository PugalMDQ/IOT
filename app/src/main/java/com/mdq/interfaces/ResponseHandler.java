package com.mdq.interfaces;

import com.mdq.pojo.jsonresponse.ErrorBody;

public interface ResponseHandler<T> {
        void onSuccess(String message);

        void onSuccess(T item, String message);

        void onFailure(ErrorBody errorBody, int statusCode);

}


