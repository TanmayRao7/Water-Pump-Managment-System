package com.tanmay.example;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PumpData {
    @SerializedName("id")
    private int id;

    @SerializedName("formatted_time")
    private String time;

    @SerializedName("pump_status")
    private String pumpStatus;

    @SerializedName("water_level")
    private int waterLevel;

    @SerializedName("energy_level")
    private int energyLevel;

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getPumpStatus() {
        return pumpStatus;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public PumpData(String pumpStatus, int waterLevel, int energyLevel) {
        this.pumpStatus = pumpStatus;
        this.waterLevel = waterLevel;
        this.energyLevel = energyLevel;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nLast Updated Time: " + time + "\n\n" + pumpStatus.toUpperCase() + "\n\n" +
                "Water Level: " + waterLevel + "\n\n" +
                "Energy Level: " + energyLevel+ "\n";
    }


}
