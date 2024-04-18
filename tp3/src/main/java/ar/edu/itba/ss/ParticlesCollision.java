package ar.edu.itba.ss;

public class ParticlesCollision extends Collision{

    private final Particle Pi;

    private final Particle Pj;

    public ParticlesCollision(double time, Particle pi, Particle pj) {
        super(time);
        Pi = pi;
        Pj = pj;
    }

    public Particle getPi() {
        return Pi;
    }

    public Particle getPj() {
        return Pj;
    }

    public static ParticlesCollision getCollision(Particle pi, Particle pj){
        double sigma = pi.getRadius() + pj.getRadius();
        double[] dR = {pj.getX() - pi.getX(),pj.getY() - pi.getY()};
        double[] dV = {pj.getVx() - pi.getVx(),pj.getVy() - pi.getVy()};

        double dVdR = dV[0]*dR[0] + dV[1]*dR[1];
        double dVdV = dV[0]*dV[0] + dV[1]*dV[1];
        double dRdR = dR[0]*dR[0] + dR[1]*dR[1];

        double d = dVdR*dVdR - dVdV*(dRdR - sigma*sigma);

        if(dVdR >= 0 || d <0){
            return new ParticlesCollision(Double.MAX_VALUE,pi,pj);
        }

        return new ParticlesCollision((-1*(dVdR + Math.sqrt(d))/ dVdV),pi,pj);
    }

    @Override
    public int hashCode() {
        int hashPi = Pi.hashCode();
        int hashPj = Pj.hashCode();

        int lowerHash = Math.min(hashPi, hashPj);
        int higherHash = Math.max(hashPi, hashPj);

        return 31 * lowerHash + higherHash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return  true;

        if(!(obj instanceof  ParticlesCollision)) return false;

        ParticlesCollision other = (ParticlesCollision) obj;

        return (Pi.equals(other.Pi) && Pj.equals(other.Pj)) || (Pi.equals(other.Pj) && Pj.equals(other.Pi));
    }

    @Override
    public String toString() {
        return String.format("Collision: { time: %s, pi: %s , pj: %s}",getTime(),Pi,Pj);
    }

    @Override
    public int compareTo(Collision collision) {
        if(!(collision instanceof ParticlesCollision)) return super.compareTo(collision);

        ParticlesCollision particlesCollision = (ParticlesCollision) collision;

        int compareTime = super.compareTo(collision);
        if(compareTime == 0){
            int iCompare = Integer.compare(Pi.getId(),particlesCollision.getPi().getId());
            if(iCompare == 0){
                return Integer.compare(Pj.getId(), particlesCollision.getPj().getId());
            } else return iCompare;

        } else return compareTime;
    }
}
