package net.thenextlvl.utilities.model;

import com.google.gson.annotations.SerializedName;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PluginConfig(
        @SerializedName("disable-explosions") boolean disableExplosions,
        @SerializedName("disable-fading") boolean disableFading,
        @SerializedName("disable-gravity") boolean disableGravity,
        @SerializedName("disable-leaves-decay") boolean disableLeavesDecay,
        @SerializedName("disable-physics") boolean disablePhysics,
        @SerializedName("disable-redstone") boolean disableRedstone,
        @SerializedName("disable-soil-trample") boolean disableSoilTrample,
        @SerializedName("disable-weather-changes") boolean disableWeatherChanges,
        @SerializedName("enable-piston-sliming") boolean pistonSliming,
        @SerializedName("fix-attack-speed") boolean fixAttackSpeed,
        @SerializedName("prevent-dragon-egg-teleport") boolean preventDragonEggTeleport
) {
}
