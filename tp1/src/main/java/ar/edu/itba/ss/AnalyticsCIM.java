package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.entity.SurfaceEntity;
import ar.edu.itba.ss.models.methods.BruteForce;
import ar.edu.itba.ss.models.methods.CellIndexMethod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalyticsCIM {

    private static final int L = 20;
    private static final double RC = 1.0;

    private static final double R = 0.25;

    private static final int MAX_N = 2000;

    private static final int STEP = 10;

    private static void bestM(){
        int m = (int)Math.floor(L/((RC)+2*R));
        System.out.println(m);
        int n = 10;

        StringBuilder builder = new StringBuilder();
        builder.append("N,M\n");

        while(n <= MAX_N){
            List<Particle> particles = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                particles.add(new Particle(R));
            }
            List<SurfaceEntity<Particle>> entities = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < n; i++) {
                Particle particle = particles.get(i);

                double x = random.nextDouble()*L;
                double y = random.nextDouble()*L;
                entities.add(new SurfaceEntity<>(particle,x,y));
            }

            long minDuration = Long.MAX_VALUE;

            long start, end;
            int aux_m = m;
            int best_m = m;

            while(aux_m > 0){
                start =System.nanoTime();
                CellIndexMethod.calculate(L,aux_m,RC,entities);
                end = System.nanoTime();
                long duration = end-start;
                if(Math.min(duration,minDuration) == duration){
                    best_m = aux_m;
                    minDuration = duration;
                }
                aux_m--;
            }

            builder.append(String.format("%d,%d\n",n,best_m));
            n+=STEP;

        }

        String outputPath = "output/analytics.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static  void algorithmsComparison(String algorithm){
        int m = (int)Math.floor(L/((RC)+2*R));
        System.out.println(m);
        int n = 10;

        StringBuilder builder = new StringBuilder();
        builder.append("N,TIME\n");

        while(n <= MAX_N){
            List<Particle> particles = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                particles.add(new Particle(R));
            }
            List<SurfaceEntity<Particle>> entities = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < n; i++) {
                Particle particle = particles.get(i);

                double x = random.nextDouble()*L;
                double y = random.nextDouble()*L;
                entities.add(new SurfaceEntity<>(particle,x,y));
            }

            long minDuration = Long.MAX_VALUE;

            long start, end;
            int aux_m = m;
            int best_m = m;

            while(aux_m > 0){

                if(algorithm.equals("BF") ){
                    start =System.nanoTime();
                    BruteForce.calculate(entities,RC);
                    end = System.nanoTime();
                } else{
                    start =System.nanoTime();
                    CellIndexMethod.calculate(L,aux_m,RC,entities);
                    end = System.nanoTime();
                }


                long duration = end-start;
                if(Math.min(duration,minDuration) == duration){
                    best_m = aux_m;
                    minDuration = duration;
                }
                aux_m--;
            }

            builder.append(String.format("%d,%.4f\n",n,minDuration/1000000.0));
            n+=STEP;

        }

        String outputPath = "output/analytics_" + algorithm.toLowerCase() + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String run = "";
        if(args.length > 0){
            run = args[0];
        }
        if(run.equals("BEST_M")){
            bestM();
        } else {
            algorithmsComparison("BF");
            algorithmsComparison("CIM");
        }
    }
}
