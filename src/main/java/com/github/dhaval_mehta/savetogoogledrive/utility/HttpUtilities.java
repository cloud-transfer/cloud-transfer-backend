package com.github.dhaval_mehta.savetogoogledrive.utility;

public class HttpUtilities {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    public static boolean success(int httpStatusCode) {
        return httpStatusCode % 2 == 0;
    }
}
