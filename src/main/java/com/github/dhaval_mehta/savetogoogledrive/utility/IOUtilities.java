package com.github.dhaval_mehta.savetogoogledrive.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtilities {
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        assert inputStream != null;

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        return sb.toString();
    }
}
