package com.mdq.interfaces;


import com.mdq.enums.MessageViewType;
import com.mdq.enums.ViewType;


public interface StateViewInterface {
        void ShowErrorMessage(MessageViewType messageViewType, String errorMessage);
        void ShowErrorMessage(MessageViewType messageViewType, ViewType viewType, String errorMessage);

    }

