package Bubble.Task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class TextEntity extends Entity {

    public static String name = "TextEntity";
    @Override
    public int getNetworkId () {
        return showMessage.TextEID;
    }

    public TextEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    protected void initEntity(){
        super.initEntity();
        this.setMaxHealth(1);
        this.setHealth(1);
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
    }

    @Override
    public void spawnToAll() {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = NETWORK_ID;
        pk.entityRuntimeId = showMessage.TextEID;
        pk.x = (float) this.x;
        pk.y = (float)this.y;
        pk.z = (float)this.z;
        pk.speedX = (float)this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        for(Player player: Server.getInstance().getOnlinePlayers().values()){
            player.dataPacket(pk);
            super.spawnTo(player);
        }

    }
}
