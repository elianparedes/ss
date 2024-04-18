package ar.edu.itba.ss;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class MolecularDynamics implements Algorithm<MolecularParams> {
    @Override
    public void calculate(MolecularParams params, EventListener eventListener) {
        List<Particle> initial = MolecularUtils.generateRandomParticles(params);
        List<Edge> edges = MolecularUtils.generateEdges(params);

        double time = 0;

        while(time < params.getMaxIter()){
            TreeSet<Collision> collisions = MolecularUtils.getInitialCollisions(initial,edges);
            Collision collision = Optional.ofNullable(collisions.pollFirst()).orElseThrow(RuntimeException::new);

            double previousTime = time;
            time = collision.getTime() + previousTime;

            List<Particle> beforeCollision = MolecularUtils.evolveParticles(initial,time-previousTime);

            List<Particle> afterCollision;

            if(collision instanceof ParticlesCollision){

                ParticlesCollision particlesCollision = (ParticlesCollision) collision;
                Particle piBefore = MolecularUtils.findById(beforeCollision,particlesCollision.getPi().getId());
                Particle pjBefore = MolecularUtils.findById(beforeCollision,particlesCollision.getPj().getId());

                afterCollision = MolecularUtils.applyOperator(particlesCollision,piBefore,pjBefore);

                //System.out.println(String.format("%s : { %s, %s , %s }",particlesCollision.getClass(),time,particlesCollision.getPi(),particlesCollision.getPj()));
            }

             else if(collision instanceof EdgeCollision){
                EdgeCollision edgeCollision = (EdgeCollision) collision;
                Particle pBefore = MolecularUtils.findById(beforeCollision,edgeCollision.getP().getId());

                afterCollision = MolecularUtils.applyOperator(edgeCollision,pBefore);

                //System.out.println(String.format("%s : { %s, %s , %s }",edgeCollision.getClass(),time,edgeCollision.getP(),edgeCollision.getEdge()));
            }

             else {
                 throw new RuntimeException("Collision type: " + collision.getClass() + " not supported");
            }

             initial = MolecularUtils.copyAndReplace(beforeCollision,afterCollision);

        }
    }
}
