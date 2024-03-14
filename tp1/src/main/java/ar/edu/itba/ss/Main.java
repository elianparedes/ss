package ar.edu.itba.ss;

import ar.edu.itba.ss.data.ParticleDataframe;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SquareGrid;
import ar.edu.itba.ss.models.entity.SurfaceEntity;
import ar.edu.itba.ss.models.methods.BruteForce;
import ar.edu.itba.ss.models.methods.CellIndexMethod;
import ar.edu.itba.ss.output.ovito.ParticleScene;
import ar.edu.itba.ss.output.ovito.ParticleStatus;
import ar.edu.itba.ss.output.ovito.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    private static final Integer TIME_STEP = 2000;

    public static void main(String[] args) {

        //TODO: Change to match arguments index
        //int n = Integer.parseInt(args[0]);
        //int l = Integer.parseInt(args[1]);
        //double r = Double.parseDouble(args[2]);

        int n = 30;
        int l = 10;
        int m = 5;
        double r = 0.1;
        double rc = 1;

        List<SurfaceEntity<Particle>> particles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            double x = random.nextDouble()*l;
            double y = random.nextDouble()*l;
            Particle particle = new Particle(r);
            SurfaceEntity<Particle> entity = new SurfaceEntity<>(particle, x, y);
            particles.add(entity);
        }

        Map<SurfaceEntity<Particle>, ParticleDataframe> df = CellIndexMethod.calculate(l, m, n, rc, particles);
        List<Scene> scenes = Scene.getScenesByDataframes(df, particles, TIME_STEP, n, rc);
        System.out.println(Scene.toStringScenes(scenes));

        //CellIndexMethod.calculate(l, m, n, r, rc,particles)
    }
}