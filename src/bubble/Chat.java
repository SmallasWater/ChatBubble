package bubble;

import bubble.task.TextEntity;
import bubble.task.RunTime;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import com.sun.istack.internal.NotNull;

import java.util.LinkedHashMap;


/**
* 对话框
 * @author 若水
* */
public class Chat extends PluginBase implements Listener {



    public LinkedHashMap<Player, TextEntity> text = new LinkedHashMap<>();
    public LinkedHashMap<Player, Integer> loadTime = new LinkedHashMap<>();
    private static Chat chat;
    private static final int TEXT_SIZE = 11;
    private static final int LINE_SIZE = 13;
    private static final int DEFAULT_SIZE = 8;
    private static final int DEFAULT_SIZE1 = 19;
    private static final int CHINESE_TEXT_SIZE = 2;

    @Override
    public void onLoad() {
        Entity.registerEntity(TextEntity.name,TextEntity.class);
    }

    @Override
    public void onEnable() {
        chat = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        Server.getInstance().getLogger().info("Task Start....");
        Server.getInstance().getScheduler().scheduleRepeatingTask(new RunTime(this),20);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new PluginTask<Chat>(this) {
            @Override
            public void onRun(int i) {
                for(Level level:Server.getInstance().getLevels().values()){
                    for(Entity entity:level.getEntities()){
                        if(entity instanceof TextEntity){
                            Player player = ((TextEntity) entity).getPlayer();
                            if(player != null){
                                if(!text.containsKey(player)){
                                    entity.kill();
                                }
                            }
                        }
                    }
                }
            }
        }, 20);
    }

    public static Chat getChat() {
        return chat;
    }

    /**
     * @param message 对话内容
     * @return 处理后的气泡
     */
    private static String chatMessage(@NotNull String message) {

        StringBuilder chatMessage = new StringBuilder();
        int length = (message.length() % TEXT_SIZE) > 0 ? message.length() / TEXT_SIZE + 1 : message.length() / TEXT_SIZE;
        StringBuilder line = new StringBuilder("§l╭─");
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            line.append("──");
        }
        int l = getTrueLength(line.toString()) - 3;
        line.append("╮");
        StringBuilder lineEnd = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String split;
            if (message.length() < (i * 11 + 11)) {
                split = message.substring(i * 11);
            } else {
                split = message.substring(i * 11, i * 11 + 11);
            }
            int addK = (getTrueLength(split) - getEnglishLength(split)) / 2;
            int adds = getEnglishLength(split) + (addK * 2);
            String chinese = "[\u4e00-\u9fa5]";
            if (split.substring(0,1).matches(chinese)) {
                builder.append("§r│§r §e");
            } else {
                builder.append("§r│§r   §e");
            }
            builder.append(split);
            for (int c = 0; c < l - adds + CHINESE_TEXT_SIZE; c++) {
                if (split.substring(split.length() - 1).matches(chinese) || addK > getEnglishLength(split)) {
                    builder.append(" ");
                } else {
                    builder.append(" ");
                }
            }
            builder.append("§r|").append("\n");
        }
        lineEnd.append("§r§l");
        for (int a = 0; a < LINE_SIZE; a++) {
            lineEnd.append("─");
        }
        lineEnd.append("╯");
        StringBuilder k = new StringBuilder(" ");
        for(int a = 0;a < DEFAULT_SIZE1;a++){
            k.append(" ");
        }
        String end = "§l\\    /";
        chatMessage.append(line).append("\n").append(builder).append(end).append(lineEnd).append("\n\\/").append(k).append("\n");

        return chatMessage.toString();
    }
    private static int getTrueLength(String str) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private static int getEnglishLength(String str) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (!temp.matches(chinese)) {
                valueLength++;
            }
        }
        return valueLength;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        loadTime.put(player,3);
        if(!text.containsKey(player)){
            TextEntity entity = new TextEntity(player.getChunk(),Entity.getDefaultNBT(player),player,
                    chatMessage(event.getMessage())){
                @Override
                public float getWidth() {
                    return player.getWidth();
                }

                @Override
                public float getHeight() {
                    return player.getHeight();
                }
            };
            text.put(player,entity);
            entity.spawnToAll();
        }else{
            TextEntity entity = text.get(player);
            entity.setText( chatMessage(event.getMessage()));
        }

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Chat.getChat().loadTime.remove(player);
        if(Chat.getChat().text.containsKey(player)){
            TextEntity entity = Chat.getChat().text.get(player);
            entity.kill();
            Chat.getChat().text.remove(player);
        }
    }
}

