package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.Point;

import java.util.*;

public class MolecularUtils {

    public static List<Particle> generateRandomParticles(MolecularParams params){
        List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < params.getN() ; i++) {
            double x , y;
            boolean notValid;

            do {
                x = Math.random()*(params.getL() - 2*params.getRadius()) + params.getRadius();
                y = Math.random()*(params.getL() - 2*params.getRadius()) + params.getRadius();

                final double finalX = x;
                final double finalY = y;

                notValid = particles.stream().anyMatch(p -> Math.pow(finalX - p.getX(),2) + Math.pow(finalY-p.getY(),2) <= Math.pow(params.getRadius() + p.getRadius(),2));
            }while (notValid);

            double angle = Math.random()*2*Math.PI;

            particles.add(new Particle(x,y,
                    Math.cos(angle)*params.getSpeed(),Math.sin(angle)*params.getSpeed(),
                    params.getRadius(),params.getMass()));
        }

        return particles;
    }

    public static List<Edge> generateEdges(MolecularParams params){
        List<Edge> edges = new ArrayList<>();
        //Left
        edges.add(new Edge(new Point(0,0),new Point(0,params.getL())));

        //Right
        edges.add(new Edge(new Point(params.getL(), 0),new Point(params.getL(), params.getL())));

        //Top
        edges.add(new Edge(new Point(0,0),new Point(params.getL(),0)));

        // Bottom
        edges.add(new Edge(new Point(0, params.getL()),new Point(params.getL(), params.getL())));

        return edges;
    }

    public static TreeSet<Collision> getInitialCollisions(List<Particle> particles, List<Edge> edges){

        TreeSet<Collision> collisions = new TreeSet<>();

        for (Particle current:particles) {
            for (Particle other: particles) {
                if(!current.equals(other)){
                    collisions.add(ParticlesCollision.getCollision(current,other));
                }
            }

            for (Edge edge: edges){
                collisions.add(EdgeCollision.getCollision(current,edge));
            }
        }

        return collisions;
    }

    public static List<Particle> evolveParticles(List<Particle> particles, double time){
        List<Particle> evolvedParticles= new ArrayList<>();
        for (Particle p: particles) {
            double newX = p.getX() + p.getVx()*time;
            double newY = p.getY() + p.getVy()*time;
            evolvedParticles.add(new Particle(p.getId(),newX,newY,p.getVx(),p.getVy(),p.getRadius(),p.getMass()));
        }

        return evolvedParticles;
    }

    public static List<Particle> applyOperator(Collision c , Particle piBefore, Particle pjBefore){

        List<Particle> result = new ArrayList<>();
        if(c instanceof ParticlesCollision){
            ParticlesCollision collision = (ParticlesCollision) c;

            Particle pj = collision.getPj();
            Particle pi = collision.getPi();

            double sigma = pi.getRadius() + pj.getRadius();
            double[] dR = {pjBefore.getX() - piBefore.getX(),pj.getY() - piBefore.getY()};
            double[] dV = {pjBefore.getVx() - piBefore.getVx(),pjBefore.getVy() - piBefore.getVy()};

            double dVdR = dV[0]*dR[0] + dV[1]*dR[1];

            double J = 2*pi.getMass()*pj.getMass()*dVdR/(sigma*(pi.getMass()+pj.getMass()));
            double Jx = ( J * dR[0] ) / sigma;
            double Jy = (J * dR[1] ) / sigma;

            Particle newPi = new Particle(pi.getId(),piBefore.getX(), piBefore.getY(), piBefore.getVx() + Jx/piBefore.getMass(), piBefore.getVy() + Jy/piBefore.getMass(), piBefore.getRadius(), piBefore.getMass());
            Particle newPj = new Particle(pj.getId(),pjBefore.getX(), pjBefore.getY(), pjBefore.getVx() - Jx/pjBefore.getMass(), pjBefore.getVy() - Jy/pjBefore.getMass(), pjBefore.getRadius(), pjBefore.getMass());

            result.add(newPi);
            result.add(newPj);

            return result;
        }
        throw new RuntimeException("No operator for collision: " + c.getClass());
    }

    public static List<Particle> applyOperator(Collision c , Particle pBefore){
        List<Particle> result = new ArrayList<>();

        if(c instanceof EdgeCollision){
            EdgeCollision collision = (EdgeCollision) c;
            Edge edge = collision.getEdge();
            Particle particle = collision.getP();

            if(edge.getP1().getX() == edge.getP2().getX()){
                result.add(new Particle(particle.getId(),pBefore.getX(), pBefore.getY(), -1* particle.getVx(), particle.getVy(), particle.getRadius(), particle.getMass()));
                return result;
            }

            if(edge.getP1().getY() == edge.getP2().getY()){
                result.add(new Particle(particle.getId(),pBefore.getX(), pBefore.getY(), particle.getVx(), -1*particle.getVy(), particle.getRadius(), particle.getMass()));
                return result;
            }
        }

        throw new RuntimeException("No operator for collision: " + c.getClass());
    }

    public static Particle findById(List<Particle> particles, int id){
        for (Particle particle : particles) {
            if (particle.getId() == id) {
                return particle;
            }
        }
        return null;
    }

    public static List<Particle> copyAndReplace(List<Particle> particles, List<Particle> toReplace){
        Map<Integer, Particle> replacementMap = new HashMap<>();

        for (Particle particle : toReplace) {
            replacementMap.put(particle.getId(), particle);
        }

        List<Particle> newList = new ArrayList<>();

        for (Particle particle : particles) {
            newList.add(replacementMap.getOrDefault(particle.getId(), particle));
        }

        return newList;
    }
}
