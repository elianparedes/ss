import colorsys
from typing import Sequence

import cv2
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 1050
video_height = 680

field_width = 105
field_height = 680

def draw_particles(video_builder: VideoBuilder, data: DataFrame):
    for index, row in data.iterrows():
        x, y = int((row['x']) * video_width), int((row['y']) * video_height)

        color = (255, 255, 255)
        particle = row['particle']
        team = row['team']

        if particle == 'lunatic':
            color = (0, 255, 0)
        if particle == 'ball':
            color = (255, 255, 255)
        else:
            if team == 'home':
                color = (255, 0, 0)
            elif team == 'away':
                color = (0, 0, 255)

        # Draw current position
        cv2.circle(video_builder.current_frame, (x, y), 5,
                   color, -1)


def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    simulation_file = '../../output/futball-vd3.00-tau0.50'
    data = pd.read_csv(simulation_file)

    timesteps = data['frame'].unique()
    for timestep in timesteps:
        timestep_data = data[data['frame'] == timestep]

        video_builder.create_frame()

        draw_particles(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


render()
