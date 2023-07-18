package com.hilltop.reservation.enums;

import lombok.Getter;

/**
 * Success messages.
 */
@Getter
public enum SuccessMessage {

    SUCCESSFULLY_ADDED("Successfully added.");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
