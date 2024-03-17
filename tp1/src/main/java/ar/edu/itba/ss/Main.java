package ar.edu.itba.ss;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.input.ArgumentHandler;
import ar.edu.itba.ss.config.CIMConfig;
import ar.edu.itba.ss.input.DynamicFile;
import ar.edu.itba.ss.input.StaticFile;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;
import ar.edu.itba.ss.models.exceptions.ParticleOutOfBoundsException;
import ar.edu.itba.ss.models.geometry.Point;
import ar.edu.itba.ss.models.methods.BruteForce;
import ar.edu.itba.ss.models.methods.CellIndexMethod;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final Integer TIME_STEP = 2000;

    private static final String CONFIG_FILE = "CIMConfig.json";
    private static final String CONFIG_FILE_ERROR = "The file CIMConfig.json was not found in resources";
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

        ArgumentHandler handler = new ArgumentHandler(args);
        String staticFileArg = handler.getStaticFileName();
        String dynamicFileArg = handler.getDynamicFileName();

        int n, l;
        double rc, maxR;
        CIMConfig config;

        try {
            config = CIMConfig.getConfig(Objects.requireNonNull(Paths.get(CONFIG_FILE).toAbsolutePath().toString()));
            n = config.getParameters().getN();
            l = config.getParameters().getL();
            rc = config.getParameters().getRc();
            maxR = config.getParameters().getR();
        } catch (Exception e) {
            throw new RuntimeException(CONFIG_FILE_ERROR);
        }

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
            for (int i = 0; i < n; i++) {
                particles.add(new Particle(maxR));
            }
        }

        List<SurfaceEntity<Particle>> entityParticles = getParticlesForSimulation(dynamicFileArg,n,l,particles);

        int m = handler.getM();
        if(m == 0 || (double)l/m < (rc+2*maxR)){
            if(m != 0){
                System.out.printf("M cannot be: %d%n", m);
            }
            m = (int)Math.floor(l/((rc)+2*maxR));
        }
        System.out.printf("Using M = %d%n",m);

        List<String> algorithms = config.getResults().getAlgorithms();

        String folderPath = "output/" + config.getResults().getOutput_folder();
        File directory = new File(folderPath);
        directory.mkdirs();

        String parametersString = String.format("L = %d ; N = %d ; M = %d; Rc = %.4f ; R (max) = %.4f\n", l, n, m, rc, maxR);

        if(algorithms.contains("CIM")){
            String filePath = folderPath + "/results_cim.txt";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            long startTime = System.nanoTime();
            Map<SurfaceEntity<Particle>, ParticleDataframe> df = CellIndexMethod.calculate(l, m, rc,entityParticles);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            TreeMap<SurfaceEntity<Particle>,ParticleDataframe> orderedDf = new TreeMap<>(orderById);
            orderedDf.putAll(df);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
                StringBuilder builder = new StringBuilder();
                builder.append(parametersString);
                builder.append(ParticleDataframe.collectionToString(orderedDf.values()));
                if(config.getResults().isTime()) {
                    builder.append(String.format("TIEMPO DE EJECUCIÓN CIM: %.2f ms\n",(double)duration/1000000));
                }
                writer.append(builder.toString());
            } catch (IOException e) {
                System.err.println("Ocurrió un error al escribir los resultados " + e.getMessage());
            }
        }

        if(algorithms.contains("BF")){
            String filePath = folderPath + "/results_bf.txt";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            long startTime = System.nanoTime();
            List<ParticleDataframe> bruteDf = BruteForce.calculate(entityParticles,rc);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
                StringBuilder builder = new StringBuilder();
                builder.append(parametersString);
                builder.append(ParticleDataframe.collectionToString(bruteDf));
                if(config.getResults().isTime()) {
                    builder.append(String.format("TIEMPO DE EJECUCIÓN BF: %.2f ms\n",(double)duration/1000000));
                }
                writer.append(builder.toString());
            } catch (IOException e) {
                System.err.println("Ocurrió un error al escribir los resultados " + e.getMessage());
            }
        }
    }
}