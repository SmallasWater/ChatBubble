package Bubble.Task;


import Bubble.Chat;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class runTime extends Task{
    @Override
    public void onRun(int i) {
        for (Player player: Server.getInstance().getOnlinePlayers().values()) {
            if(Chat.loadTime.containsKey(player)){
                if(Chat.loadTime.get(player) > 0)
                    Chat.loadTime.put(player,Chat.loadTime.get(player) - 1);
                else
                    Chat.loadTime.remove(player);
            }
        }
    }
}
