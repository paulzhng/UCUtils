package de.fuzzlemann.ucutils.commands.faction.medic;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * @author RettichLP
 */
@Mod.EventBusSubscriber public class ARecipeGiveCommand {

    private static final Pattern RECIPE_GIVE_PATTERN = Pattern.compile(
            "^Du hast ((?:\\[UC])*[a-zA-Z0-9_]+) ein Rezept für (Antibiotika|Hustensaft|Schmerzmittel) ausgestellt\\.$");
    private static Timer TIMER = new Timer();
    private static String target;
    private static String medication = null;
    private static int recipeGiveAmountLeft;
    private static long lastExecution;
    private static List<String> medications = Arrays.asList("Antibiotika", "Hustensaft", "Schmerzmittel");

    @Command(value = "arezept", usage = "/%label% [Spieler] [Antibiotika/a/Hustensaft/h/Schmerzmittel/s] [Menge]")
    public boolean onCommand(UPlayer p, String target, String medication, int recipeGiveAmount) {
        // if no permitted medication was used and the number of recipes is 0, return false
        if (recipeGiveAmount < 1) return false;

        if (medication.equalsIgnoreCase("antibiotika") || medication.equalsIgnoreCase("a")) ARecipeGiveCommand.medication = "Antibiotika";
        if (medication.equalsIgnoreCase("hustensaft") || medication.equalsIgnoreCase("h")) ARecipeGiveCommand.medication = "Hustensaft";
        if (medication.equalsIgnoreCase("schmerzmittel") || medication.equalsIgnoreCase("s")) ARecipeGiveCommand.medication = "Schmerzmittel";

        if (ARecipeGiveCommand.medication == null) return false;

        // the number of remaining recipes is reduced by 1 because a recipe is given directly to trigger the ClientChatReceivedEvent
        recipeGiveAmountLeft = recipeGiveAmount - 1;
        p.sendChatMessage("/rezept " + (ARecipeGiveCommand.target = target) + " " + (ARecipeGiveCommand.medication = medication));
        return true;
    }

    @SubscribeEvent public static void onRecipeGiveFeedback(ClientChatReceivedEvent e) throws InterruptedException {
        if (recipeGiveAmountLeft < 1)
            return; //checks if there is an active recipe-give-process

        String msg = e.getMessage().getUnformattedText();
        if (!RECIPE_GIVE_PATTERN.matcher(msg).find())
            return;

        long timeSinceLastExecution = System.currentTimeMillis() - lastExecution;
        long delay = 0;

        if (timeSinceLastExecution < 500)
            delay = 500 - timeSinceLastExecution;

        TIMER.schedule(new TimerTask() {
            @Override public void run() {
                giveRecipe();
            }
        }, delay);
    }

    private static void giveRecipe() {
        --recipeGiveAmountLeft;
        lastExecution = System.currentTimeMillis();
        AbstractionLayer.getPlayer().sendChatMessage("/rezept " + target + " " + medication);
    }
}