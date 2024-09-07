package pl.supereasy.sectors.util.schematics;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchematicUtil {

    /*
    private static final Map<SchematicType, CuboidClipboard> schematics = new HashMap<>();
    private static final EditSession editSession = new EditSession(new BukkitWorld(Bukkit.getWorld("world")), 9999999);
    static {
        for (SchematicType type : SchematicType.values()) {
            final File f = new File(type.getFileName());
            if (f.exists()) {
                SchematicFormat sf = SchematicFormat.getFormat(f);
                try {
                    CuboidClipboard cc = sf.load(f);
                    schematics.put(type, cc);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean pasteSchematic(final SchematicType type, final Location location) {
        final CuboidClipboard clipboard = schematics.get(type);
        if (clipboard == null) {
            return false;
        }
        try {
            clipboard.paste(editSession, BukkitUtil.toVector(location), false);
        } catch (MaxChangedBlocksException e) {
            return false;
        }
        return true;
    }

     */
}
