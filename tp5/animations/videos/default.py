import colorsys
from typing import Sequence

import cv2
import pandas as pd
from pandas import DataFrame

from lib.video.builder import VideoBuilder

video_name = "default.mp4"
video_width = 1280
video_height = 720

show_numbers = False


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

        color = (255, 255, 255)
        particle = row['particle']
        team = row['team']

        if particle == 'lunatic':
            color = (0, 255, 255)
        if particle == 'ball':
            ball = (x, y)
            continue

        else:
            if team == 'home':
                color = (255, 0, 0)
            elif team == 'away':
                color = (0, 0, 255)

        cv2.circle(video_builder.current_frame, (x, y), 8,
                   color, -1)

        cv2.circle(video_builder.current_frame, (x, y), 8,
                   (0,0,0), 2)

        text = row['particle']
        if show_numbers:
            if is_number(text):
                font = cv2.FONT_HERSHEY_SIMPLEX
                font_scale = 0.5
                font_thickness = 1
                text_size, _ = cv2.getTextSize(text, font, font_scale, font_thickness)
                text_x = x - text_size[0] // 2
                text_y = y + text_size[1] // 2

                # Draw the text
                cv2.putText(video_builder.current_frame, text, (text_x, text_y), font, font_scale, (255, 255, 255), font_thickness,
                            cv2.LINE_AA)

    cv2.circle(video_builder.current_frame, ball, 5,
               (255, 255, 255), -1)


def render():
    video_builder = VideoBuilder("", video_name).set_width(video_width).set_height(video_height)

    # Load the field image and resize it to match the video dimensions
    field_image = cv2.imread('../assets/field.png')
    field_image = cv2.resize(field_image, (video_width, video_height))

    simulation_file = '../../output/varying_speed-vd1.00-tau0.50.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['frame'].unique()
    for timestep in timesteps:
        timestep_data = data[data['frame'] == timestep]

        video_builder.create_frame()

        # Draw the field image as the background
        video_builder.current_frame = field_image.copy()

        draw_particles(video_builder, timestep_data)

        video_builder.push_frame()

    video_builder.render()


render()
