package ar.edu.itba.ss;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.input.DynamicFile;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SquareGrid;
import ar.edu.itba.ss.models.entity.SurfaceEntity;
import ar.edu.itba.ss.models.exceptions.ParticleOutOfBoundsException;
import ar.edu.itba.ss.models.methods.BruteForce;
import ar.edu.itba.ss.models.methods.CellIndexMethod;
import ar.edu.itba.ss.output.ovito.ParticleScene;
import ar.edu.itba.ss.output.ovito.ParticleStatus;
import ar.edu.itba.ss.output.ovito.Scene;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static final Integer TIME_STEP = 2000;

    private static final Comparator<SurfaceEntity<Particle>> orderById = Comparator.comparing(particle -> particle.getEntity().getId());

    private static List<SurfaceEntity<Particle>> getParticlesForSimulation(String inputFile, int n, double l, double r){
        List<SurfaceEntity<Particle>> particles = new ArrayList<>();

        try {
            DynamicFile dynamicFile = new DynamicFile(inputFile);
            System.out.println("Using filename: " + inputFile);
            dynamicFile.getPositions().forEach(position->{
                if(position.getX() < 0 || position.getX() > l || position.getY() < 0 || position.getY() > l){
                    throw new ParticleOutOfBoundsException();
                }
                Particle particle = new Particle(r);
                particles.add(new SurfaceEntity<>(particle,position.getX(),position.getY()));
            });
        } catch (FileNotFoundException e){

            System.out.println("Using random generated particles");
            Random random = new Random();

            for (int i = 0; i < n; i++) {
                double x = random.nextDouble()*l;
                double y = random.nextDouble()*l;
                Particle particle = new Particle(r);
                SurfaceEntity<Particle> entity = new SurfaceEntity<>(particle, x, y);
                particles.add(entity);
            }
        }

        return particles;
    }

    public static void main(String[] args) {

        //TODO: Change to match arguments index
        //int n = Integer.parseInt(args[0]);
        //int l = Integer.parseInt(args[1]);
        //double r = Double.parseDouble(args[2]);
        int n = 100;
        int l = 100;
        int m = 10;
        double r = 0.37;
        double rc = 6;

        String inputFile = (args.length == 0) ? null : args[0];

       List<SurfaceEntity<Particle>> particles = getParticlesForSimulation(inputFile,n,l,r);

        Map<SurfaceEntity<Particle>, ParticleDataframe> df = CellIndexMethod.calculateWithRadius(l, m, rc, particles);

        TreeMap<SurfaceEntity<Particle>,ParticleDataframe> orderedDf = new TreeMap<>(orderById);
        orderedDf.putAll(df);
        orderedDf.values().forEach(System.out::println);

        List<Scene> scenes = Scene.getScenesByDataframes(df, particles, TIME_STEP, n, rc);
        System.out.println(Scene.toStringScenes(scenes));
    }
}