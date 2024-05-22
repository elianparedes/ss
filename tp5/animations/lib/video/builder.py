import os
from typing import Callable, Any

import cv2
import numpy as np
from numpy import ndarray
from dotenv import load_dotenv

DEFAULT_VIDEO_WIDTH = 1280
DEFAULT_VIDEO_HEIGHT = 720
DEFAULT_VIDEO_FPS = 60

env_path = os.path.join(os.path.dirname(__file__), '..', '..', '.env')
load_dotenv(dotenv_path=env_path)
codec = os.getenv('RENDER_CODEC', 'mp4v')

class VideoBuilder:
    def __init__(self, output_path: str, name: str):
        self.width: int = DEFAULT_VIDEO_WIDTH
        self.height: int = DEFAULT_VIDEO_HEIGHT
        self.fps: int = DEFAULT_VIDEO_FPS
        self.current_frame: ndarray = ndarray([])
        self.frames: ndarray = ndarray([])
        self.out = cv2.VideoWriter(
            os.path.join(output_path, name),
            cv2.VideoWriter.fourcc(*codec),
            self.fps,
            (self.width, self.height)
        )

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

    def draw_frame(self, draw_func: Callable[[ndarray], Any]):
        draw_func(self.current_frame)
        return self

    def push_frame(self):
        self.out.write(self.current_frame)
        self.current_frame = None
        return self

    def render(self) -> None:
        self.out.release()
        cv2.destroyAllWindows()
