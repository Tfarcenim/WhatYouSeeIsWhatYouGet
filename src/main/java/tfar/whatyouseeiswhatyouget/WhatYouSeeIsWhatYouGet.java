package tfar.whatyouseeiswhatyouget;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WhatYouSeeIsWhatYouGet.MODID)
public class WhatYouSeeIsWhatYouGet
{
    public static final String MODID = "whatyouseeiswhatyouget";

    public WhatYouSeeIsWhatYouGet() {
        MinecraftForge.EVENT_BUS.addListener(this::lookAndSee);
    }

    private static final Map<UUID, Tracker> track = new HashMap<>();

    static int TIME = 60;

    private void lookAndSee(TickEvent.PlayerTickEvent e) {
        if (!e.player.world.isRemote) {
            UUID uuid = e.player.getGameProfile().getId();
            Tracker tracker = track.computeIfAbsent(uuid, uuid1 -> new Tracker());
            tracker.update(e.player);
        }
    }
}
