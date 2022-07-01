import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {

    private final static String TOKEN = "OTkwNzAyMzAxNzI5NjY5MTQx.GHAS3e.-Za_BLOAsOsF4zifOmWexZDOPCqJZADZFUoElI";

    public static void main(String[] args) {
        try {
            JDABuilder.createDefault(TOKEN)
                    .addEventListeners(new CommandsBuilder(),
                            new CommandsHandler())
                    .build();
        }catch (LoginException e){
            System.out.println("Un problème est survenue lors de la connexion avec le Token du BOT.");
        }
    }

}
