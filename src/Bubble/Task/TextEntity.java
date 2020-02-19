package Bubble.Task;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;


public class TextEntity extends Entity {

    public static String name = "TextEntity";

    private Player player;

    private String text;


    @Override
    public int getNetworkId() {
        return 64;
    }


    public TextEntity(FullChunk chunk, CompoundTag nbt,Player player,String text) {
        super(chunk, nbt);
        this.player = player;
        this.y = player.y + player.getEyeHeight() + 0.25;
        this.text= text;
    }

    @Override
    protected void initEntity(){
        super.initEntity();
        this.setMaxHealth(1);
        this.setHealth(1);
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.setImmobile();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if(player.isOnline()){
            this.x = player.x;
            this.y = player.y;
            this.z = player.z;
            this.addMotion(this.player.motionX, this.player.motionY, this.player.motionZ);
            this.updateMovement();
            this.setPositionAndRotation(new Vector3(this.player.x, this.player.y, this.player.z),
                    this.player.yaw, this.player.pitch);
            this.updateMovement();
            if(!this.level.getFolderName().equals(player.getLevel().getFolderName())){
                this.kill();
            }
            setNameTag(text);
        }else{
            this.kill();
        }
        return super.onUpdate(currentTick);
    }

    public void setText(String text) {
        this.text = text;
    }

}
