package Bubble.Task;


import Bubble.Chat;

import cn.nukkit.Player;
import cn.nukkit.Server;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityGhast;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.Task;

public class showMessage extends Task{
    static final  int TextEID = 0xedeca;
    @Override
    public void onRun(int i) {
        for (Player player: Server.getInstance().getOnlinePlayers().values()) {
            if (Chat.chatMessage.containsKey(player)) {
                if(Chat.loadTime.containsKey(player)){
                EntityGhast entity = new EntityGhast(player.chunk,Entity.getDefaultNBT(new Vector3(player.x,
                        player.y+player.getEyeHeight() + 1,player.z)));
                entity.yaw = player.yaw;
                entity.pitch = player.pitch;
                entity.spawnToAll();
               //     player.setNameTag(Chat.chatMessage.get(player)+"\n\n"+Chat.Tag.get(player));
                }else{
//                    Chat.chatMessage.remove(player);
//                    player.setNameTag(Chat.Tag.get(player));
//                    Chat.Tag.remove(player);
                }
            }
        }
    }
}

