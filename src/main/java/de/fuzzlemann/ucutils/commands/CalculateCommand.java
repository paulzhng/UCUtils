package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CalculateCommand {

    @Command(value = {"calc", "calculate", "rechner"}, usage = "/%label% (Expression)")
    public boolean onCommand(@CommandParam(joinStart = true, joiner = "") String input) {
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
