package net.thenextlvl.utilities.model;

import java.util.Set;

public final class Commands {
    public final Command advancedFly = new Command("advanced-fly", "advancedfly", "advfly", "fly");
    public final Command banner = new Command("banner", "bm");
    public final Command color = new Command("armor-color", "armorcolor", "color");
    public final Command nightVision = new Command("night-vision", "nightvision", "nv", "n");
    public final Command noClip = new Command("no-clip", "noclip", "nc");
    public final Command pottery = new Command("pottery-designer", "pottery", "pd");
    public final Command utils = new Command("utils", "butil", "bu");

    public final Command convex = new Command("/convex", "/con");
    public final Command cuboid = new Command("/cuboid", "/cub");
    public final Command deformRotate = new Command("/derot");
    public final Command scale = new Command("/scale");
    public final Command twist = new Command("/twist");

    public record Command(String name, Set<String> aliases) {
        private Command(final String name, final String... aliases) {
            this(name, Set.of(aliases));
        }
    }
}
