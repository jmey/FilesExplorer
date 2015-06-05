package com.example.filesexplorer.Enum;

import java.io.File;

public enum SortCriterion {
    FILENAME, SIZE, DATE;

    private static SortCriterion[] list = values();

    public static SortCriterion get(int index) {
        if (index >= 0 && index < list.length) {
            return list[index];
        } else {
            return list[0];
        }
    }
}
