package Bubble;

import Bubble.Task.TextEntity;
import Bubble.Task.runTime;
import Bubble.Task.showMessage;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import com.sun.istack.internal.NotNull;

import java.util.LinkedHashMap;


/*
* 对话框
* */
public class Chat extends PluginBase implements Listener {

    public static LinkedHashMap<Player, String> chatMessage = new LinkedHashMap<>();
    public static LinkedHashMap<Player, Integer> loadTime = new LinkedHashMap<>();
    public static LinkedHashMap<Player, String> Tag = new LinkedHashMap<>();
    private static Chat chat;

    @Override
    public void onLoad() {
        Entity.registerEntity(TextEntity.name,TextEntity.class);
    }

    @Override
    public void onEnable() {
        chat = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getScheduler().scheduleDelayedTask(this,()->
                this.getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
                    public void onRun() {
                        Server.getInstance().getLogger().info("Task Start....");
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new showMessage(),2);
                        Server.getInstance().getScheduler().scheduleRepeatingTask(new runTime(),20);
                    }
                }), 40);
    }

    public static Chat getChat() {
        return chat;
    }

    /**
     * @param message 对话内容
     * @return 处理后的气泡
     */
    private static String chatMessage(@NotNull String message) {

        StringBuilder chatMessage = new StringBuilder("");
        int Length = (message.length() % 12) > 0 ? message.length() / 12 + 1 : message.length() / 12;
        StringBuilder line = new StringBuilder("§l╭─");
        for (int i = 0; i < 8; i++) {
            line.append("──");
        }
        int Line = getTrueLength(line.toString()) - 3;
        line.append("╮");
        StringBuilder lineEnd = new StringBuilder("");
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < Length; i++) {
            String split;
            if (message.length() < (i * 12 + 12)) {
                split = message.substring(i * 12, message.length());
            } else {
                split = message.substring(i * 12, i * 12 + 12);
            }
            int addK = (getTrueLength(split) - getEnglishLength(split)) / 2;
            int adds = getEnglishLength(split) + (addK * 2);
            String chinese = "[\u4e00-\u9fa5]";
            if (split.substring(0,1).matches(chinese)) {
                builder.append("§│§r §e");
            } else {
                builder.append("§l│§r   §e");
            }
            builder.append(split);
            for (int c = 0; c < Line - adds + 2; c++) {
                if (split.substring(split.length() - 1).matches(chinese) || addK > getEnglishLength(split)) {
                    builder.append(" ");
                } else {
                    builder.append(" ");
                }
            }
            builder.append("§r§l|").append("\n");
        }
        lineEnd.append("§r§l");
        for (int a = 0; a < 13; a++) {
            lineEnd.append("─");
        }
        lineEnd.append("╯");
        StringBuilder k = new StringBuilder(" ");
        for(int a = 0;a < 19;a++){
            k.append(" ");
        }
        String end = "§l\\  /";
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


    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        chatMessage.put(player,chatMessage(event.getMessage()));
        loadTime.put(player,3);
        if(!Tag.containsKey(player))
            Tag.put(player,player.getNameTag());
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        chatMessage.remove(player);
    }


}

