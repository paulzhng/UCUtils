package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.keybind.KeyBindRegistry;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionHandler;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class ABuyCommand {

    private static final Pattern BUY_INTERRUPTED_PATTERN = Pattern.compile("^Verkäufer: (Tut (uns|mir) Leid|Verzeihung), unser Lager ist derzeit leer\\.$" +
            "|^Verkäufer: Dieses Produkt kostet \\d+\\$\\.$");
    private static final Timer TIMER = new Timer();
    private static int delay = 10;
    private static long lastBuy;
    private static int amount;
    private static int amountLeft;
    private static int slotIndex;

    @Command(value = "abuy", usage = "/%label% [Menge] (Delay [nur bei schlechteren PCs nötig])")
    public boolean onCommand(int tempAmount, @CommandParam(required = false, defaultValue = "10") int tempDelay) {
        if (tempAmount <= 1) return false;
        if (tempDelay <= 1) return false;

        amount = tempAmount;
        delay = tempDelay;

        Message.builder()
                .prefix()
                .of("Die Menge für ABuy wurde erfolgreich eingestellt.").color(TextFormatting.GRAY).advance()
                .newLine()
                .info()
                .of("Um das gewünschte Produkt zu kaufen, klicke nun mit ").color(TextFormatting.WHITE).advance()
                .of("'" + KeyBindRegistry.aBuy.getDisplayName().toLowerCase() + "'").color(TextFormatting.GOLD).advance()
                .of(" auf das Item im Shop.").color(TextFormatting.WHITE).advance()
                .send();
        return true;
    }

    @SubscribeEvent
    public static void onKeyboardClickEvent(GuiScreenEvent.KeyboardInputEvent e) {
        if (amount == 0) return;

        if (Keyboard.isKeyDown(KeyBindRegistry.aBuy.getKeyCode())) return;

        if (!(e.getGui() instanceof GuiContainer)) return;
        GuiContainer inv = (GuiContainer) e.getGui();

        Slot slot = inv.getSlotUnderMouse();
        if (slot == null) return;

        ItemStack is = slot.inventory.getStackInSlot(slot.getSlotIndex());
        NBTTagCompound nbt = is.getTagCompound();
        if (nbt == null) return;

        NBTTagCompound display = nbt.getCompoundTag("display");
        String lore = display.getTagList("Lore", Constants.NBT.TAG_STRING).getStringTagAt(0);
        if (!lore.startsWith("§c") || !lore.endsWith("$")) return;

        slotIndex = slot.getSlotIndex();
        amountLeft = amount;

        buy();
    }

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent e) {
        if (amountLeft == 0) return;
        if (!(e.getGui() instanceof GuiContainer)) return;

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastBuy > TimeUnit.SECONDS.toMillis(2)) {
                    amountLeft = 0;
                    slotIndex = 0;
                    return;
                }

                buy();
                if (amountLeft == 0) {
                    slotIndex = 0;
                }
            }
        }, delay);
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (amountLeft == 0) return;

        String message = e.getMessage().getUnformattedText();
        if (!BUY_INTERRUPTED_PATTERN.matcher(message).find()) return;

        amountLeft = 0;
        slotIndex = 0;
    }

    private static void buy() {
        --amountLeft;
        lastBuy = System.currentTimeMillis();

        Container container = AbstractionHandler.getInstance().getPlayer().getOpenContainer();
        Main.MINECRAFT.playerController.windowClick(container.windowId, slotIndex, 0, ClickType.QUICK_MOVE, Main.MINECRAFT.player);

        container.detectAndSendChanges();
        AbstractionHandler.getInstance().getPlayer().getInventoryContainer().detectAndSendChanges();
    }
}
