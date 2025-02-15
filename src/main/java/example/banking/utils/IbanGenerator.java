package example.banking.utils;

public class IbanGenerator {

    public static String Generate(String country) {
        if (country.length() != 2) {
            throw new IllegalArgumentException("Country code must be 2 characters long.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(country);
        int remainingLength = 34 - sb.length();

        for (int i = 0; i < remainingLength; i++) {
            sb.append((int)(Math.random() * 10)); // Append a random digit (0-9)
        }

        return sb.toString();
    }
}
