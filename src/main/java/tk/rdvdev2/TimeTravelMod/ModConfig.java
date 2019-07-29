package tk.rdvdev2.TimeTravelMod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

    public static class Client {

        public final ForgeConfigSpec.BooleanValue enableTimeLineMusic;


        Client(ForgeConfigSpec.Builder builder) {

            builder.comment("Time Travel Mod client settings")
                    .push("client");

            enableTimeLineMusic = builder
                    .comment("When this is enabled the Time Lines have their own music")
                    .translation("config.timetravelmod.client.enabletimelinemusic")
                    .define("enableTimeLineMusic", true);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Common {

        public final ForgeConfigSpec.BooleanValue enableExperimentalFeatures;
        public final ForgeConfigSpec.BooleanValue enableUpdatePromos;
        public final ForgeConfigSpec.BooleanValue enableCheaterReports;

        Common(ForgeConfigSpec.Builder builder) {

            builder.comment("Time Travel Mod common settings")
                    .push("common");

            enableExperimentalFeatures = builder
                    .comment("When this is enabled experimental features of the mod are activated")
                    .translation("config.timetravelmod.common.enableexperimentalfeatrues")
                    .define("enableExperimentalFeatrues", false);

            enableUpdatePromos = builder
                    .comment("If this is disabled you won't be notiified of mod updates at world joining")
                    .translation("config.timetravelmod.common.enableupdatepromos")
                    .define("enableUpdatePromos", true);

            enableCheaterReports = builder
                    .comment("If this is disabled game masters (ops) won't be notified when a possible cheater is detected")
                    .translation("config.timetravelmod.common.enablecheaterreports")
                    .define("enableCheaterReports", true);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}