package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
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
public class ABuyCommand implements CommandExecutor {

    private static final Pattern BUY_INTERRUPTED_PATTERN = Pattern.compile("^Verk\u00e4ufer: (Tut (uns|mir) Leid|Verzeihung), unser Lager ist derzeit leer\\.$" +
            "|^Verk\u00e4ufer: Dieses Produkt kostet \\d+\\$\\.$");
    private static final Timer TIMER = new Timer();
    private static long lastBuy;
    private static int amount;
    private static int amountLeft;
    private static int slotIndex;

    @Override
    @Command(labels = "abuy", usage = "/%label% [Menge]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        int tempAmount;
        try {
            tempAmount = Integer.parseInt(args[0]);
        } catch (NumberFormatException exc) {
            return false;
        }

        if (tempAmount <= 1) return false;

        amount = tempAmount;

        p.sendMessage(TextUtils.simpleMessage("Die Menge wurde erfolgreich eingestellt.", TextFormatting.GREEN));
        return true;
    }

    @SubscribeEvent
    public static void onKeyboardClickEvent(GuiScreenEvent.KeyboardInputEvent e) {
        if (amount == 0) return;

        int key = Keyboard.getEventCharacter();
        if (key != 'b') return;

        if (!(e.getGui() instanceof GuiContainer)) return;
        GuiContainer inv = (GuiContainer) e.getGui();

        Slot slot = inv.getSlotUnderMouse();
        if (slot == null) return;

        ItemStack is = slot.inventory.getStackInSlot(slot.getSlotIndex());
        NBTTagCompound nbt = is.getTagCompound();
        if (nbt == null) return;

        NBTTagCompound display = nbt.getCompoundTag("display");
        String lore = display.getTagList("Lore", Constants.NBT.TAG_STRING).getStringTagAt(0);
        if (!lore.startsWith("\u00a7c") || !lore.endsWith("$")) return;

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
        }, 10);
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

        Container container = Main.MINECRAFT.player.openContainer;
        Main.MINECRAFT.playerController.windowClick(container.windowId, slotIndex, 0, ClickType.QUICK_MOVE, Main.MINECRAFT.player);

        container.detectAndSendChanges();
        Main.MINECRAFT.player.inventoryContainer.detectAndSendChanges();
    }
}
