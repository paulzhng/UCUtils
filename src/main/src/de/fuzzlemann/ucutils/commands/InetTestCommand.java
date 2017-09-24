package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import lombok.SneakyThrows;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class InetTestCommand implements CommandExecutor {
    private final List<String> HOSTS = Arrays.asList("server.unicacity.de", "www.unicacity.de", "ts.unicacity.de", "www.google.de");

    @Override
    @Command(labels = "inettest")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        new Thread(() -> {
            TextComponentString textBegin = new TextComponentString("Ping zu ");
            textBegin.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString textMid = new TextComponentString(": ");
            textMid.getStyle().setColor(TextFormatting.AQUA);

            for (String host : HOSTS) {
                TextComponentString hostComponent = new TextComponentString(host);
                hostComponent.getStyle().setColor(TextFormatting.AQUA);

                String result = ping(host);
                TextComponentString pingComponent = new TextComponentString(result);
                pingComponent.getStyle().setColor(result.equals("Nicht erreichbar") ? TextFormatting.RED : TextFormatting.GOLD);

                p.sendMessage(textBegin.createCopy().appendSibling(hostComponent).appendSibling(textMid).appendSibling(pingComponent));
            }
        }).start();
        return true;
    }

    @SneakyThrows
    private String ping(String host) {
        ProcessBuilder processBuilder = new ProcessBuilder("ping", "-n", "1", "-4", host);
        Process proc = processBuilder.start();

        String pingResult = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            int i = 0;
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (++i == 3) {
                    pingResult = inputLine;
                    break;
                }
            }
        }

        return toPing(Objects.requireNonNull(pingResult));
    }

    private String toPing(String pingString) {
        if (!pingString.contains("=")) return "Nicht erreichbar";

        return pingString.split(" ")[4].split("=")[1];
    }
}
