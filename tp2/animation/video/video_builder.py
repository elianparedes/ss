import os
from typing import Callable

import cv2
import numpy as np
from numpy import ndarray

DEFAULT_VIDEO_WIDTH = 800
DEFAULT_VIDEO_HEIGHT = 800
DEFAULT_VIDEO_FPS = 30
DEFAULT_VIDEO_NAME = 'output.mp4'
DEFAULT_VIDEO_PATH = os.getcwd()


class VideoBuilder:
    def __init__(self):
        self.name: str = DEFAULT_VIDEO_NAME
        self.output_path: str = DEFAULT_VIDEO_PATH
        self.width: int = DEFAULT_VIDEO_WIDTH
        self.height: int = DEFAULT_VIDEO_HEIGHT
        self.fps: int = DEFAULT_VIDEO_FPS
        self.current_frame: ndarray = ndarray([])
        self.frames: ndarray = ndarray([])
        self.out = cv2.VideoWriter(
            os.path.join(self.output_path, self.name),
            cv2.VideoWriter.fourcc(*'mp4v'),
            self.fps,
            (self.width, self.height)
        )

    def set_output_path(self, output_path: str):
        self.output_path = output_path
        return self

    def set_fps(self, fps: int):
        self.fps = fps
        return self

    def set_width(self, width: int):
        self.width = width
        return self

    def set_height(self, height: int):
        self.height = height
        return self

    def create_frame(self):
        self.current_frame = np.zeros((self.height, self.width, 3), np.uint8)
        return self

    def draw_frame(self, draw_func: Callable[[ndarray], None]):
        draw_func(self.current_frame)
        return self

    def push_frame(self):
        self.out.write(self.current_frame)
        self.current_frame = None
        return self

    def render(self) -> None:
        self.out.release()
        cv2.destroyAllWindows()
