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

# List to store IDs of collided particles
collided_particles = []
collided_ball = []


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


def draw_particles(video_builder: VideoBuilder, data: DataFrame):
    global collided_particles  # Access the global list of collided particles
    global collided_ball
    for index, row in data.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int(
            (row['y'] / grid_size) * video_height)

        is_colliding = check_collision(row['x'], row['y'], 0.001, grid_size / 2, grid_size / 2, 0.005)
        is_colliding_wall = check_wall_collision((grid_size, grid_size), (row['x'], row['y']), 0.001);

        if row['id'] in collided_particles:
            particle_color = is_visiting_color
        elif row['id'] in collided_ball:
            particle_color = has_visited_color
        else:
            particle_color = default_color

        # Draw the particle with the appropriate color
        video_builder.draw_frame(
            lambda frame: cv2.circle(frame, (x, y), int((0.001 / grid_size) * video_width), particle_color, -1))

        # # If the particle is colliding, mark it as collided
        if is_colliding_wall:
            collided_particles.append(row['id'])

            if row['id'] in collided_ball:
                collided_ball.remove(row['id'])

        elif is_colliding:
            collided_ball.append(row['id'])

            if row['id'] in collided_particles:
                collided_particles.remove(row['id'])



def draw_ball(video_builder: VideoBuilder):
    video_builder.draw_frame(
        lambda frame: cv2.circle(frame, (int(video_width / 2), int(video_height / 2)),
                                 int((0.005 / grid_size) * video_width), (255, 255, 0), 2))


def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/test.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    for i, timestep in enumerate(timesteps):
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        draw_ball(video_builder)
        draw_particles(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


render()
