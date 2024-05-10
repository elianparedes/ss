import colorsys
from typing import Sequence

import cv2
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 800
video_height = 800
grid_size = 5 * 10 ** 8
visiting_area_color: Sequence[float] = (50, 50, 50)

radius = {"sun": 6378, "earth": 6378, "mars": 3389.5, "spaceship": 1500, "venus": 6378}
color = {"sun": (0, 255, 255), "earth": (0, 255, 0), "mars": (0, 0, 255), "spaceship": (255, 255, 255), "venus": (0, 75, 150)}
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
                cv2.circle(video_builder.current_frame, center, 1, (120, 120, 120), -1)

        # Draw current position
        cv2.circle(video_builder.current_frame, (x, y), int((radius[row['name']] * 1000 / grid_size) * video_width),
                   color[row['name']], -1)

        #Update previous position
        if particle_id not in previous_positions:
            previous_positions[particle_id] = []
        else:
            previous_positions[particle_id].append((x, y))

    time = data['time'].iloc[0] * 10
    # Add time text overlay
    time_text = f"Time: {time}"
    cv2.putText(video_builder.current_frame, time_text, (20, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)



def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/venus-dt-10.0-start-198.0d.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['time'].unique()
    step = 10
    selected_timesteps = timesteps[::step]
    for timestep in selected_timesteps:
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        draw_particles(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


render()
