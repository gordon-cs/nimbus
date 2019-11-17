package com.vmware.nimbus.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private String apiKeyError;
    private boolean isDataValid;

    LoginFormState(@Nullable String apiKeyError, int invalid_password) {
        this.apiKeyError = apiKeyError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.apiKeyError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    String getApiKeyError() {
        return apiKeyError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
