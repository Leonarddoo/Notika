import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommandsHandler extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        //On récupère l'objet de la commande réalisé
        CustomCommands cmd = CustomCommands.retrieveCommandByName(event.getName());
        //Si aucune commande ne correspond on ne fait rien
        if(cmd == null) return;

        //On vérifie si le membre a la permission de faire cela
        if(!cmd.getAcceptedId().contains(event.getUser().getId())){
            //Si ce n'est pas le cas on envoie un message d'erreur
            event.reply("Désolé, vous n'avez pas la permission de faire cela.").setEphemeral(true).queue();
            return;
        }

        try {
            //On récupère le channel où on envoie la commande et on envoie un embed
            System.out.println(cmd.getImage());
            event.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setAuthor(cmd.getAuthor())
                    .setTitle(cmd.getTitle())
                    .setDescription(cmd.getDescription())
                    .setImage(cmd.getImage())
                    .setColor(new Color(cmd.getRed(), cmd.getGreen(), cmd.getBlue()))
                    //On rajoute le bouton d'achat
                    .build()).setActionRow(Button.primary("buy", "\uD83D\uDCB5 Acheter " + cmd.getPrice() + " €")).queue();
            //On envoie un message bidon
            event.reply("Vos désirs sont des ordres.").setEphemeral(true).queue();
        }catch (IllegalArgumentException e){
            //Message d'erreur
            event.reply("Le lien de l'image ne doit pas être correcte.").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        //On vérifie que l'id du bouton est bien 'buy'
        if(event.getButton().getId().equals("buy")) {


            boolean find = false;
            //On parcourt tous les textChannels
            for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                //On vérifie qu'aucun channel ne porte déjà le nom du membre
                if (textChannel.getName().equals(event.getMember().getEffectiveName().toLowerCase(Locale.ROOT))) {
                    find = true;
                    break;
                }
            }
            //Si aucun channel ne porte le nom du membre
            if (!find) {
                //On crée un nouveau textChannel
                event.getGuild()
                        .createTextChannel(event.getMember().getEffectiveName(), event.getGuild().getCategoryById("990964567368663110")).syncPermissionOverrides()
                        //On synchronise les permissions avec la catégorie
                        .syncPermissionOverrides()
                        //On ajoute les permissions à la personne ayant demandé l'achat
                        .addMemberPermissionOverride(event.getMember().getIdLong(), Permission.VIEW_CHANNEL.getRawValue(), Permission.MANAGE_CHANNEL.getRawValue())
                        .queue(channel -> {
                            //On lui répond que le channel vient d'être créé
                            event.reply("Un channel vient d'être créer : " + channel.getAsMention()).setEphemeral(true).queue();
                            //On envoie un message pour valider ou refuser le fait qu'il veuille acheter cet élément
                            channel.sendMessageEmbeds(new EmbedBuilder()
                                    .setColor( new Color(51,156,255))
                                    .setDescription("**DevOceãn** vous remercie pour votre demande d'achat.")
                                    .addField("Vous souhaitez acheter notre pack suivant :", event.getMessage().getEmbeds().get(0).getTitle(), true)
                                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                    .setImage(event.getMessage().getEmbeds().get(0).getImage().getUrl())
                                    .build()).setActionRow(
                                    Button.success("accept", "✅ Valider"),
                                    Button.danger("deny", "❌ Refuser")
                            ).queue();
                        });
            //Si le membre a déjà un salon ouvert à son nom
            }else{
                event.reply("Désolé mais vous avez déjà une commande en cours.").setEphemeral(true).queue();
            }

        //Si le bouton accepter est pressé
        }else if(event.getButton().getId().equals("accept")){
            //On renvoie un message de confirmation
            event.reply("Très bien ! Un <@&990683386039394335> a été prévenu pour qu'il prenne en compte votre demande.").queue();

        //Si le bouton refuser est pressé
        }else if(event.getButton().getId().equals("deny")){
            //On renvoie un message de confirmation
            event.reply("Dommage. Vous vous êtes désister ! Ce salon va se supprimer d'ici quelques secondes.").queue();
            //On ferme le salon au bout de 5 secondes
            event.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }
}
