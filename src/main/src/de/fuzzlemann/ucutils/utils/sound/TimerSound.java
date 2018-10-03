package de.fuzzlemann.ucutils.utils.sound;

import de.fuzzlemann.ucutils.Main;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

/**
 * @author Fuzzlemann
 */
public class TimerSound extends MovingSound {

    public TimerSound() {
        super(SoundUtil.TIMER, SoundCategory.MASTER);
        this.attenuationType = AttenuationType.NONE;
    }

    public void stop() {
        this.donePlaying = true;

        this.xPosF = 0;
        this.yPosF = 0;
        this.zPosF = 0;

        this.volume = 0;
    }

    @Override
    public void update() {
        if (donePlaying) return;

        Vec3d vec = Main.MINECRAFT.player.getEntityBoundingBox().getCenter();

        this.xPosF = (float) vec.x;
        this.yPosF = (float) vec.y;
        this.zPosF = (float) vec.z;
    }
}
