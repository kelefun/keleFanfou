package com.zua.kelefun.exception;

/**
 * @author liukaiyang
 * @since 2017/2/7 17:53
 */

public class OAuthException extends RuntimeException {

    /**
     * Default constructor
     *
     * @param message
     *            message explaining what went wrong
     * @param e
     *            original exception
     */
    public OAuthException(String message, Exception e) {
        super(message, e);
    }

    /**
     * No-exception constructor. Used when there is no original exception
     *
     * @param message
     *            message explaining what went wrong
     */
    public OAuthException(String message) {
        super(message, null);
    }

}
