package ar.edu.itba.ss.output.ovito;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.models.Cell;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.*;

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

    public static List<Scene> getScenesByDataframes(Map<SurfaceEntity<Particle>, OvitoDataFrame> df, List<SurfaceEntity<Particle>> particles, Integer TIME_STEP, Integer atoms, Double rc){
        List<Scene> scenes = new ArrayList<>();
        Integer time = 0;

        for (OvitoDataFrame particleDataframe: df.values()) {
            List<ParticleScene> particlesSceneList = new ArrayList<>();
            ParticleScene currentParticleScene = new ParticleScene(particleDataframe.getParticle(), ParticleStatus.CURRENT);
            particlesSceneList.add(currentParticleScene);

            Set<SurfaceEntity<Particle>> neighbours = new HashSet<>(particleDataframe.getNeighbours());

            for (SurfaceEntity<Particle> particle:neighbours) {
                ParticleScene particleScene = new ParticleScene(particle,ParticleStatus.NEIGHBOUR);
                particlesSceneList.add(particleScene);
            }

            List<SurfaceEntity<Particle>> notNeighbours = particleDataframe.getNotNeighbours(particles);
            notNeighbours.remove(particleDataframe.getParticle());

            for(SurfaceEntity<Particle> particle: notNeighbours){
                ParticleScene particleScene = new ParticleScene(particle, ParticleStatus.OTHER);
                particlesSceneList.add(particleScene);
            }

            ParticleScene radioScene = new ParticleScene(particleDataframe.getParticle(),atoms+1,rc+particleDataframe.getParticle().getEntity().getRadius());
            particlesSceneList.add(radioScene);

            int cellId = atoms + 2;
            for (Cell<Particle> cell : particleDataframe.getNotNeighbourCells()) {
                ParticleScene cellScene = new ParticleScene(cell.getCenterX(), cell.getCenterY(), cellId,cell.getSize() / 2 - 0.05, ParticleStatus.CELL);
                particlesSceneList.add(cellScene);
                cellId++;
            }

            int cellNeighbourId = cellId;
            for (Cell<Particle> cell : particleDataframe.getNeighbourCells()) {
                    ParticleScene cellScene = new ParticleScene(cell.getCenterX(), cell.getCenterY(), cellNeighbourId,cell.getSize() / 2 - 0.05, ParticleStatus.NEIGHBOURCELL);
                particlesSceneList.add(cellScene);
                cellNeighbourId++;
            }

            int currentCellId = cellNeighbourId;
            Cell<Particle> current = particleDataframe.getCell();
            ParticleScene currentCellScene = new ParticleScene(current.getCenterX(),current.getCenterY(),currentCellId,current.getSize()/2 -0.05, ParticleStatus.CURRENT_CELL);
            particlesSceneList.add(currentCellScene);

            Scene scene = new Scene(time,atoms + 1 + particleDataframe.getNotNeighbourCells().size() + particleDataframe.getNeighbourCells().size() + 1 , particlesSceneList);
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
