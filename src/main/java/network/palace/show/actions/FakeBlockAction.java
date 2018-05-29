package network.palace.show.actions;

import network.palace.show.Show;
import network.palace.show.exceptions.ShowParseException;
import network.palace.show.handlers.BlockData;
import network.palace.show.utils.ShowUtil;
import network.palace.show.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Marc on 7/1/15
 */
@SuppressWarnings("deprecation")
public class FakeBlockAction extends ShowAction {
    private Location loc;
    private int id;
    private byte data;

    public FakeBlockAction(Show show, long time) {
        super(show, time);
    }

    @Override
    public void play() {
        for (UUID uuid : show.getNearPlayers()) {
            Player tp = Bukkit.getPlayer(uuid);
            if (tp == null) {
                continue;
            }
            tp.sendBlockChange(loc, id, data);
        }
    }

    @Override
    public ShowAction load(String line, String... args) throws ShowParseException {
        Location loc = WorldUtil.strToLoc(show.getWorld().getName() + "," + args[3]);
        if (loc == null) {
            throw new ShowParseException("Invalid Location " + line);
        }
        try {
            BlockData data = ShowUtil.getBlockData(args[2]);
            this.loc = loc;
            this.id = data.getId();
            this.data = data.getData();
        } catch (ShowParseException e) {
            throw new ShowParseException(e.getReason());
        }
        return this;
    }
}