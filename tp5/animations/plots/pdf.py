import os

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import utils.plots as plots

FILE_NAME = 'player_speeds_pdf.csv'
PLOT_TEAMS ={'home': 'Local', 'away': 'Visitante'}

current_dir = os.path.dirname(os.path.abspath(__file__))
input_dir = os.path.join(current_dir, '..', 'data', 'output')
csv_file_path = os.path.join(input_dir, FILE_NAME)

data = pd.read_csv(csv_file_path)

players = data['player'].unique()

plots.set_global_font_size(20)
fig, ax = plt.subplots()
plots.set_plot_size(fig, 14, 9)

for player in players:
    player_data = data[data['player'] == player]
    team = player_data['team'].unique()[0]
    print(player)
    print(team)

    if player == 'lunatic':
        player = 'Loco'
        label = player
    else:
        label = str(player) + ' - ' + PLOT_TEAMS[team]

    ax.plot(player_data['bin_center'], player_data['probability'], marker='o', linestyle='-', label=label)

# Configurar etiquetas y título del gráfico
ax.set_xlabel('Velocidad (m/s)')
ax.set_ylabel('Densidad de probabilidad')

legend = plots.PlotLegend.get_legend(ax).set_title('Jugador').set_title_horizontal_alignment(
    'center').set_title_bold()
legend.set_legend_outside()

plt.semilogy()
plt.grid(True)
plt.tight_layout()
plt.show()
