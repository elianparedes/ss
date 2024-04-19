import colorsys
import math
from ctypes import Array
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

DT = 0.7

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


def calculate_pressure(states: list[DataFrame]):
    prevState = states[0]
    pressure_values = []
    for state in states[1:]:
        colliding_particles = get_colliding_particles(state, prevState)
        collision_type = get_collision_type(colliding_particles)

        if collision_type != WALL_COLLISION:
            continue

        for (previousParticle, currentParticle) in colliding_particles:
            previousVx = previousParticle['vx']
            previousVy = previousParticle['vy']

            currentVx = currentParticle['vx']
            currentVy = currentParticle['vy']

            if previousVx != currentVx:
                pressure = np.abs(previousVx - currentVx)
            elif previousVy != currentVy:
                pressure = np.abs(previousVy - currentVy)
            else:
                raise ValueError('This should not happen')

            pressure_values.append(pressure)
        prevState = state
    sum = np.sum(pressure_values)
    return sum / (0.4 * DT)


def get_dt(data: DataFrame):
    timesteps = data['time'].unique()
    dt_differences = np.diff(timesteps)
    dt_stddev = np.std(dt_differences, ddof=1) / np.sqrt(len(dt_differences))
    dt_mean = np.mean(dt_differences)

    return dt_mean, dt_stddev


def compute():
    simulation_file = '../../output/test-30k.csv'
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

    return df


compute()
