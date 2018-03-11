package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CalculateCommand implements CommandExecutor {

    @Override
    @Command(labels = {"calc", "calculate", "rechner"}, usage = "/%label% (Expression)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        Expression expr = new Expression(String.join("", args));
        try {
            expr.evaluate();
        } catch (Expression.ExpressionException e) {
            TextUtils.error("Es ist ein Fehler bei der Evaluierung aufgetreten: " + e.getMessage());
            return true;
        }

        TextComponentString textBegin = new TextComponentString("Das Resultat ist: ");
        textBegin.getStyle().setColor(TextFormatting.AQUA);

        TextComponentString resultComponent = new TextComponentString(expr.parse());
        resultComponent.getStyle().setColor(TextFormatting.RED);

        p.sendMessage(textBegin.appendSibling(resultComponent));
        return true;
    }
}
