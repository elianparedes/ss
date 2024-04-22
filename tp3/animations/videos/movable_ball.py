import math
from typing import Sequence

import cv2
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
ball_color = (255, 255, 255)
triangle_length = 8
triangle_base_ratio = 2.5

vector_color = (255, 255, 255)
vector_length = 15

collided_particles = []
collided_ball = []
prev_state = {}


def check_collision(x_particle, y_particle, radius_particle, x_circle, y_circle, radius_circle):
    distance = math.sqrt((x_particle - x_circle) ** 2 + (y_particle - y_circle) ** 2)
    return distance <= radius_particle + radius_circle


def check_wall_collision(square_size, particle_position, particle_radius):
    square_width, square_height = square_size

    particle_x, particle_y = particle_position

    if particle_x - particle_radius <= 0 or particle_x + particle_radius >= square_width:
        return True

    if particle_y - particle_radius <= 0 or particle_y + particle_radius >= square_height:
        return True

    return False


WALL_COLLISION = 1
PARTICLES_COLLISION = 2
BALL_COLLISION = 3


def get_collision_type(colliding_particles):
    if len(colliding_particles) == 2:
        return PARTICLES_COLLISION
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
            colliding_particles.append(current['id'])

    return colliding_particles


def draw_particles(video_builder: VideoBuilder, state: DataFrame, prevState: DataFrame):
    global collided_particles
    global collided_ball

    colliding_particles = get_colliding_particles(state, prevState)
    collision_type = get_collision_type(colliding_particles)

    for index, row in state.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int(
            (row['y'] / grid_size) * video_height)

        id = row['id']

        particle_color = default_color
        if id in colliding_particles:
            if collision_type == PARTICLES_COLLISION:
                particle_color = is_visiting_color
            if collision_type == WALL_COLLISION:
                particle_color = has_visited_color

        video_builder.draw_frame(
            lambda frame: cv2.circle(frame, (x, y), int((row['radius'] / grid_size) * video_width), particle_color, -1))


def draw_ball(video_builder: VideoBuilder):
    video_builder.draw_frame(
        lambda frame: cv2.circle(frame, (int(video_width / 2), int(video_height / 2)),
                                 int((0.005 / grid_size) * video_width), (255, 255, 255), 2))


def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/msd/v10/msd_1.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    previous_time = timesteps[0]
    for i, timestep in enumerate(timesteps):
        timestep_data = data[data['time'] == timestep]
        previous_timestep_data = data[data['time'] == previous_time]

        video_builder.create_frame()

        draw_particles(video_builder, timestep_data, previous_timestep_data)

        video_builder.push_frame()

        previous_time = timestep

    video_builder.render()


render()
