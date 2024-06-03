import os
import numpy as np
import pandas as pd
from data.utils import calculate_speed

FILE_NAME = 'futball-vd3.00-tau0.50.csv'
PLAYERS = [('home', '10'), ('home', '5'), ('away', '15')]
EXCLUDED_PLAYERS = ['lunatic','11','25']
DT = 1.0 / 24.0
BIN_WIDTH = 0.4
MAX_SPEED = 30

current_dir = os.path.dirname(os.path.abspath(__file__))
input_dir = os.path.join(current_dir, '..', '..', 'output')
file_path = os.path.join(input_dir, FILE_NAME)

data = pd.read_csv(file_path)

all_data = []
players = data['particle'].unique()
speeds = []

for player in players:
    if player not in EXCLUDED_PLAYERS:
        player_data = data[data['particle'] == player]
        for idx in range(1, len(player_data)):
            current_row = player_data.iloc[idx]
            previous_row = player_data.iloc[idx - 1]

            speed = calculate_speed(current_row['x'],current_row['y'], previous_row['x'], previous_row['y'], DT)
            if speed < MAX_SPEED:
                speeds.append(speed)


max_speed = max(speeds)
bin_edges = np.arange(0, MAX_SPEED, BIN_WIDTH)

hist, bin_edges = np.histogram(speeds, bins=bin_edges)
N = len(speeds)
bin_width = bin_edges[1] - bin_edges[0]

bin_centers = (bin_edges[:-1] + bin_edges[1:]) / 2

pdf = hist / (bin_width * N)

integral = np.trapz(pdf, bin_centers)

for bin_center, prob in zip(bin_centers, pdf):
    all_data.append({'bin_center': bin_center, 'probability': prob})

print(integral)
output_df = pd.DataFrame(all_data)
output_dir = os.path.join(current_dir, 'output')
os.makedirs(output_dir, exist_ok=True)
output_file_path = os.path.join(output_dir, 'all_player_speeds_pdf.csv')
output_df.to_csv(output_file_path, index=False)
