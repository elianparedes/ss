package ar.edu.itba.ss.utils.collision;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.entity.SurfaceEntity;
import ar.edu.itba.ss.utils.models.Border;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class WallCollision extends Collision {

    public WallCollision(double time, MovableSurfaceEntity<Particle> particle, SurfaceEntity<Border> border) {
        super(time);

        this.particlesInvolved.add(particle);
        this.border = border;
    }

    @Override
    public List<MovableSurfaceEntity<Particle>> computeCollision() {
        List<MovableSurfaceEntity<Particle>> collidingParticle = new ArrayList<>();

        if (this.particlesInvolved.size() == 1) {
            MovableSurfaceEntity<Particle> p = this.particlesInvolved.get(0);
            double vx = p.getXSpeed();
            double vy = p.getYSpeed();

            Border border = this.border.getEntity();

            if (border.getP1().getX() == border.getP2().getX()) {
                // Vertical Wall
                vx = -vx;
            } else if (border.getP1().getY() == border.getP2().getY()) {
                // Horizontal Wall
                vy = -vy;
            }

            double newAngle = Math.atan2(vy, vx);
            collidingParticle.add(new MovableSurfaceEntity<>(p.getEntity(), p.getX(), p.getY(), p.getSpeed(), newAngle));
        }

        return collidingParticle;
    }
}
