package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class InternetTestCommand implements CommandExecutor {
    private final List<String> hosts = Arrays.asList("unicacity.de", "fuzzlemann.de", "google.de");

    @Override
    @Command(labels = {"internettest", "inettest"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        new Thread(() -> {
            Message.MessageBuilder builder = Message.builder();

            for (int i = 0; i < hosts.size(); i++) {
                String host = hosts.get(i);
                String result;
                try {
                    result = ping(host);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                TextFormatting color = result.equals("Nicht erreichbar") ? TextFormatting.RED : TextFormatting.GOLD;

                builder.of("Ping zu " + host + ": ").color(TextFormatting.AQUA).advance()
                        .of(result).color(color).advance();

                if (i != hosts.size() - 1) builder.newLine();
            }

            p.sendMessage(builder.build().toTextComponent());
        }).start();
        return true;
    }

    private String ping(String host) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("ping", "-n", "1", "-4", host);
        Process proc = processBuilder.start();

        String pingResult = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8))) {
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
