package com.tanmay.example;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

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

    public String getData(){
        return "Time: "+time+" Pump Status: "+pumpStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nLast Updated Time: " + time + "\n\n" + pumpStatus.toUpperCase() + "\n\n" +
                "Water Level: " + waterLevel + "\n\n" +
                "Energy Level: " + energyLevel+ "\n";
    }


}
