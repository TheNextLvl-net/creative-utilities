package net.thenextlvl.utilities.api.view;

import net.thenextlvl.utilities.api.controller.FlyController;
import net.thenextlvl.utilities.api.controller.NoClipController;
import net.thenextlvl.utilities.api.controller.OpenableController;
import net.thenextlvl.utilities.api.model.NoClipManager;
import org.bukkit.plugin.Plugin;

public interface UtilitiesProvider extends Plugin {
    /**
     * Retrieves the FlyController instance.
     *
     * @return the FlyController instance
     */
    FlyController flyController();

    /**
     * Retrieves the NoClipController instance.
     *
     * @return the NoClipController instance
     */
    NoClipController noClipController();

    /**
     * Retrieves the NoClipManager instance.
     *
     * @return the NoClipManager instance
     */
    NoClipManager noClipManager();

    /**
     * Retrieves the OpenableController instance.
     *
     * @return the OpenableController instance
     */
    OpenableController openableController();
}
