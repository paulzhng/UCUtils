package de.fuzzlemann.ucutils.commands.faction.medic;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author RettichLP
 */
@Mod.EventBusSubscriber
public class ARecipeAcceptCommand {

    private static final Pattern RECIPE_ACCEPT_PATTERN = Pattern.compile("^((?:\\[UC])*[a-zA-Z0-9_]+) möchte dir ein Rezept für 300\\$ verkaufen\\.$");
    private static int recipeAcceptAmountLeft;

    @Command(value = { "arezeptannehmen", "arannehmen" }, usage = "/arezeptannehmen [Menge]")
    public boolean onCommand(UPlayer p, int recipeAcceptAmount) {

        // if the number of recipes is 0, return false
        if (recipeAcceptAmount < 1) return false;

        // the number of remaining recipes is reduced by 1 because a recipe is accepted directly to trigger the ClientChatReceivedEvent
        ARecipeAcceptCommand.recipeAcceptAmountLeft = recipeAcceptAmount - 1;
        p.sendChatMessage("/annehmen");
        return true;
    }

    @SubscribeEvent
    public static void onRecipeAcceptFeedback(ClientChatReceivedEvent e) throws InterruptedException {

        if (recipeAcceptAmountLeft < 1) return; //checks if there is an active recipe-accept-process

        String msg = e.getMessage().getUnformattedText();
        if (RECIPE_ACCEPT_PATTERN.matcher(msg).find()) {
            TimeUnit.MILLISECONDS.sleep(1000); // wait because if the medic uses '/arezept' there is a spam error
            AbstractionLayer.getPlayer().sendChatMessage("/annehmen");
            recipeAcceptAmountLeft--;
        } else if (msg.contains("Dir wird nichts angeboten.") /* termination conditions */ ) {
            recipeAcceptAmountLeft = 0;
        }
    }
}