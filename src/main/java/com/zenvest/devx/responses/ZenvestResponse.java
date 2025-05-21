package com.zenvest.devx.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * ZenvestResponse is a generic class that represents the response structure for API endpoints.
 * It contains a status, a list of results, and a message.
 *
 * @param <T> the type of the results
 */
@Getter
public class ZenvestResponse<T> {
    private static final String SUCCESS = "successful";
    private static final String UNSUCCESSFUL = "unsuccessful";

    private final String status;

    @Setter
    private List<T> results;

    @Setter
    private String message;

    /**
     * Default constructor, initializes with a "successful" status and an empty results list.
     */
    public ZenvestResponse() {
        this.status = SUCCESS;
        this.results = new ArrayList<>();
        this.message = "Operation successful";
    }

    /**
     * Constructor to initialize the response with a success status, results, and a message.
     *
     * @param success indicates if the operation was successful
     * @param results the list of results
     * @param message the message to be set
     */
    public ZenvestResponse(boolean success, List<T> results, String message) {
        this.status = success ? SUCCESS : UNSUCCESSFUL;
        this.results = results != null ? results : new ArrayList<>();
        this.message = message;
    }

    /**
     * Constructor to initialize the response with a success status and a single result.
     *
     * @param data the single result to be added
     */
    public ZenvestResponse(T data) {
        this();
        addData(data);
    }

    /**
     * Constructor to initialize the response with a success status and a list of results.
     *
     * @param results the list of results to be added
     */
    private void addData(T data) {
        if(data != null) {
            results.add(data);
        }
    }
}
