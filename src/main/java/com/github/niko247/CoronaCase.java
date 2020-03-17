package com.github.niko247;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CoronaCase {
    @SerializedName("Województwo")
    private String voivodeship;
    @SerializedName("Powiat/Miasto")
    private String county;
    @SerializedName("Liczba")
    private int casesNumber;
    @SerializedName("Liczba zgonów")
    private String deathsNumber;

    @Override
    public String toString() {
        return "Województwo: " + voivodeship + " Powiat/Miasto: " + county + " Liczba przypadków:" + casesNumber +
                " Liczba zgonów:" + deathsNumber;
    }

    public int getDeathsNumberAsInt() {
        return deathsNumber.isBlank() ? 0 : Integer.parseInt(deathsNumber);
    }
}
