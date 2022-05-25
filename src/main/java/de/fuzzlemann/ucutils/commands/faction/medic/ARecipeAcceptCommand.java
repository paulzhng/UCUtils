package de.fuzzlemann.ucutils.commands.faction.medic;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * @author RettichLP
 */
@Mod.EventBusSubscriber
public class ARecipeAcceptCommand {

    private static final Pattern RECIPE_ACCEPT_PATTERN = Pattern.compile("^((?:\\[UC])*[a-zA-Z0-9_]+) möchte dir ein Rezept für 300\\$ verkaufen\\.$");
    private static Timer TIMER = new Timer();
    private static int recipeAcceptAmountLeft;
    private static long lastExecution;

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
    public static void onRecipeAcceptFeedback(ClientChatReceivedEvent e) {
        if (recipeAcceptAmountLeft < 1) return; //checks if there is an active recipe-accept-process

        String msg = e.getMessage().getUnformattedText();
        if (!RECIPE_ACCEPT_PATTERN.matcher(msg).find()) return;

        long timeSinceLastExecution = System.currentTimeMillis() - lastExecution;
        long delay = 0;

        if (timeSinceLastExecution < 500) delay = 500 - timeSinceLastExecution;

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                acceptRecipe();
            }
        }, delay);
    }

    private static void acceptRecipe() {
        --recipeAcceptAmountLeft;
        lastExecution = System.currentTimeMillis();
        AbstractionLayer.getPlayer().sendChatMessage("/annehmen");
    }
}