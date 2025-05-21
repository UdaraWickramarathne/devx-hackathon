package com.zenvest.devx.constants;

public class ApiEndpoint {
    public static final String AUTHENTICATE_PATH = "/api/authenticate";

    // Public API endpoints
    public static final String AUTH = AUTHENTICATE_PATH + "/auth";
    public static final String AUTH_REGISTER = "/register";
    public static final String AUTH_LOGIN = "/login";
    public static final String AUTH_REFRESH = "/refresh";

    // Secured (Account) API endpoints
    public static final String ACCOUNT  = AUTHENTICATE_PATH + "/accounts";
    public static final String ACCOUNTS_RESOURCE_BY_ID = "/{id}";

    // Secured (Transaction) API endpoints
    public static final String TRANSACTION = ACCOUNT + "/{accountId}/transactions";
    public static final String TRANSACTION_DEPOSIT = "/deposit";
    public static final String TRANSACTION_WITHDRAW = "/withdraw";

    // Secured (Transfer) API endpoints
    public static final String TRANSFER = "/transfer";
    public static final String TRANSFER_HISTORY = "/history";

}
