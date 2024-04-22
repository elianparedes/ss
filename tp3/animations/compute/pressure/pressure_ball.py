import numpy as np
import pandas as pd
from pandas import DataFrame

DT = 0.0004

collided_particles = []

WALL_COLLISION = 1
PARTICLES_COLLISION = 2
BALL_COLLISION = 3


def get_collision_type(colliding_particles):
    if len(colliding_particles) == 2:
        return PARTICLES_COLLISION

    previous, current = colliding_particles[0]
    x = current['x']
    y = current['y']

    if (x - 0.05) ** 2 + (y - 0.05) ** 2 <= (0.005 + 0.003) ** 2:
        return BALL_COLLISION
    else:
        return WALL_COLLISION


def get_colliding_particles(state: DataFrame, prevState: DataFrame):
    colliding_particles = []

    for ((i, current), (j, previous)) in zip(state.iterrows(), prevState.iterrows()):
        currentVx = current['vx']
        currentVy = current['vy']
        previousVx = previous['vx']
        previousVy = previous['vy']

        if currentVx != previousVx or currentVy != previousVy:
            colliding_particles.append((previous, current))

    return colliding_particles


def calculate_normal_v(ball, particle):
    ballX = ball['x'].iloc[0]
    ballY = ball['y'].iloc[0]

    particleX = particle['x']
    particleY = particle['y']

    particleVx = particle['vx']
    particleVy = particle['vy']

    rp_ro = np.subtract([particleX, particleY], [ballX, ballY])
    rp_ro_norm = np.linalg.norm(rp_ro)

    n = rp_ro / rp_ro_norm
    return particleVx * n[0] + particleVy * n[1]


def calculate_pressure(states: list[DataFrame]):
    prevState = states[0]
    pressure_values = []
    for state in states[1:]:
        colliding_particles = get_colliding_particles(state, prevState)
        collision_type = get_collision_type(colliding_particles)

        prevState = state

        if collision_type != BALL_COLLISION:
            continue

        ball = state[state['id'] == 1]

        prevParticle, currentParticle = colliding_particles[0]

        vi = calculate_normal_v(ball, prevParticle)
        vf = calculate_normal_v(ball, currentParticle)

        pressure = np.abs(vf - vi)
        pressure_values.append(pressure)

    sum = np.sum(pressure_values)
    return sum / (2 * np.pi * 0.005 * DT)


def get_dt(data: DataFrame):
    timesteps = data['time'].unique()
    dt_differences = np.diff(timesteps)
    dt_stddev = np.std(dt_differences, ddof=1) / np.sqrt(len(dt_differences))
    dt_mean = np.mean(dt_differences)

    return dt_mean, dt_stddev


def compute():
    simulation_file = '../../../output/l0.1-n300-i20000-s10-mfalse.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    pressure_values = []
    intervals_values = []

    intervalNumber = 0
    timesteps_window = []
    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        if timestep <= (intervalNumber + 1) * DT:
            timesteps_window.append(timestep_data)
        else:
            pressure = calculate_pressure(timesteps_window)
            intervals_values.append(intervalNumber * DT)
            pressure_values.append(pressure)
            timesteps_window = []
            intervalNumber += 1

    df = pd.DataFrame()
    df['pressure'] = pressure_values
    df['interval'] = intervals_values

    df.to_csv('l0.1-n300-i20000-s10-mfalse-pressure_ball.csv', index=False)

    return df


compute()
