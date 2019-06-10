package de.fuzzlemann.ucutils.utils.cape;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CustomLayerCape implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    CustomLayerCape(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer p, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!p.hasPlayerInfo() || p.isInvisible() || !p.isWearing(EnumPlayerModelParts.CAPE))
            return;

        String uuid = p.getUniqueID().toString();
        ResourceLocation cape = CapeUtil.getCape(uuid);

        if (cape == null) return;

        ItemStack itemstack = p.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemstack.getItem() == Items.ELYTRA) return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.playerRenderer.bindTexture(cape);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.125F);
        double d0 = p.prevChasingPosX + (p.chasingPosX - p.prevChasingPosX) * (double) partialTicks - (p.prevPosX + (p.posX - p.prevPosX) * (double) partialTicks);
        double d1 = p.prevChasingPosY + (p.chasingPosY - p.prevChasingPosY) * (double) partialTicks - (p.prevPosY + (p.posY - p.prevPosY) * (double) partialTicks);
        double d2 = p.prevChasingPosZ + (p.chasingPosZ - p.prevChasingPosZ) * (double) partialTicks - (p.prevPosZ + (p.posZ - p.prevPosZ) * (double) partialTicks);
        float f = p.prevRenderYawOffset + (p.renderYawOffset - p.prevRenderYawOffset) * partialTicks;
        double d3 = MathHelper.sin(f * 3.1415927F / 180.0F);
        double d4 = -MathHelper.cos(f * 3.1415927F / 180.0F);
        float f1 = (float) d1 * 10.0F;
        f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
        float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
        float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

        if (f2 < 0.0F) f2 = 0.0F;
        if (f2 >= 180.0F) f2 = 180.0F + (f2 - 180.0F) * 0.2F;

        float f4 = p.prevCameraYaw + (p.cameraYaw - p.prevCameraYaw) * partialTicks;
        f1 = f1 + MathHelper.sin((p.prevDistanceWalkedModified + (p.distanceWalkedModified - p.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

        if (p.isSneaking()) {
            f1 += 25.0F;
            GlStateManager.translate(0.0F, 0.142F, -0.0178F);
        }

        GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

        this.playerRenderer.getMainModel().renderCape(0.0625F);

        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}