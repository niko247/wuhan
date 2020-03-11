package com.github.niko24;

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

    @Override
    public String toString() {
        return "Województwo: " + voivodeship + " Powiat: " + county + " Liczba przypadków" + casesNumber;
    }
}
