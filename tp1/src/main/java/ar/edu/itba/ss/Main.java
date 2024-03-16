package ar.edu.itba.ss;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.input.DynamicFile;
import ar.edu.itba.ss.input.StaticFile;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SquareGrid;
import ar.edu.itba.ss.models.entity.SurfaceEntity;
import ar.edu.itba.ss.models.exceptions.ParticleOutOfBoundsException;
import ar.edu.itba.ss.models.geometry.Point;
import ar.edu.itba.ss.models.methods.BruteForce;
import ar.edu.itba.ss.models.methods.CellIndexMethod;
import ar.edu.itba.ss.output.ovito.ParticleScene;
import ar.edu.itba.ss.output.ovito.ParticleStatus;
import ar.edu.itba.ss.output.ovito.Scene;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static final Integer TIME_STEP = 2000;

    private final static int N = 2000;
    private final static int L = 50;

    private final static double RC = 1.0;

    private final static double R = 0.25;

    private static final Comparator<SurfaceEntity<Particle>> orderById = Comparator.comparing(particle -> particle.getEntity().getId());

    private static List<SurfaceEntity<Particle>> getParticlesForSimulation(String inputFile, int n, double l, List<Particle> particles){

        List<SurfaceEntity<Particle>> entities = new ArrayList<>();

        try {
            DynamicFile dynamicFile = new DynamicFile(inputFile);
            System.out.println("Using filename: " + inputFile);
            List<Point> positions = dynamicFile.getPositions();
            for (int i = 0; i < n; i++) {
                Point position = positions.get(i);
                Particle particle = particles.get(i);
                if(position.getX() < 0 || position.getX() > l || position.getY() < 0 || position.getY() > l){
                    throw new ParticleOutOfBoundsException();
                }
                entities.add(new SurfaceEntity<>(particle,position.getX(),position.getY()));
            }
        } catch (FileNotFoundException e){

            System.out.println("Using random generated particles");
            Random random = new Random(1234);

            for (int i = 0; i < n; i++) {
                Particle particle = particles.get(i);

                double x = random.nextDouble()*l;
                double y = random.nextDouble()*l;
                entities.add(new SurfaceEntity<>(particle,x,y));
            }
        }

        return entities;
    }

    public static void main(String[] args) {

        String staticFileArg = (args.length == 0) ? null : args[0];
        String dynamicFileArg = (args.length == 1) ? null : args[1];

        int n, l;
        double rc, maxR;
        List<Particle> particles = new ArrayList<>();

        try {
            StaticFile staticFile = new StaticFile(staticFileArg);
            System.out.println("Using static file for parameters");
            n = staticFile.getN();
            l = staticFile.getL();
            maxR = staticFile.getMaxR();
            rc = staticFile.getRc();
            particles = staticFile.getParticles();

        } catch (FileNotFoundException e) {
            System.out.println("Using default parameters");
            n = N;
            l = L;
            rc = RC;
            maxR = R;
            for (int i = 0; i < n; i++) {
                particles.add(new Particle(R));
            }
        }

        List<SurfaceEntity<Particle>> entityParticles = getParticlesForSimulation(dynamicFileArg,n,l,particles);

        int m;
        if(args.length < 3){
            m = (int)Math.floor(l/((rc)+2*maxR));
            System.out.printf("Using M = %d%n",m);
        } else{
            m = Integer.parseInt(args[2]);
            if((double)l/m < (rc+2*maxR)) {
                System.out.printf("M cannot be: %d%n", m);
                m = (int)Math.floor(l/((rc)+2*maxR));
            }
            System.out.printf("Using M = %d%n",m);
        }

        long startTime = System.nanoTime();
        Map<SurfaceEntity<Particle>, ParticleDataframe> df = CellIndexMethod.calculate(l, m, rc,entityParticles);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        TreeMap<SurfaceEntity<Particle>,ParticleDataframe> orderedDf = new TreeMap<>(orderById);
        orderedDf.putAll(df);

        orderedDf.values().forEach(System.out::println);
        System.out.println("Tiempo de ejecución CIM: " + duration / 1000000.0 + " milisegundos");


        startTime = System.nanoTime();
        List<ParticleDataframe> bruteDf = BruteForce.calculate(entityParticles,rc);
        endTime = System.nanoTime();
        duration = endTime - startTime;

        bruteDf.forEach(System.out::println);
        System.out.println("Tiempo de ejecución BF: " + duration / 1000000.0 + " milisegundos");

        //List<Scene> scenes = Scene.getScenesByDataframes(df, entityParticles, TIME_STEP, n, rc);
        //System.out.println(Scene.toStringScenes(scenes));
    }
}