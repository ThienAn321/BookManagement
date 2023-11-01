package com.learn.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OTPUtil {

    public String generateOTP() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);

        while (output.length() < 6) {
            output = "0" + output;
        }

        // output : 001234
        return output;
    }

}
