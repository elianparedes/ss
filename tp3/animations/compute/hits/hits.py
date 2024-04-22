import colorsys
import math
from ctypes import Array
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder


collided_particles = set()
all_hits = 0
unique_hits = 0

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


def get_colliding_particles(state: DataFrame, prev_state: DataFrame):
    colliding_particles = []

    for ((i, current), (j, previous)) in zip(state.iterrows(), prev_state.iterrows()):
        currentVx = current['vx']
        currentVy = current['vy']
        previousVx = previous['vx']
        previousVy = previous['vy']

        if currentVx != previousVx or currentVy != previousVy:
            colliding_particles.append((previous, current))

    return colliding_particles


def calculate_hits(states: list[DataFrame]):
    global all_hits
    global unique_hits

    prevState = states[0]
    for state in states[1:]:
        colliding_particles = get_colliding_particles(state, prevState)
        collision_type = get_collision_type(colliding_particles)

        prevState = state

        if collision_type == BALL_COLLISION:
            all_hits += 1

            current, _ = colliding_particles[0]
            current_id = current['id']
            if current_id not in collided_particles:
                unique_hits += 1
                collided_particles.add(current_id)


    return all_hits, unique_hits


def get_dt(data: DataFrame):
    timesteps = data['time'].unique()
    dt_differences = np.diff(timesteps)
    dt_stddev = np.std(dt_differences, ddof=1) / np.sqrt(len(dt_differences))
    dt_mean = np.mean(dt_differences)

    return dt_mean, dt_stddev


def compute(fileName):
    global all_hits
    global unique_hits
    global collided_particles
    simulation_file = f"../../../output/hits/v1/{fileName}"
    data = pd.read_csv(simulation_file)
    dt, _ = get_dt(data)
    dt *= 100

    print(dt)

    timesteps = data['time'].unique()
    all_hits_values = []
    unique_hits_values = []
    intervals_values = []

    intervalNumber = 0
    timesteps_window = []
    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        if timestep <= (intervalNumber + 1) * dt:
            timesteps_window.append(timestep_data)
        else:
            a, u = calculate_hits(timesteps_window)
            intervals_values.append(intervalNumber * dt)
            all_hits_values.append(a)
            unique_hits_values.append(u)
            timesteps_window = []
            intervalNumber += 1

    df = pd.DataFrame()
    df['all_hits'] = all_hits_values
    df['unique_hits'] = unique_hits_values
    df['interval'] = intervals_values

    df.to_csv(f'output/v1/{fileName}', index=False)

    all_hits = 0
    unique_hits = 0
    collided_particles = set()

    return df

compute(fileName='hits_30k_1.csv')
compute(fileName='hits_30k_2.csv')
compute(fileName='hits_30k_3.csv')
compute(fileName='hits_30k_4.csv')
compute(fileName='hits_30k_5.csv')
