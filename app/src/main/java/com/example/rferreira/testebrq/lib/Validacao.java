package com.example.rferreira.testebrq.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rferreira on 17/05/2018.
 */

public class Validacao {



    public static boolean isEmail(String email)
    {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }


    public static boolean isTelefone(String numeroTelefone) {
        return numeroTelefone.matches("^\\(?\\d{2}\\)?[\\s-]?\\d{4}-?\\d{4}$") || numeroTelefone.matches("^\\(?\\d{2}\\)?[\\s-]?\\d{5}-?\\d{4}$") ;
    }





}

