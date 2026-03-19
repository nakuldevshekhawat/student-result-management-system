package com.srms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Utils {

    /** SHA-256 hash a plain-text password. */
    public static String hashPassword(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plain.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /** Print a horizontal divider line. */
    public static void printLine(int width) {
        System.out.println("─".repeat(width));
    }

    public static void printLine() { printLine(65); }

    /** Center-print a title banner. */
    public static void printBanner(String title) {
        int width = 65;
        System.out.println();
        printLine(width);
        int pad = (width - title.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, pad)) + title);
        printLine(width);
    }

    /** Print a simple table header row. */
    public static void printTableHeader(String... cols) {
        StringBuilder sb = new StringBuilder();
        for (String c : cols) sb.append(String.format("%-" + (65 / cols.length) + "s", c));
        System.out.println(sb);
        printLine();
    }

    /** Format double to 2 decimal places. */
    public static String fmt(double d) { return String.format("%.2f", d); }

    /** Calculate simple average of a list of doubles. */
    public static double average(List<Double> values) {
        return values.isEmpty() ? 0.0 : values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
