import colorsys
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.video_builder import VideoBuilder

video_name = "va-rho-4000-20-2.mp4"
video_width = 800
video_height = 800
grid_size = 5
visiting_area_color: Sequence[float] = (50, 50, 50)

default_color = (100, 100, 100)
has_visited_color = (0, 0, 255)
is_visiting_color = (223.78, 49.086, 48.442)
triangle_length = 8
triangle_base_ratio = 2.5

vector_color = (255, 255, 255)
vector_length = 15


def draw_particles(video_builder: VideoBuilder, data: DataFrame):
    for index, row in data.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int(
            (row['y'] / grid_size) * video_height)

        # Render particles

        angle = float(row['angle'])
        color = default_color

        # Map the angle to an HSL color value
        hue = int((angle % (2 * np.pi)) * 180 / np.pi)
        saturation = 100
        lightness = 50

        # Convert HSL to RGB for OpenCV
        rgb_color = colorsys.hls_to_rgb(hue / 360, lightness / 100, saturation / 100)
        color = tuple(int(255 * c) for c in rgb_color)

        # Render line to represent the movement direction
        x2 = x + int(vector_length * np.cos(angle))
        y2 = y + int(vector_length * np.sin(angle))

        video_builder.draw_frame(lambda frame: cv2.line(frame, (x, y), (x2, y2), vector_color, 1))

        # Render a triangle pointing in the movement direction
        tip_x = x + int(triangle_length * np.cos(angle))
        tip_y = y + int(triangle_length * np.sin(angle))

        base_left_x = x + int(triangle_length * np.cos(angle + np.pi * 2 / triangle_base_ratio))
        base_left_y = y + int(triangle_length * np.sin(angle + np.pi * 2 / triangle_base_ratio))

        base_right_x = x + int(triangle_length * np.cos(angle - np.pi * 2 / triangle_base_ratio))
        base_right_y = y + int(triangle_length * np.sin(angle - np.pi * 2 / triangle_base_ratio))

        triangle_pts = np.array([[tip_x, tip_y], [base_left_x, base_left_y], [base_right_x, base_right_y]], np.int32)
        triangle_pts = triangle_pts.reshape((-1, 1, 2))

        video_builder.draw_frame(lambda frame: cv2.fillPoly(frame, [triangle_pts], color))


def draw_stats(video_builder: VideoBuilder, data: DataFrame):
    timestep = data['time'].values[0]
    va = data['va'].values[0]

    text = f"TIMESTEP: {timestep}, VA: {va}"

    video_builder.draw_frame(lambda frame:
                             cv2.putText(video_builder.current_frame, text,
                                         (20, 40), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1))

def pbc_visitors(show_stats: bool):
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/default/default_04_04_2024_21_07_39.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        draw_particles(video_builder, timestep_data)

        if show_stats:
            draw_stats(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


pbc_visitors(False)
