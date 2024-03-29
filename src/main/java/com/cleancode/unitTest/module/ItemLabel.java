package com.cleancode.unitTest.module;

public enum ItemLabel {
    FRUIT, IT, MEET, FOOD, COMPUTER, MOUSE, KEYBOARD, CUP, UNKNOWN;

    public static ItemLabel fromString(String stringLabel) {
        try {
            return ItemLabel.valueOf(stringLabel.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ItemLabel.UNKNOWN;
        }
    }
}
