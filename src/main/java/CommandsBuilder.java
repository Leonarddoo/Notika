import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CommandsBuilder extends ListenerAdapter {


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        //Au lancement, on supprime toutes les commandes
        event.getGuild().updateCommands()
                //On rajoute la commande par défaut de setup
                .addCommands(Commands.slash("setup","Mise en service de toutes les commandes."))
            .queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        //Si la commande est setup
        if(event.getName().equals("setup")){
            try {
                //On initialize notre liste de commande
                CustomCommands.initializeCommandsList("config.tld");
            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new RuntimeException(e);
            }
            //On récupère l'objet contenant les slashCommands de la guilde
            CommandListUpdateAction commands = event.getGuild().updateCommands();
            //On rajoute la commande de setup de base
            commands.addCommands(Commands.slash("setup","Mise en service de toutes les commandes."));
            //Pour chaque commande du fichier de config
            for(CustomCommands cmd : CustomCommands.getCommandsList()){
                //On ajoute la commande
                commands.addCommands(Commands.slash(cmd.getName(), "Embed de la commande : " + cmd.getName()));
            }
            commands.queue();
            //On renvoie un message d'erreur
            event.reply("Les commandes ont été mise à jour").queue();
        }
    }
}
