package ar.edu.itba.ss;

import ar.edu.itba.ss.simulation.algorithms.Algorithm;
import ar.edu.itba.ss.simulation.events.Event;
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
            MolecularUtils.addCollisionWithFixedParticle(collisions,initial,params.getFixedParticle(),params.getCn(),params.getCt());
            Collision collision = Optional.ofNullable(collisions.pollFirst()).orElseThrow(RuntimeException::new);

            double previousTime = time;
            time = collision.getTime() + previousTime;

            List<Particle> beforeCollision = MolecularUtils.evolveParticles(initial,time-previousTime);

            List<Particle> afterCollision;

            if(collision instanceof FixedParticleCollision){
                FixedParticleCollision fixedCollision = (FixedParticleCollision) collision;
                Particle pBefore = MolecularUtils.findById(beforeCollision,fixedCollision.getPi().getId());
                afterCollision = MolecularUtils.applyOperator(fixedCollision,pBefore);
                System.out.println(String.format("%s : { %s, %s , %s }",fixedCollision.getClass(),time,fixedCollision.getPi(),fixedCollision.getFixed()));
            }

            else if(collision instanceof ParticlesCollision){

                ParticlesCollision particlesCollision = (ParticlesCollision) collision;
                Particle piBefore = MolecularUtils.findById(beforeCollision,particlesCollision.getPi().getId());
                Particle pjBefore = MolecularUtils.findById(beforeCollision,particlesCollision.getPj().getId());

                afterCollision = MolecularUtils.applyOperator(particlesCollision,piBefore,pjBefore);

                System.out.println(String.format("%s : { %s, %s , %s }",particlesCollision.getClass(),time,particlesCollision.getPi(),particlesCollision.getPj()));
            }

             else if(collision instanceof EdgeCollision){
                EdgeCollision edgeCollision = (EdgeCollision) collision;
                Particle pBefore = MolecularUtils.findById(beforeCollision,edgeCollision.getP().getId());

                afterCollision = MolecularUtils.applyOperator(edgeCollision,pBefore);

                System.out.println(String.format("%s : { %s, %s , %s }",edgeCollision.getClass(),time,edgeCollision.getP(),edgeCollision.getEdge()));
            }

             else {
                 throw new RuntimeException("Collision type: " + collision.getClass() + " not supported");
            }

             eventListener.emit(new Event<>(new MolecularState(initial, time)));

             initial = MolecularUtils.copyAndReplace(beforeCollision,afterCollision);

        }

        eventListener.emit(new Event<>(new MolecularState(initial, time)));
    }
}
