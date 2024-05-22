import colorsys
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from matplotlib import pyplot as plt
from pandas import DataFrame

from lib.video.builder import VideoBuilder

field_width = 105
field_height = 68

cell_size = 5

num_cells_x = field_width // cell_size
num_cells_y = field_height // cell_size

target_player = "lunatic"

def is_number(s: str) -> bool:
    try:
        float(s)
        return True
    except ValueError:
        return False


def compute_presence(data: DataFrame, heatmap: np.array):
    for index, row in data.iterrows():
        x = row['x']
        y = row['y']

        x *= field_width
        y *= field_height

        particle = row['particle']
        if particle == 'Ball':
            continue

        if target_player is None or particle == target_player:
            cell_x = min(int(x // cell_size), num_cells_x - 1)
            cell_y = min(int(y // cell_size), num_cells_y - 1)

            heatmap[cell_y, cell_x] += 1


def plot():
    heatmap = np.zeros((num_cells_y, num_cells_x))

    simulation_file = '../../output/futball-vd3.00-tau0.50'
    data = pd.read_csv(simulation_file)

    timesteps = data['frame'].unique()
    for timestep in timesteps:
        timestep_data = data[data['frame'] == timestep]

        compute_presence(timestep_data, heatmap)

    fig, ax = plt.subplots(figsize=(10, 6))
    im = ax.imshow(heatmap, interpolation='nearest', origin='upper',
               extent=[0, field_width, field_height, 0])
    plt.colorbar(im, ax=ax, label='Número de visitas')

    if target_player:
        plt.title(f"Presencia del jugador {target_player} en el campo de juego")
    else:
        plt.title(f"Presencia de todos los jugadores en el campo de juego")

    plt.xlabel('Ancho del campo de juego ($m$)')
    plt.ylabel('Largo del campo de juego ($m$)')

    ax.xaxis.set_ticks_position('top')
    ax.xaxis.set_label_position('top')

    plt.tight_layout()
    plt.show()

plot()
