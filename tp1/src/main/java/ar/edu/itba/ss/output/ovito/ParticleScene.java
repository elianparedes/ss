package ar.edu.itba.ss.output.ovito;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.List;

public class ParticleScene {
    private final Integer id;
    private final ParticleStatus status;
    private final Double x;
    private final Double y;
    private final Double z;
    private final Double radius;
    
    public ParticleScene(final SurfaceEntity<Particle> p, final ParticleStatus status){

        this.id = p.getEntity().getId();
        this.radius = p.getEntity().getRadius();
        this.status = status;
        this.x = p.getX();
        this.y = p.getY();
        this.z = 0.0;
    }

    public ParticleScene (final SurfaceEntity<Particle> current, Integer id, Double radius){
        this.id = id;
        this.status = ParticleStatus.RADIO;
        this.x = current.getX();
        this.y = current.getY();
        this.z = 0.0;

        this.radius = radius;
    }

    public static String listToString(final List<ParticleScene> particleSceneList){
        StringBuilder builder = new StringBuilder();
        for (ParticleScene p:
             particleSceneList) {
            builder.append(p).append('\n');
        }
        return builder.toString();
    }

    @Override
    public String toString(){
        return  String.format("%d %.4f %.4f %.4f %s %.4f", id,x,y,z,status,radius);
    }
}
