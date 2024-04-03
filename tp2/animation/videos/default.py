import colorsys
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.video_builder import VideoBuilder

video_name = "pbc_polarization.mp4"
video_width = 800
video_height = 800
grid_size = 10
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

    angles = data['angle'].round(1)
    counts = angles.value_counts()

    for i, (angle, count) in enumerate(zip(angles, counts)):
        # Map the angle to an HSL color value
        hue = int((angle % (2 * np.pi)) * 180 / np.pi)
        saturation = 100
        lightness = 50

        # Convert HSL to RGB for OpenCV
        rgb_color = colorsys.hls_to_rgb(hue / 360, lightness / 100, saturation / 100)
        color = tuple(int(255 * c) for c in rgb_color)

        # Render bar chart
        bar_width = 1000
        bar_height = 100

        scale_factor = bar_height / 20
        height = int(count * scale_factor)

        def render_bars(frame: np.ndarray):
            # Draw the bars on the chart image
            cv2.rectangle(frame[8:108, -200:],
                          (i * bar_width // len(angles), bar_height - height), ((i + 1) * bar_width // len(angles), bar_height), color, -1)

        video_builder.draw_frame(lambda frame: render_bars(frame))


def pbc_visitors(show_stats: bool):
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/offlatice_02_04_2024_03_29_51.csv'
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


pbc_visitors(True)
