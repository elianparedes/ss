# Event-Based Molecular Dynamics Simulation

This project implements an event-based molecular dynamics simulation framework. It simulates the behavior of a system of particles over time using event-driven algorithms.

## Requirements

-   Java 11
-   Maven
-   Python 3
-   OpenCV-Python (for animations)

## Configuration

### Simulation

The simulation parameters are set in the `config.json` file, which has the following schema:

```json
{
  "movable": {
    "desc": "If the obstacle is moving or not",
    "type": "boolean"
  },
  "iterations": {
    "desc": "Max number of iterations",
    "type": "integer"
  },
  "n": {
    "desc": "Number of particles",
    "type": "integer"
  },
  "l": {
    "desc": "Size of the box",
    "type": "number"
  },
  "rp": {
    "desc": "Particle radius",
    "type": "number"
  },
  "rb": {
    "desc": "Obstacle radius",
    "type": "number"
  },
  "mass_p": {
    "desc": "Particles mass",
    "type": "number"
  },
  "mass_b": {
    "desc": "Obstacle mass",
    "type": "number"
  },
  "speed": {
    "desc": "Initial particles speed",
    "type": "number"
  }
}
```

## Usage

### Simulation

Simulations were implemented in Java. After simulations are execute, we provide a Main file for computing output data of the whole simulation.

```sh
mvn exec:java -Dexec.mainClass="ar.edu.itba.ss.Main"
```

### Animation

Animations were implemented in Python inside the `animations` folder. Each script can be run with:
```sh
python animation/<ANIMATION_SCRIPT>
```

## Authors

This project was made by:

-   Saul Ariel Castañeda
-   Elian Paredes

for the System Simulations course at Instituto Tecnológico de Buenos Aires.