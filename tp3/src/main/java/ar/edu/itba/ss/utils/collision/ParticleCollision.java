package ar.edu.itba.ss.utils.collision;

import ar.edu.itba.ss.utils.entity.MovableSurfaceEntity;
import ar.edu.itba.ss.utils.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleCollision extends Collision {

    public ParticleCollision(double time, MovableSurfaceEntity<Particle> p1, MovableSurfaceEntity<Particle> p2) {
        super(time);

        this.particlesInvolved.add(p1);
        this.particlesInvolved.add(p2);
    }

    @Override
    public List<MovableSurfaceEntity<Particle>> computeCollision() {
        List<MovableSurfaceEntity<Particle>> collidingParticles = new ArrayList<>();

        if (this.particlesInvolved.size() == 2) {
            MovableSurfaceEntity<Particle> p1 = this.particlesInvolved.get(0);
            MovableSurfaceEntity<Particle> p2 = this.particlesInvolved.get(1);

            collidingParticles.add(MovableSurfaceEntity.collisionMotion(p1, p2));
            collidingParticles.add(MovableSurfaceEntity.collisionMotion(p2, p1));
        }

        return collidingParticles;
    }
}
