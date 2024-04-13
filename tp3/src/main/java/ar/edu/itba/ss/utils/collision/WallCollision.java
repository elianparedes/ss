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

            collidingParticle.add(MovableSurfaceEntity.collisionWithFixed(p, this.border));
        }

        return collidingParticle;
    }
}
