import glob
import os

import pandas as pd
from matplotlib import pyplot as plt
from utils import plots

DT = 1.0 / 24.0
LEGEND_TITLE = {'tau': '$\\tau$', 'speed': 'Velocidad deseada'}
LEGEND_UNITS = {'tau': 's', 'speed': 'm/s'}


def plot(variable_name):
    current_dir = os.path.dirname(os.path.abspath(__file__))
    input_dir = os.path.join(current_dir, '..', 'data', 'output', variable_name)

    csv_files = glob.glob(os.path.join(input_dir, '*.csv'))

    plots.set_global_font_size(16)

    fig, ax = plt.subplots()
    plots.set_plot_size(fig, 14, 6)

    for file_path in csv_files:
        file_name = os.path.basename(file_path)

        name_parts = file_name.split('-')
        tau_value = float(name_parts[1][3:-4])

        data = pd.read_csv(file_path)

        data['time'] = data['frame'] * DT

        ax.plot(data['time'], data['distance'], label=f'{tau_value:.2f} {LEGEND_UNITS[variable_name]}')

    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Distancia (m)')

    legend = plots.PlotLegend.get_legend(ax).set_title(LEGEND_TITLE[variable_name]).set_title_horizontal_alignment(
        'center').set_title_bold()
    legend.set_legend_outside()

    plt.tight_layout()
    plt.show()

variables = ['speed', 'tau']
for variable in variables:
    plot(variable)