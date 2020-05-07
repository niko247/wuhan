package com.github.niko247;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CoronaCase {
    @SerializedName("Województwo")
    private String voivodeship;
    @SerializedName("Liczba")
    private String casesNumber;
    @SerializedName("Liczba zgonów")
    private String deathsNumber;

    @Override
    public String toString() {
        return "Województwo: " + voivodeship + " Liczba przypadków:" + casesNumber +
                " Liczba zgonów:" + deathsNumber;
    }

    public int getDeathsNumberAsInt() {
        return parseIntFromStringDefaultZero(deathsNumber);
    }

    public int getCasesNumberAsInt() {
        return parseIntFromStringDefaultZero(casesNumber);
    }

    private int parseIntFromStringDefaultZero(String value) {
        return value == null || value.isBlank() ? 0 : Integer.parseInt(removeSpaces(value));
    }

    private String removeSpaces(String value) {
        return value.replace(" ", "");
    }
}
