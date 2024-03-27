package com.mygdx.group17.shipocalypse.views;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountManager {
    private Map<String, String> accounts;

    public AccountManager() {
        this.accounts = new HashMap<>();
    }

    public String createAccount(String email, String password) {
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long";
        }

        if (accounts.containsKey(email)) {
            return "Account already exists with this email";
        }

        accounts.put(email, password);
        return "Account created successfully";
    }

    private boolean isValidEmail(String email) {
        // Basic email format validation using regular expression
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        // Simple password length validation
        return password.length() >= 6;
    }

    // Example usage
    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();

        // Simulating user input
        String email = "example@example.com";
        String password = "password123";

        String result = accountManager.createAccount(email, password);
        System.out.println(result);
    }
}
