package ar.edu.itba.ss.output.ovito;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scene {

    private static final String TIME_HEADER = "ITEM: TIMESTEP";
    private static final String NUMBER_ATOMS_HEADER= "ITEM: NUMBER OF ATOMS";
    private static final String ATOMS_HEADER= "ITEM: ATOMS id x y z type radius";
    private Integer time;
    private Integer atoms;
    List<ParticleScene> particles;

    public Scene(Integer time, Integer atoms, List<ParticleScene> particles){
        this.time = time;
        this.atoms = atoms;
        this.particles = particles;
    }

    public static List<Scene> getScenesByDataframes(List<ParticleDataframe> df, List<SurfaceEntity<Particle>> particles, Integer TIME_STEP, Integer atoms, Double rc){
        List<Scene> scenes = new ArrayList<>();
        Integer time = 0;

        for (ParticleDataframe particleDataframe:df) {
            List<ParticleScene> particlesSceneList = new ArrayList<>();
            ParticleScene currentParticleScene = new ParticleScene(particleDataframe.getParticle(), ParticleStatus.CURRENT);
            particlesSceneList.add(currentParticleScene);

            Set<SurfaceEntity<Particle>> neighbours = new HashSet<>(particleDataframe.getNeighbours().keySet());

            for (SurfaceEntity<Particle> particle:neighbours) {
                ParticleScene particleScene = new ParticleScene(particle,ParticleStatus.NEIGHBOUR);
                particlesSceneList.add(particleScene);
            }

            List<SurfaceEntity<Particle>> notNeighbours = particleDataframe.getNotNeighbours(particles);
            notNeighbours.remove(particleDataframe.getParticle());

            for(SurfaceEntity<Particle> particle: notNeighbours){
                ParticleScene particleScene = new ParticleScene(particle,ParticleStatus.OTHER);
                particlesSceneList.add(particleScene);
            }

            ParticleScene radioScene = new ParticleScene(particleDataframe.getParticle(),atoms+1,rc);
            particlesSceneList.add(radioScene);

            Scene scene = new Scene(time,atoms+1,particlesSceneList);
            scenes.add(scene);
            time += TIME_STEP;
        }

        return scenes;
    }

    public static String toStringScenes(List<Scene> scenes){
        StringBuilder builder = new StringBuilder();
        for (Scene scene:scenes) {
            builder.append(scene);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(TIME_HEADER)
                .append('\n')
                .append(time)
                .append('\n')
                .append(NUMBER_ATOMS_HEADER)
                .append('\n')
                .append(atoms)
                .append('\n')
                .append(ATOMS_HEADER)
                .append('\n')
                .append(ParticleScene.listToString(particles));

        return builder.toString();
    }
}
