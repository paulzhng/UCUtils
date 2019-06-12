package de.fuzzlemann.ucutils.utils.sound;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionHandler;
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
    }

    @Override
    public void update() {
        if (donePlaying) {
            Main.MINECRAFT.getSoundHandler().stopSound(this);

            this.xPosF = 0;
            this.yPosF = 0;
            this.zPosF = 0;

            this.volume = 0;
            return;
        }

        Vec3d vec = AbstractionHandler.getInstance().getPlayer().getEntityBoundingBox().getCenter();

        this.xPosF = (float) vec.x;
        this.yPosF = (float) vec.y;
        this.zPosF = (float) vec.z;
    }
}
