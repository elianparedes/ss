package ar.edu.itba.ss;

public class FixedParticleCollision extends ParticlesCollision{

    private final double Cn;
    private final double Ct;
    public FixedParticleCollision(double time, Particle pi, Particle fixed, double cn, double ct) {
        super(time, pi, fixed);
        Cn = cn;
        Ct = ct;
    }

    public static FixedParticleCollision getCollision(Particle pi, Particle fixed, double cn, double ct){
        ParticlesCollision particlesCollision = ParticlesCollision.getCollision(pi,fixed);
        return new FixedParticleCollision(particlesCollision.getTime(),particlesCollision.getPi(),particlesCollision.getPj(),cn,ct);
    }

    public Particle getFixed(){
        return getPj();
    }

    public double getCn() {
        return Cn;
    }

    public double getCt() {
        return Ct;
    }
}
