import glob
import os
import re

import pandas as pd
from matplotlib import pyplot as plt

from utils import plots

DT = 1.0 / 24.0
LEGEND_TITLE = {'tau': 'Tiempo de relajación $\\tau$', 'speed': 'Velocidad deseada $v_d$'}
LEGEND_UNITS = {'tau': 's', 'speed': 'm/s'}

file_name_pattern = r'-(vd|tau)(\d+\.\d+)\.csv'


def plot(variable_name, value):
    current_dir = os.path.dirname(os.path.abspath(__file__))
    input_dir = os.path.join(current_dir, '..', 'data', 'output', variable_name)

    csv_files = glob.glob(os.path.join(input_dir, '*.csv'))

    plots.set_global_font_size(20)

    fig, ax = plt.subplots()
    plots.set_plot_size(fig, 18, 9)

    for file_path in csv_files:
        file_name = os.path.basename(file_path)

        match = re.search(file_name_pattern, file_name)
        if match:
            param_value = float(match.group(2))

            data = pd.read_csv(file_path)

            data['time'] = data['frame'] * DT

            ax.plot(data['time'], data['distance'], label=f"{param_value:.2f} {LEGEND_UNITS[variable_name]}")

    ax.set_xlabel('$\\text{Tiempo (s)}$')
    ax.set_ylabel('$\\text{Distancia entre el Loco y la pelota (m)}$')

    legend = plots.PlotLegend.get_legend(ax).set_title(LEGEND_TITLE[variable_name]).set_title_horizontal_alignment(
        'left').set_title_bold()
    legend.set_legend_outside()

    plt.tight_layout()
    plt.savefig(f'distance_vs_{variable_name}.png')
    plt.show()


variables = ['speed', 'tau']
values = []

for variable in variables:
    plot(variable, values)
