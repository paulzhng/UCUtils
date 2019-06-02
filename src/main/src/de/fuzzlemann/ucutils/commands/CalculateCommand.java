package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
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

        String input = String.join("", args);
        Expression expr = new Expression(input);
        try {
            expr.evaluate();
        } catch (Expression.ExpressionException e) {
            TextUtils.error("Es ist ein Fehler bei der Evaluierung aufgetreten: " + e.getMessage());
            return true;
        }

        Message.builder()
                .prefix()
                .of(input).color(TextFormatting.BLUE).advance()
                .of(" = ").color(TextFormatting.GRAY).advance()
                .of(expr.parse()).color(TextFormatting.BLUE).advance()
                .send();
        return true;
    }
}
