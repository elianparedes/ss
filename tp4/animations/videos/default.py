import colorsys
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 800
video_height = 800
grid_size = 1 * 10 ** 9
visiting_area_color: Sequence[float] = (50, 50, 50)


def draw_particles(video_builder: VideoBuilder, data: DataFrame):
    # Draw particles relative to the Sun
    for index, row in data.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int((row['y'] / grid_size) * video_height)

        x += video_width // 2
        y += video_height // 2

        video_builder.draw_frame(lambda frame: cv2.circle(frame, (x, y), int((1 * 10 ** 7 / grid_size) * video_width), (255, 255, 255), -1))

def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/solarium-pericardium.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    for i, timestep in enumerate(timesteps):
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        draw_particles(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


render()
