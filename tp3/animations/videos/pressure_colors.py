import colorsys
import math
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 800
video_height = 800
grid_size = 0.1
visiting_area_color: Sequence[float] = (50, 50, 50)

default_color = (100, 100, 100)
has_visited_color = (0, 0, 255)
is_visiting_color = (223.78, 49.086, 48.442)
triangle_length = 8
triangle_base_ratio = 2.5

vector_color = (255, 255, 255)
vector_length = 15

collided_particles = set()
all_hits = 0
unique_hits = 0

Dt = 0.06
i = 0
current_time = 0


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
        print('ball')
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


def update_collision_lists(current_data, prev_data, collided_with_wall, collided_with_ball):
    colliding_particles = get_colliding_particles(current_data, prev_data)

    if not colliding_particles:
        return

    collision_type = get_collision_type(colliding_particles)

    for previous, current in colliding_particles:
        particle_id = current['id'] if isinstance(current, dict) else current.at['id']

        if collision_type == WALL_COLLISION:
            if particle_id not in collided_with_wall:
                collided_with_wall.append(particle_id)
        elif collision_type == BALL_COLLISION:
            if particle_id not in collided_with_ball:
                collided_with_ball.append(particle_id)

def draw_particles(video_builder: VideoBuilder, state: DataFrame, collided_with_wall, collided_with_ball):

    for index, row in state.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int(
            (row['y'] / grid_size) * video_height)
        id = row['id']

        # Determinar el color de la partícula
        if id in collided_with_wall:
            particle_color = (255,0,0)  # Define este color
        elif id in collided_with_ball:
            particle_color = (0,165,255)  # Define este color
        else:
            particle_color = (100,100,100) # Define un color por defecto

        if id == 1:
            particle_color = (255,255,255)
        # Dibujar la partícula
        video_builder.draw_frame(
            lambda frame: cv2.circle(frame, (x, y), int((row['radius'] / grid_size) * video_width), particle_color, -1))

def draw_ball(video_builder: VideoBuilder):
    video_builder.draw_frame(
        lambda frame: cv2.circle(frame, (int(video_width / 2), int(video_height / 2)),
                                 int((0.005 / grid_size) * video_width), (255, 255, 255), 2))

def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)
    video_builder.set_fps(120)

    simulation_file = '../../output/pressure_V10.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    previous_time = timesteps[0]

    collided_with_wall = []
    collided_with_ball = []

    j = 1
    dt = 0.0004
    for i, timestep in enumerate(timesteps[:5000]):
        timestep_data = data[data['time'] == timestep]
        previous_timestep_data = data[data['time'] == previous_time]

        video_builder.create_frame()

        if timestep > dt * j:
            print('entre')
            collided_with_wall.clear()
            collided_with_ball.clear()
            j += 1
        if timestep != timesteps[0]:
            update_collision_lists(timestep_data, previous_timestep_data, collided_with_wall, collided_with_ball)

        draw_particles(video_builder, timestep_data,collided_with_wall,collided_with_ball)

        video_builder.push_frame()

        previous_time = timestep

    video_builder.render()


render()