package cs211.project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatList {
    private ArrayList<Chat> chats;
    public ChatList(){
        chats = new ArrayList<>();
    }
    public void addNewChat(String userId, String teamId, String chat, LocalDateTime sentDateTimeText) {
        chats.add(new Chat(userId, teamId, chat, sentDateTimeText));
    }

    public void addNewChat(Chat chat){
        chats.add(chat);
    }

    public ChatList findTeamChatByTeamId(String teamId){
        ChatList chatList = new ChatList();
            for (Chat chat : chats){
                if (chat.isTeamId(teamId)){
                    chatList.addNewChat(chat);
                }
            }
        return chatList;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

}
