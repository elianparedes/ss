import colorsys
from typing import Sequence

import cv2
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 800
video_height = 800
grid_size = 1 * 10 ** 9
visiting_area_color: Sequence[float] = (50, 50, 50)
# Track previous positions for each particle
previous_positions = {}
def draw_particles(video_builder: VideoBuilder, data: DataFrame):
    global previous_positions

    # Draw particles relative to the Sun and track trajectory
    for index, row in data.iterrows():
        x, y = int((row['x'] / grid_size) * video_width), int((row['y'] / grid_size) * video_height)

        x += video_width // 2
        y += video_height // 2

        particle_id = row['name']

        # Draw trajectory if the particle has a previous position
        if particle_id in previous_positions:
            for center in previous_positions[particle_id]:
                cv2.circle(video_builder.current_frame, center, 1,(120, 120, 120), -1)

        # Draw current position
        cv2.circle(video_builder.current_frame, (x, y), int((1 * 10 ** 7 / grid_size) * video_width), (255, 255, 255), -1)

        # Update previous position
        if particle_id not in previous_positions:
            previous_positions[particle_id] = []
        else:
            previous_positions[particle_id].append((x, y))

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
