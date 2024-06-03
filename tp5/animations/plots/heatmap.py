import colorsys
from typing import Sequence

import cv2
import numpy as np
import pandas as pd
from matplotlib import pyplot as plt
from matplotlib.colors import ListedColormap
from pandas import DataFrame

from lib.video.builder import VideoBuilder

field_width = 105
field_height = 68

cell_size = 3
total_minutes = 55.4

num_cells_x = field_width // cell_size
num_cells_y = field_height // cell_size

target_player = None


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

        particle = row['particle']
        is_goalkeeper = particle == "11" or particle == "25"

        if particle == 'ball':
            continue

        if target_player is None and (particle == 'lunatic' or is_goalkeeper):
            continue

        if target_player is None or particle == target_player:
            cell_x = min(int(x // cell_size), num_cells_x - 1)
            cell_y = min(int(y // cell_size), num_cells_y - 1)

            heatmap[cell_y, cell_x] += 1


def plot():
    heatmap = np.zeros((num_cells_y, num_cells_x))

    simulation_file = '../../output/full/futball-vd5.00-tau0.50.csv'
    data = pd.read_csv(simulation_file)

    timesteps = data['frame'].unique()
    for timestep in timesteps:
        timestep_data = data[data['frame'] == timestep]

        compute_presence(timestep_data, heatmap)

    heatmap = heatmap / total_minutes

    fig, ax = plt.subplots(figsize=(10, 6))

    background_image = plt.imread('../assets/field-crop.png')

    brightness_factor = 0.2
    background_image = background_image * brightness_factor
    background_image = np.clip(background_image, 0, 1)
    ax.imshow(background_image, extent=[0, field_width, field_height, 0], aspect='auto')

    cmap = plt.cm.turbo
    cmap_colors = cmap(np.arange(cmap.N))
    cmap_colors[:, -1] = np.linspace(0, 1, cmap.N) ** 0.3
    cmap_colors[0, -1] = 0
    transparent_cmap = ListedColormap(cmap_colors)

    im = ax.imshow(heatmap, interpolation='gaussian', origin='upper',
                   extent=[0, field_width, field_height, 0], cmap=transparent_cmap)
    plt.colorbar(im, ax=ax, label='Número de visitas por minuto')

    plt.xlabel('Ancho del campo de juego (m)')
    plt.ylabel('Largo del campo de juego (m)')

    ax.xaxis.set_ticks_position('top')
    ax.xaxis.set_label_position('top')

    plt.tight_layout()
    plt.savefig(f'player-{target_player}-presence.png')
    plt.show()


plot()
