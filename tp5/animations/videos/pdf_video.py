import colorsys
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 1280
video_height = 720

show_numbers = False

COLORS = {
    'red': (20, 20, 223),
    'blue': (223, 135, 20),
    'green': (20, 223, 20),
    'yellow': (20, 121, 223)
}
PLAYERS = {'home':[5,10],'away':[15]}



def is_number(s: str) -> bool:
    try:
        float(s)
        return True
    except ValueError:
        return False


def draw_particles(video_builder: VideoBuilder, data: DataFrame):
    ball = None
    for index, row in data.iterrows():
        x, y = int((row['x']) * 10), int((row['y']) * 10)

        x += 116
        y += 20

        particle = row['particle']

        if particle == 'lunatic':
            color = COLORS['red']
            thickness = 2
        elif particle == 'ball':
            ball = (x, y)
            continue
        elif particle == '15':
            color = COLORS['green']
            thickness = 2
        elif particle == '10':
            color = COLORS['blue']
            thickness = 2
        elif particle == '5':
            color = COLORS['yellow']
            thickness = 2
        else:
            color = (70, 70, 70)
            thickness = -1

        cv2.circle(video_builder.current_frame, (x, y), 8,
                   (0,0,0), thickness)
        cv2.circle(video_builder.current_frame, (x, y), 8,
                   color, -1)

    cv2.circle(video_builder.current_frame, ball, 5,
               (255, 255, 255), -1)





def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    # Load the field image and resize it to match the video dimensions
    field_image = cv2.imread('../assets/field.png')
    field_image = cv2.resize(field_image, (video_width, video_height))

    darkening_factor = 0.5

    field_image = (field_image * darkening_factor).astype(np.uint8)

    simulation_file = '../../output/futball-vd3.00-tau0.50.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['frame'].unique()
    previous = timesteps[0]
    for timestep in timesteps[1:]:
        timestep_data = data[data['frame'] == timestep]
        previous_data = data[data['frame'] == previous]

        video_builder.create_frame()

        # Draw the field image as the background
        video_builder.current_frame = field_image.copy()

        draw_particles(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


render()
