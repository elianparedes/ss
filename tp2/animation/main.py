import colorsys
from typing import Union

import cv2
import numpy as np
import pandas as pd
from pandas import DataFrame, Series

from lib.video.video_builder import VideoBuilder


# Draws the particles in the plane
def draw_particles(timestep_data: Union[Series, None, DataFrame], grid_size: int, video_builder: VideoBuilder,
                   show_visitors: bool):
    for index, row in timestep_data.iterrows():
        x, y = int((row['x'] / grid_size) * video_builder.width), int(
            (row['y'] / grid_size) * video_builder.height)

        angle = float(row['angle'])
        color = (100, 100, 100)

        if show_visitors:
            has_visited = bool(row['has_visited'])
            is_visiting = bool(row['is_visiting'])
            if has_visited: color = (0, 0, 255)
            if is_visiting: color = (223.78, 49.086, 48.442)
        else:
            # Map the angle to an HSL color value
            hue = int((angle % (2 * np.pi)) * 180 / np.pi)
            saturation = 100
            lightness = 50

            # Convert HSL to RGB for OpenCV
            rgb_color = colorsys.hls_to_rgb(hue / 360, lightness / 100, saturation / 100)
            color = tuple(int(255 * c) for c in rgb_color)

        # Add a line to represent the movement direction
        x2 = x + int(15 * np.cos(angle))
        y2 = y + int(15 * np.sin(angle))

        video_builder.draw_frame(lambda frame: cv2.line(frame, (x, y), (x2, y2), (255, 255, 255), 1))

        # Add a triangle pointing in the movement direction
        length = 8
        tip_x = x + int(length * np.cos(angle))
        tip_y = y + int(length * np.sin(angle))

        base_left_x = x + int(length * np.cos(angle + np.pi * 2 / 2.5))
        base_left_y = y + int(length * np.sin(angle + np.pi * 2 / 2.5))

        base_right_x = x + int(length * np.cos(angle - np.pi * 2 / 2.5))
        base_right_y = y + int(length * np.sin(angle - np.pi * 2 / 2.5))

        triangle_pts = np.array([[tip_x, tip_y], [base_left_x, base_left_y], [base_right_x, base_right_y]], np.int32)
        triangle_pts = triangle_pts.reshape((-1, 1, 2))

        video_builder.draw_frame(lambda frame: cv2.fillPoly(frame, [triangle_pts], color))


def draw_timestep(video_builder: VideoBuilder, timestep: int):
    cv2.putText(video_builder.current_frame, f"TIMESTEP: {timestep}", (20, 40), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)

def draw_visiting_area(video_builder: VideoBuilder, center_x: int, center_y: int, radius: int, grid_size: int):
    cv2.circle(video_builder.current_frame, (
        int((center_x / grid_size) * video_builder.width),
        int((center_y / grid_size) * video_builder.height)),
               int((radius / grid_size) * video_builder.width), (50, 50, 50), -1)


# Draws a chart in the bottom-right corner of the video showing the current polarization value
def draw_polarization_chart(points: [(float, float)], iterations: int, timestep: int, va: float,
                            video_builder: VideoBuilder, chart_width=200, chart_height=100):
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

    most_recent_csv = '../output/offlatice_02_04_2024_01_26_58.csv'

    data = pd.read_csv(most_recent_csv)

    timesteps = data['time'].unique()

    simulation_grid_size = 10
    chart_points = []

    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        # draw_visiting_area(video_builder, timestep_data['visiting_area_x'].iloc[0],
        #                    timestep_data['visiting_area_y'].iloc[0],
        #                    timestep_data['visiting_area_radius'].iloc[0],
        #                    simulation_grid_size)

        draw_particles(timestep_data, simulation_grid_size, video_builder, False)
        draw_timestep(video_builder, timestep)

        draw_polarization_chart(chart_points, len(timesteps), timestep, timestep_data['va'].iloc[0], video_builder)

        video_builder.push_frame()

    video_builder.render()


render_animation()
