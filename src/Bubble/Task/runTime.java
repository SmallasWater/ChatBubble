package Bubble.Task;


import Bubble.Chat;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class runTime extends Task{
    @Override
    public void onRun(int i) {
        for (Player player: Server.getInstance().getOnlinePlayers().values()) {
            if(Chat.getChat().loadTime.containsKey(player)){
                if(Chat.getChat().loadTime.get(player) > 0) {
                    Chat.getChat().loadTime.put(player,Chat.getChat().loadTime.get(player) - 1);
                } else {
                    Chat.getChat().loadTime.remove(player);
                    TextEntity entity = Chat.getChat().text.get(player);
                    entity.kill();
                    Chat.getChat().text.remove(player);
                }
            }
        }
    }
}
