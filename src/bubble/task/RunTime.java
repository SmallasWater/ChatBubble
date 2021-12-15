package bubble.task;


import bubble.Chat;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;

/**
 * @author 若水
 */
public class RunTime extends PluginTask<Chat> {
    public RunTime(Chat owner) {
        super(owner);
    }

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
