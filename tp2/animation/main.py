import colorsys
from typing import Union

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame, Series

from file.csv import get_most_recent_csv
from video.video_builder import VideoBuilder

# Draws the particles in the plane
def draw_particles(timestep_data: Union[Series, None, DataFrame], grid_size: int, video_builder: VideoBuilder):
    for index, row in timestep_data.iterrows():
        x, y = int((row['x'] / grid_size) * video_builder.width), int(
            (row['y'] / grid_size) * video_builder.height)

        angle = float(row['angle'])

        # Map the angle to an HSL color value
        hue = int((angle % (2 * np.pi)) * 180 / np.pi)
        saturation = 100
        lightness = 50

        # Convert HSL to RGB for OpenCV
        rgb_color = colorsys.hls_to_rgb(hue / 360, lightness / 100, saturation / 100)
        color = tuple(int(255 * c) for c in rgb_color)

        # Add a line to represent the movement direction
        x2 = x + int(30 * np.cos(angle))
        y2 = y + int(30 * np.sin(angle))

        video_builder.draw_frame(lambda frame: cv2.line(frame, (x, y), (x2, y2), color, 2))


# Draws a chart in the bottom-right corner of the video showing the current polarization value
def draw_polarization_chart(points: [(float, float)], iterations: int, timestep: int, va: float, video_builder: VideoBuilder, chart_width=200, chart_height=100):
    chart = np.zeros((chart_height, chart_width, 3), dtype=np.uint8)

    for point in points:
        cv2.circle(chart, point, 1, (255, 255, 255), -1)

    new_point = (int((timestep / iterations) * chart_width), chart_height - int(va * chart_height))
    cv2.circle(chart, new_point, 1, (255, 255, 255), -1)
    points.append(new_point)

    def add_chart(frame, chart):
        frame[-100:, -200:] = chart

    video_builder.draw_frame(lambda frame: add_chart(frame, chart))


def render_animation():
    video_width = 800
    video_height = 800
    video_builder = VideoBuilder().set_width(video_width).set_height(video_height)

    most_recent_csv = get_most_recent_csv('../output/')

    data = pd.read_csv(most_recent_csv)

    timesteps = data['time'].unique()

    simulation_grid_size = 5
    chart_points = []

    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        draw_particles(timestep_data, simulation_grid_size, video_builder)
        draw_polarization_chart(chart_points, len(timesteps), timestep, timestep_data['va'].iloc[0], video_builder)

        video_builder.push_frame()

    video_builder.render()


render_animation()
