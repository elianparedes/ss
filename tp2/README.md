# Automata Off Lattice: Swarming Behavior of Self-Propelled Agents

This project is a study of swarming behavior in self-propelled agents, implemented using Java and Python. It includes a CLI (`run.sh`) for executing simulations and animations, as well as tools for analyzing simulation data.

## Requirements

-   Java 11
-   Maven
-   Python 3
-   OpenCV-Python (for animations)

This project provides a CLI called `run.sh` which has three types of commands for executing different phases.

> This commands must be executed at the root directory of the project

## Configuration

### Simulation

The simulation and animation parameters are set in the `config.json` file, which has the following schema:

```json
{
    "properties": {
        "cim": {
            "properties": {
                "l": { "type": "number" },
                "m": { "type": "integer" },
                "n": { "type": "integer" },
                "rc": { "type": "integer" },
                "r": { "type": "number" }
            }
        },
        "speed": { "type": "number" },
        "max_iterations": { "type": "integer" },
        "etha": { "type": "number" }
    }
}
```

## Usage

### Animation

This command sets a Python virtual environment and then installs the required packages. This is only necessary for running Python animations

```sh
./run.sh config
```

In case of having a global `Pipenv` installation, packages can be installed directly by doing

```sh
pip install -r animation/requirements.txt
```

## Simulation

Simulations were implemented in Java. After simulations are execute, we provide different Main files for computing output data. The CLI has a set of arguments for the `simulation` directive, which allows to execute different computing variants for generating a different set of outputs.

```sh
./run.sh simulation --variant <NAME_OF_VARIANT>
```

The available variants are:

-   `va-etha`: Compute va versus etha after simulation.
-   `va-time`: Compute va versus time.
-   `va-rho`: Compute va versus rho density.
-   `va-time-particles`: Compute va versus time by executing multiple simulations with different amount of particles.
-   `visitors`: The default visitors simulation.
-   `visitors-time-etha`: Visitors simulation with time versus etha computing
-   `visitors-slope-etha`: Vistiors simulation with slope versus etha computing

## Animation

After running simulations, different types of outputs are generated inside the `output` folder. The CLI provides a way for consuming and creating animations from this generated files

```sh
./run.sh animation [--type]
```

Generating plots

-   `--va-etha`
-   `--va-etha-densities`
-   `--va-rho`
-   `--va-time`
-   `--va-time-particles`
-   `--visitors-slope-etha`
-   `--visitors-slopes`
-   `--visitors-visits-time`

Creating videos

-   `--video-default`
-   `--video-visitors`

---

## Authors

This project was made by:

-   Saul Ariel Castañeda
-   Elian Paredes

for the System Simulations course at Instituto Tecnológico de Buenos Aires.
