import numpy as np


def calculate_distance(x1, y1, x2, y2):
    return np.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)

def calculate_speed(x,y,xp,yp,dt):
    speed_x = (x - xp) / dt
    speed_y = (y - yp) / dt
    speed = (speed_x ** 2 + speed_y ** 2) ** 0.5
    return speed